package com.gulshansutey.mycomposeplayground.model

import java.io.Serializable


data class OptionModel(val title: String, val subTitle: String, val icon: Int? = null) :
    BaseUiModel(), Serializable
