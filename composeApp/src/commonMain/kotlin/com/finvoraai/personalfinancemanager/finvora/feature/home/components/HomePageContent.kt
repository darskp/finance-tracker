package com.finvoraai.personalfinancemanager.finvora.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthUiState
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreenViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.theme.*
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.*
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
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
                top = padding.calculateTopPadding() + Spacing.s12,
                bottom = padding.calculateBottomPadding() + Spacing.s4,
                start = Spacing.s4,
                end = Spacing.s4
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Total Balance Card
            item {
                AppCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.s1)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.s3)
                    ) {
                        Text(
                            text = stringResource(Res.string.dashboard_total_balance),
                            style = BodyNormal(),
                            color = palette.textSecondary
                        )
                        VSpacer(Spacing.s1)
                        Text(
                            text = stringResource(Res.string.dashboard_balance_value),
                            style = H2TextStyle().copy(
                                fontWeight = FontWeight.Bold,
                                color = palette.primary
                            )
                        )
                        VSpacer(Spacing.s1)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Income Column
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_arrow_down_left),
                                        contentDescription = null,
                                        tint = palette.success,
                                        modifier = Modifier.size(Spacing.s4)
                                    )
                                    HSpacer(Spacing.s1)
                                    Text(
                                        text = stringResource(Res.string.dashboard_income_label),
                                        style = BodyNormal(),
                                        color = palette.textSecondary
                                    )
                                }
                                VSpacer(Spacing.s1)
                                Text(
                                    text = stringResource(Res.string.dashboard_income_value),
                                    style = H6TextStyle().copy(
                                        color = palette.success,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            // Expenses Column
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_arrow_up_right),
                                        contentDescription = null,
                                        tint = palette.error,
                                        modifier = Modifier.size(Spacing.s4)
                                    )
                                    HSpacer(Spacing.s1)
                                    Text(
                                        text = stringResource(Res.string.dashboard_expenses_label),
                                        style = BodyNormal(),
                                        color = palette.textSecondary
                                    )
                                }
                                VSpacer(Spacing.s1)
                                Text(
                                    text = stringResource(Res.string.dashboard_expenses_value),
                                    style = H6TextStyle().copy(
                                        color = palette.error,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Wealth Insights Section
            item {
                VSpacer(Spacing.s3)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.s1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.dashboard_wealth_insights),
                        style = H4TextStyle(),
                        color = palette.textPrimary
                    )
                    Text(
                        text = stringResource(Res.string.dashboard_view_analysis),
                        style = BodyNormal().copy(
                            color = palette.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { /* Handle Analysis Navigation */ }
                    )
                }
            }

            item {
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(vertical = Spacing.s1),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s3)
                ) {
                    // Card 1: AI Growth Forecast
                    AppCard(
                        modifier = Modifier.width(Spacing.s40 + Spacing.s8)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing.s2, vertical = Spacing.s3)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(Spacing.s8)
                                    .clip(RoundedCornerShape(Spacing.s2))
                                    .background(palette.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_trend_up),
                                    contentDescription = null,
                                    tint = palette.primary,
                                    modifier = Modifier.size(Spacing.s4)
                                )
                            }
                            VSpacer(Spacing.s2)
                            Text(
                                text = stringResource(Res.string.dashboard_ai_growth_title),
                                style = H6TextStyle().copy(fontWeight = FontWeight.Bold),
                                color = palette.textPrimary
                            )
                            VSpacer(Spacing.s1)
                            Text(
                                text = stringResource(Res.string.dashboard_ai_growth_desc),
                                style = BodySmall(),
                                color = palette.textSecondary
                            )
                        }
                    }

                    // Card 2: Savings Target achieved
                    AppCard(
                        modifier = Modifier.width(Spacing.s40 + Spacing.s8)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing.s2, vertical = Spacing.s3)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(Spacing.s8)
                                    .clip(RoundedCornerShape(Spacing.s2))
                                    .background(palette.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_piggy_bank),
                                    contentDescription = null,
                                    tint = palette.primary,
                                    modifier = Modifier.size(Spacing.s4)
                                )
                            }
                            VSpacer(Spacing.s2)
                            Text(
                                text = stringResource(Res.string.dashboard_savings_title),
                                style = H6TextStyle().copy(fontWeight = FontWeight.Bold),
                                color = palette.textPrimary
                            )
                            VSpacer(Spacing.s1)
                            Text(
                                text = stringResource(Res.string.dashboard_savings_desc),
                                style = BodySmall(),
                                color = palette.textSecondary
                            )
                        }
                    }
                }
            }

            // 4. Recent Transactions Section
            item {
                VSpacer(Spacing.s3)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.s1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.dashboard_recent_transactions),
                        style = H4TextStyle(),
                        color = palette.textPrimary
                    )
                    Text(
                        text = stringResource(Res.string.dashboard_see_all),
                        style = BodyNormal().copy(
                            color = palette.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { /* Handle See All Navigation */ }
                    )
                }
            }

            // Transaction 1: Apple Store
            item {
                TransactionRow(
                    iconRes = Res.drawable.ic_cart,
                    title = stringResource(Res.string.dashboard_tx_apple_title),
                    subtitle = stringResource(Res.string.dashboard_tx_apple_sub),
                    amount = stringResource(Res.string.dashboard_tx_apple_value),
                    isIncome = false
                )
            }

            // Transaction 2: Salary Deposit
            item {
                TransactionRow(
                    iconRes = Res.drawable.ic_wallet,
                    title = stringResource(Res.string.dashboard_tx_salary_title),
                    subtitle = stringResource(Res.string.dashboard_tx_salary_sub),
                    amount = stringResource(Res.string.dashboard_tx_salary_value),
                    isIncome = true
                )
            }

            // Transaction 3: The Green Bistro
            item {
                TransactionRow(
                    iconRes = Res.drawable.ic_restaurant,
                    title = stringResource(Res.string.dashboard_tx_bistro_title),
                    subtitle = stringResource(Res.string.dashboard_tx_bistro_sub),
                    amount = stringResource(Res.string.dashboard_tx_bistro_value),
                    isIncome = false
                )
            }

            // Transaction 4: Shell Station
            item {
                TransactionRow(
                    iconRes = Res.drawable.ic_car,
                    title = stringResource(Res.string.dashboard_tx_shell_title),
                    subtitle = stringResource(Res.string.dashboard_tx_shell_sub),
                    amount = stringResource(Res.string.dashboard_tx_shell_value),
                    isIncome = false
                )
            }

            // 5. Logout Button (Utility)
            item {
                VSpacer(Spacing.s3)
                FinvoraButton(
                    text = stringResource(Res.string.auth_logout),
                    onClick = { authViewModel.signOut() },
                    style = ButtonStyle.SECONDARY,
                    enabled = !isLoggingOut,
                    modifier = Modifier.padding(horizontal = Spacing.s4)
                )
                VSpacer(Spacing.s3)
            }
        }
    }
}

@Composable
private fun TransactionRow(
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    subtitle: String,
    amount: String,
    isIncome: Boolean
) {
    val palette = LocalAppPalette.current
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.s1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.s3, vertical = Spacing.s2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.s9)
                        .clip(RoundedCornerShape(Spacing.s2))
                        .background(palette.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = palette.primary,
                        modifier = Modifier.size(Spacing.s4)
                    )
                }
                HSpacer(Spacing.s3)
                Column {
                    Text(
                        text = title,
                        style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
                        color = palette.textPrimary
                    )
                    VSpacer(Spacing.sHalf)
                    Text(
                        text = subtitle,
                        style = BodySmall(),
                        color = palette.textSecondary
                    )
                }
            }

            Text(
                text = amount,
                style = BodyNormal().copy(fontWeight = FontWeight.Bold),
                color = if (isIncome) palette.success else palette.textPrimary
            )
        }
    }
}
