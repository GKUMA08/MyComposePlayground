package com.gulshansutey.mycomposeplayground.viewmodel

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gulshansutey.mycomposeplayground.model.BaseUiModel
import com.gulshansutey.mycomposeplayground.Repository
import com.gulshansutey.mycomposeplayground.model.ThemeItemModel
import com.gulshansutey.mycomposeplayground.ui.theme.FireColorPalette
import com.gulshansutey.mycomposeplayground.ui.theme.MyComposePlaygroundTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeListViewModel {

    private val themeList = Repository.getThemeScreenData()
    private val viewStatePublisher: MutableStateFlow<ThemeItemState> =
        MutableStateFlow(initViewState())
    private val lastThemeState: ThemeItemState
        get() = viewStatePublisher.value
    private var selectedTheme by mutableStateOf("Light")
    private fun initViewState(): ThemeItemState {
        return ThemeItemState(themeList)
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.ThemeSelected -> {
                val themeList = lastThemeState.themeList.map {
                    if (it is ThemeItemModel) {
                        if (it.title == uiAction.theme.title) {
                            selectedTheme = it.title
                        }
                        it.copy(isSelected = it.title == uiAction.theme.title)
                    } else {
                        it
                    }
                }
                emit(lastThemeState.copy(themeList = themeList))

            }
        }
    }

    sealed class UiAction {
        class ThemeSelected(val theme: ThemeItemModel) : UiAction()
    }

    private fun emit(viewState: ThemeItemState) {
        viewStatePublisher.value = viewState
    }

    fun viewStateStream(): StateFlow<ThemeItemState> {
        return viewStatePublisher.asStateFlow()
    }

    data class ThemeItemState(
        val themeList: List<BaseUiModel>
    )


    @Composable
    fun MyAppTheme(child: @Composable () -> Unit) {
        when (selectedTheme) {
            "Dark" -> MyComposePlaygroundTheme(darkTheme = true) {
                child()
            }
            "Light" -> MyComposePlaygroundTheme(darkTheme = false) {
                child()
            }
            "Fire" -> MaterialTheme(colors = FireColorPalette) {
                child()
            }
        }
    }

}