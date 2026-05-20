package com.finvoraai.personalfinancemanager.finvora.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.feature.home.components.HomePageContent
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.HomeTopAppBar
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = koinViewModel()) {
    val listState = rememberLazyListState()
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()

    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Scaffold { padding ->
        AppBackgroundScreen {
            Box(modifier = Modifier.fillMaxSize()) {
                HomePageContent(
                    navController = navController,
                    listState = listState,
                    padding = padding,
                    viewModel = viewModel
                )

                AnimatedVisibility(
                    visible = isScrolled,
                    enter = slideInVertically(
                        animationSpec = tween(Motion.APP_BAR_ANIMATION_DURATION)
                    ) { -it } + fadeIn(
                        animationSpec = tween(Motion.APP_BAR_ANIMATION_DURATION)
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(Motion.APP_BAR_ANIMATION_DURATION)
                    ) { -it } + fadeOut(
                        animationSpec = tween(Motion.APP_BAR_ANIMATION_DURATION)
                    ),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    HomeTopAppBar(
                        navController = navController
                    )
                }
            }
        }
    }
}
