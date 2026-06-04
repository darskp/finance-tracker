package com.finvoraai.personalfinancemanager.finvora.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface // import material 3 not material
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.finvoraai.personalfinancemanager.finvora.feature.chat.ChatScreen
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreen
import com.finvoraai.personalfinancemanager.finvora.feature.main.MainViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.SimpleBottomNavigation
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavigation(
    navController: NavHostController,
    onNavigateToAuth: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = viewModel.bottomNavItems

    val selectedIndex = remember(currentRoute) {
        bottomNavItems.indexOfFirst { item ->
            currentRoute?.contains(item.route::class.simpleName ?: "") == true
        }.coerceAtLeast(0)
    }

    // Check if we should show bottom bar
    val shouldShowBottomBar = remember(currentRoute) {
        viewModel.shouldShowBottomBar(currentRoute)
    }

    Surface(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = NavRoute.HomeScreen,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (shouldShowBottomBar) {
                            Modifier.padding(bottom = Spacing.s18)
                        } else {
                            Modifier
                        }
                    )
            ) {
                composable<NavRoute.HomeScreen> {
                    HomeScreen(
                        navController = navController,
                        onNavigateToAuth = onNavigateToAuth
                    )
                }

                composable<NavRoute.ChatScreen> {
                    ChatScreen(
                        navController = navController
                    )
                }
            }

            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                SimpleBottomNavigation(
                    modifier = Modifier,
                    items = bottomNavItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        val selectedRoute = bottomNavItems[index].route

                        if (index != selectedIndex) {
                            navController.navigate(selectedRoute) {
                                // Pop up to the HomeScreen to avoid building up a large stack
                                popUpTo(NavRoute.HomeScreen) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            // If re-selecting the home item, we might want to reset its state or do nothing.
                            // For now, if re-selecting Home, just pop back to it.
                            if (index == 0) {
                                navController.navigate(NavRoute.HomeScreen) {
                                    popUpTo(NavRoute.HomeScreen) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
