package com.gulshansutey.mycomposeplayground.model

data class ThemeItemModel(
    val title: String,
    val isSelected: Boolean = false
) : BaseUiModel()
