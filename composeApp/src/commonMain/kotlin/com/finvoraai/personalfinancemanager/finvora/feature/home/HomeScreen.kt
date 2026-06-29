package com.finvoraai.personalfinancemanager.finvora.feature.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthEvent
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.home.components.HomePageContent
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.HomeTopAppBar
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    viewModel: HomeScreenViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val listState = rememberLazyListState()
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()
    val palette = LocalAppPalette.current

    LaunchedEffect(Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEvent.NavigateToAuth -> onNavigateToAuth()
                else -> {}
            }
        }
    }

    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    val shouldRefresh = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh") ?: false
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.refresh()
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", false)
        }
    }

    val appBarColor by animateColorAsState(
        targetValue = if (isScrolled) {
            palette.surface
        } else {
            palette.surface.copy(alpha = 0f)
        },
        animationSpec = tween(Motion.APP_BAR_ANIMATION_DURATION)
    )

    Scaffold { padding ->
        AppBackgroundScreen {
            Box(modifier = Modifier.fillMaxSize()) {
                HomePageContent(
                    navController = navController,
                    listState = listState,
                    padding = padding,
                    viewModel = viewModel,
                    authViewModel = authViewModel
                )

                HomeTopAppBar(
                    navController = navController,
                    backgroundColor = appBarColor,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
