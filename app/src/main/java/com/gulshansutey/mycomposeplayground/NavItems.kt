package com.gulshansutey.mycomposeplayground

sealed class NavItems(var title: String, var icon: Int, var path: String) {
    object Home : NavItems("Home", R.drawable.outline_home_white_24dp, "home")
    object Data : NavItems("Explore", R.drawable.outline_data_exploration_white_24dp, "explore")
    object Anchor : NavItems("Anchor", R.drawable.outline_anchor_white_24dp, "anchor")
    object Bag : NavItems("Bag", R.drawable.outline_shopping_bag_white_24dp, "bag")
    object Theme : NavItems("Theme", R.drawable.outline_format_paint_white_24dp, "theme")
    object Details : NavItems("Details", R.drawable.outline_format_paint_white_24dp, "details")
}
