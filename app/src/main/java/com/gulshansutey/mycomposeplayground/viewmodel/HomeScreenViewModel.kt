package com.gulshansutey.mycomposeplayground.viewmodel

import com.gulshansutey.mycomposeplayground.Repository
import com.gulshansutey.mycomposeplayground.model.BaseUiModel
import com.gulshansutey.mycomposeplayground.model.OptionModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeScreenViewModel {

    private val options = Repository.getHomeOptions()
    val optionsStateFlow: MutableStateFlow<List<BaseUiModel>> =
        MutableStateFlow(options)

}
