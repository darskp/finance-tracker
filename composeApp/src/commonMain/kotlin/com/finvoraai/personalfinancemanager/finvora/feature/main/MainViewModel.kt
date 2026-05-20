package com.finvoraai.personalfinancemanager.finvora.feature.main

import androidx.lifecycle.ViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.IconSource
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.NavItem
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.home

class MainViewModel : ViewModel() {

    val bottomNavItems = listOf(
        NavItem(
            icon = IconSource.Drawable(Res.drawable.home),
            label = "Home",
            route = NavRoute.HomeScreen
        )
    )

    fun shouldShowBottomBar(currentRoute: String?): Boolean {
        if (currentRoute == null) return false

        val hideBottomBarRoutes = listOf<String?>(
            // NavRoute.AboutUs::class.simpleName
        )

        // Check if current route matches any of the hidden routes
        return hideBottomBarRoutes.none { hiddenRoute ->
            hiddenRoute != null && currentRoute.contains(hiddenRoute)
        }
    }
}
