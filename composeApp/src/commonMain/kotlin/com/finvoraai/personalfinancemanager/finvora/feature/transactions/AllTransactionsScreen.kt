@file:Suppress("MagicNumber")
package com.finvoraai.personalfinancemanager.finvora.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionType
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreenViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H6TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.HSpacer
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.VSpacer
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.cd_back
import finvoraai.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs

// ---------------------------------------------------------------------------
// AllTransactionsScreen
// ---------------------------------------------------------------------------

@Composable
fun AllTransactionsScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()
    val palette = LocalAppPalette.current

    AppBackgroundScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header — same style as ChatScreen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.surface)
                    .border(
                        width = Spacing.hairline,
                        color = palette.outline.copy(alpha = 0.4f)
                    )
                    .padding(horizontal = Spacing.s2, vertical = Spacing.s2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.cd_back),
                        tint = palette.textPrimary
                    )
                }
                HSpacer(Spacing.s2)
                Text(
                    text = "Transactions",
                    style = H6TextStyle().copy(fontWeight = FontWeight.Black),
                    color = palette.textPrimary
                )
            }

            // Body
            if (uiState.isLoading && uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = palette.primary,
                        strokeWidth = Spacing.sHalf
                    )
                }
            } else if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No transactions yet.",
                        style = BodyNormal(),
                        color = palette.textSecondary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = Spacing.s4,
                        vertical = Spacing.s3
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sHalf)
                ) {
                    items(uiState.transactions) { tx ->
                        AllTransactionRow(
                            emoji = tx.emoji,
                            title = tx.title,
                            subtitle = tx.category,
                            amount = formatTxAmount(tx.amount.toDoubleOrNull() ?: 0.0),
                            date = tx.date,
                            isIncome = tx.transactionType == TransactionType.Income
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Transaction Row — identical card style to dashboard
// ---------------------------------------------------------------------------

@Composable
private fun AllTransactionRow(
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
                .padding(horizontal = Spacing.s3, vertical = Spacing.s1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.s8)
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

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

private fun formatTxAmount(amount: Double): String {
    val absAmount = abs(amount)
    val prefix = if (amount < 0) "-" else ""
    return when {
        absAmount >= 1_000_000_000 -> "$prefix$${(absAmount / 1_000_000_000).toString().take(4).removeSuffix(".")}B"
        absAmount >= 1_000_000 -> "$prefix$${(absAmount / 1_000_000).toString().take(4).removeSuffix(".")}M"
        absAmount >= 1_000 -> "$prefix$${(absAmount / 1_000).toString().take(4).removeSuffix(".")}k"
        else -> "$prefix$${absAmount.toLong()}"
    }
}
