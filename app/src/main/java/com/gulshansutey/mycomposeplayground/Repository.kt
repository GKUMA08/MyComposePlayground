package com.gulshansutey.mycomposeplayground

import com.gulshansutey.mycomposeplayground.model.*

object Repository {

    fun getThemeScreenData() =
        mutableListOf<BaseUiModel>(
            HeaderModel("Themes")
        ).apply {
            val colors = listOf("Fire", "Dark", "Light")
            colors.forEach {
                val selected = selectedTheme == it
                add(ThemeItemModel(it, selected))
            }
        }

    var selectedTheme = "Light"

    fun getHomeOptions() = mutableListOf<BaseUiModel>(
        RitualModel(
            "Today’s ritual",
            "Co-hosting a leader-hosted livestream is an easy way to engage with your customers and increase sales. Check out this upcoming event!"
        ),
        OptionModel("Tasks", "3 Open"),
        OptionModel("Leads", "4 Updates"),
        OptionModel("Customers", "4 New"),
        OptionModel("Orders", "3 Processing"),
        OptionModel("Events", "2 Live Now"),
        OptionModel("Campaigns", "3 In Progress"),
    )

}
