package com.finvoraai.personalfinancemanager.finvora.feature.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthUiState
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreenViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.theme.*
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.*
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.auth_logout
import finvoraai.composeapp.generated.resources.home_dashboard_placeholder
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Suppress("UnusedParameter")
@Composable
fun HomePageContent(
    navController: NavController,
    listState: androidx.compose.foundation.lazy.LazyListState,
    padding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomeScreenViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()
    val isRefreshing by viewModel.isRefreshing.collectAsStateLifecycleAware()
    val pullRefreshState = rememberPullToRefreshState()
    val palette = LocalAppPalette.current

    val uiStateAuth by authViewModel.uiState.collectAsState()
    val isLoggingOut = uiStateAuth is AuthUiState.Loading

    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        modifier = Modifier.fillMaxSize(),
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = padding.calculateTopPadding()),
                isRefreshing = isRefreshing,
                state = pullRefreshState
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + Spacing.s5,
                start = Spacing.s5,
                end = Spacing.s5
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(Res.string.home_dashboard_placeholder),
                    style = H3TextStyle(),
                    color = palette.textPrimary,
                    modifier = Modifier.padding(top = Spacing.s5)
                )
            }

            item {
                VSpacer(Spacing.s8)
                FinvoraButton(
                    text = stringResource(Res.string.auth_logout),
                    onClick = { authViewModel.signOut() },
                    style = ButtonStyle.SECONDARY,
                    enabled = !isLoggingOut,
                    modifier = Modifier.padding(horizontal = Spacing.s4)
                )
                VSpacer(Spacing.s4)
            }
        }
    }
}
