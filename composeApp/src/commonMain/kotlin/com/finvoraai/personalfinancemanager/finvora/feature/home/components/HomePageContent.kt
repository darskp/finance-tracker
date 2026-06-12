package com.finvoraai.personalfinancemanager.finvora.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionType
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthUiState
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreenViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.ErrorStateView
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.components.LoadingView
import com.finvoraai.personalfinancemanager.finvora.ui.theme.*
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.*
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs

private fun formatAmount(amount: Double): String {
    val absAmount = abs(amount)
    val prefix = if (amount < 0) "-" else ""
    return when {
        absAmount >= 1_000_000_000 -> "${prefix}$${(absAmount / 1_000_000_000).toString().take(4).removeSuffix(".")}B"
        absAmount >= 1_000_000 -> "${prefix}$${(absAmount / 1_000_000).toString().take(4).removeSuffix(".")}M"
        absAmount >= 1_000 -> "${prefix}$${(absAmount / 1_000).toString().take(4).removeSuffix(".")}k"
        else -> "${prefix}$${absAmount.toLong()}"
    }
}

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
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(primary = palette.primary)
            ) {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = padding.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        }
    ) {
        if (uiState.isLoading && uiState.transactions.isEmpty()) {
            LoadingView(modifier = Modifier.fillMaxSize())
        } else if (uiState.error != null && uiState.transactions.isEmpty()) {
            ErrorStateView(
                errorMessage = Res.string.error_message_generic,
                onRetry = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            )
        } else {
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
                                text = formatAmount(uiState.totalBalance),
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
                                        text = formatAmount(uiState.totalIncome),
                                        style = H6TextStyle().copy(
                                            color = palette.success,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

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
                                        text = formatAmount(uiState.totalExpense),
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

                // 2. Wealth Insights Section
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
                            modifier = Modifier.clickable { }
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

                // 3. Recent Transactions Section
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
                            modifier = Modifier.clickable { }
                        )
                    }
                }

                if (uiState.transactions.isEmpty()) {
                    item {
                        Text(
                            text = "No recent transactions.",
                            modifier = Modifier.padding(Spacing.s4),
                            color = palette.textSecondary,
                            style = BodyNormal()
                        )
                    }
                } else {
                    items(uiState.transactions) { tx ->
                        TransactionRow(
                            emoji = tx.emoji,
                            title = tx.title,
                            subtitle = tx.category,
                            amount = formatAmount(tx.amount.toDoubleOrNull() ?: 0.0),
                            date = tx.date,
                            isIncome = tx.transactionType == TransactionType.Income
                        )
                    }
                }

                // 4. Logout Button
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
}

@Composable
private fun TransactionRow(
    emoji: String,
    title: String,
    subtitle: String,
    amount: String,
    date: String,
    isIncome: Boolean
) {
    val palette = LocalAppPalette.current
    val displayDate = remember(date) {
        if (date.length >= 10) date.substring(0, 10) else date
    }
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
                    Text(
                        text = emoji,
                        style = H4TextStyle()
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
                    VSpacer(Spacing.sHalf)
                    Text(
                        text = displayDate,
                        style = BodySmall(),
                        color = palette.textTertiary
                    )
                }
            }

            Text(
                text = amount,
                style = BodyNormal().copy(fontWeight = FontWeight.Bold),
                color = if (isIncome) palette.success else palette.error
            )
        }
    }
}
