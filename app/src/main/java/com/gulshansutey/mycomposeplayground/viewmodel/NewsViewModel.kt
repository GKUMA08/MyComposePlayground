package com.gulshansutey.mycomposeplayground.viewmodel

import androidx.lifecycle.ViewModel
import com.gulshansutey.mycomposeplayground.NewsRepository
import com.gulshansutey.mycomposeplayground.model.NewsResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val repo: NewsRepository) : ViewModel() {
    private val newsStateFlow: MutableStateFlow<NewsResponseModel> =
        MutableStateFlow(NewsResponseModel())
    val lastNewsState: NewsResponseModel
        get() = newsStateFlow.value

    suspend fun getLatestItNews() {
        repo.fetchNews().collect {
            emit(
                lastNewsState.copy(
                    status = it.status,
                    totalResults = it.totalResults,
                    articles = it.articles
                )
            )
        }
    }

    private fun emit(state: NewsResponseModel) {
        newsStateFlow.value = state
    }
}
