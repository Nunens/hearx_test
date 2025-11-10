package com.devnunens.hearx.ui.navigation

sealed class TestNavigation(val route: String) {
    object Home : TestNavigation("home_screen")
    object Test : TestNavigation("test_screen")
    object Results : TestNavigation("results_screen")
}