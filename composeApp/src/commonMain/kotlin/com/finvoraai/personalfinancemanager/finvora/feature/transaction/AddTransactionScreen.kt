@file:Suppress("MagicNumber", "MaxLineLength")
package com.finvoraai.personalfinancemanager.finvora.feature.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.viewmodel.koinViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppTextField
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraDatePickerField
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H6TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.VSpacer
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.category_bills
import finvoraai.composeapp.generated.resources.category_food
import finvoraai.composeapp.generated.resources.category_health
import finvoraai.composeapp.generated.resources.category_income
import finvoraai.composeapp.generated.resources.category_other
import finvoraai.composeapp.generated.resources.category_shopping
import finvoraai.composeapp.generated.resources.category_transport
import finvoraai.composeapp.generated.resources.ic_arrow_back
import finvoraai.composeapp.generated.resources.ic_arrow_drop_down
import finvoraai.composeapp.generated.resources.transaction_amount_label
import finvoraai.composeapp.generated.resources.transaction_category_label
import finvoraai.composeapp.generated.resources.transaction_date_label
import finvoraai.composeapp.generated.resources.transaction_date_today
import finvoraai.composeapp.generated.resources.transaction_new_title
import finvoraai.composeapp.generated.resources.transaction_save_btn
import finvoraai.composeapp.generated.resources.transaction_select_category
import finvoraai.composeapp.generated.resources.transaction_title_hint
import finvoraai.composeapp.generated.resources.transaction_title_label
import finvoraai.composeapp.generated.resources.transaction_type_expense
import finvoraai.composeapp.generated.resources.transaction_type_income
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// ---------------------------------------------------------------------------
// Data model — structured for future API replacement without a UI redesign
// ---------------------------------------------------------------------------

data class TransactionCategory(
    val id: String,
    val emoji: String,
    val labelResId: org.jetbrains.compose.resources.StringResource
)

// Mocked locally; swap this list for an API result when backend is ready.
// Res.string.* references are plain StringResource objects — no @Composable needed here.
val defaultCategories: List<TransactionCategory> = listOf(
    TransactionCategory("shopping", "🛒", Res.string.category_shopping),
    TransactionCategory("food", "🍔", Res.string.category_food),
    TransactionCategory("transport", "🚕", Res.string.category_transport),
    TransactionCategory("health", "💊", Res.string.category_health),
    TransactionCategory("bills", "🧾", Res.string.category_bills),
    TransactionCategory("income", "💰", Res.string.category_income),
    TransactionCategory("other", "📦", Res.string.category_other)
)

// ---------------------------------------------------------------------------
// UI State — currencySymbol is a field so it can later come from settings/locale
// ---------------------------------------------------------------------------

data class AddTransactionUiState(
    val amount: String = "",
    val currencySymbol: String = "$",
    val title: String = "",
    val selectedCategory: TransactionCategory? = null,
    // Stored as LocalDate? — formatted to ISO-8601 when saving
    val date: LocalDate? = null,
    val isExpense: Boolean = true,
    val showCategoryPicker: Boolean = false
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: AddTransactionViewModel = koinViewModel()
) {
    val palette = LocalAppPalette.current
    val categories = defaultCategories
    var uiState by remember {
        mutableStateOf(AddTransactionUiState(selectedCategory = categories.first()))
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val submitState by viewModel.submitState.collectAsState()

    LaunchedEffect(submitState.success) {
        if (submitState.success) {
            navController.popBackStack()
        }
    }

    // Category picker bottom sheet
    if (uiState.showCategoryPicker) {
        CategoryPickerBottomSheet(
            categories = categories,
            sheetState = sheetState,
            onCategorySelected = { category ->
                uiState = uiState.copy(
                    selectedCategory = category,
                    showCategoryPicker = false
                )
            },
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    uiState = uiState.copy(showCategoryPicker = false)
                }
            }
        )
    }

    Scaffold(
        containerColor = palette.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.s2, vertical = Spacing.s1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.transaction_new_title),
                        tint = palette.textPrimary
                    )
                }
                Text(
                    text = stringResource(Res.string.transaction_new_title),
                    style = H6TextStyle().copy(fontWeight = FontWeight.SemiBold),
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                // Spacer to optically center the title against the back button
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.s4)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VSpacer(Spacing.s4)

            // ----------------------------------------------------------------
            // Expense / Income segmented control
            // ----------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.s13)
                    .clip(RoundedCornerShape(Spacing.s3))
                    .background(palette.surface),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SegmentButton(
                    label = stringResource(Res.string.transaction_type_expense),
                    isSelected = uiState.isExpense,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { uiState = uiState.copy(isExpense = true) }
                )
                SegmentButton(
                    label = stringResource(Res.string.transaction_type_income),
                    isSelected = !uiState.isExpense,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    onClick = { uiState = uiState.copy(isExpense = false) }
                )
            }

            VSpacer(Spacing.s8)

            // ----------------------------------------------------------------
            // Amount input
            // ----------------------------------------------------------------
            Text(
                text = stringResource(Res.string.transaction_amount_label),
                style = BodyNormal(),
                color = palette.textSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            VSpacer(Spacing.s2)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Currency symbol — not hardcoded; comes from uiState.currencySymbol
                Text(
                    text = uiState.currencySymbol,
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.isExpense) palette.error else palette.success
                    ),
                    modifier = Modifier.padding(end = Spacing.s1)
                )
                BasicTextField(
                    value = uiState.amount,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            uiState = uiState.copy(amount = newValue)
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.textPrimary
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    cursorBrush = SolidColor(palette.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.amount.isEmpty()) {
                                Text(
                                    text = "0.00",
                                    style = TextStyle(
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = palette.textSecondary.copy(alpha = 0.4f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            VSpacer(Spacing.s8)

            // ----------------------------------------------------------------
            // Title field
            // ----------------------------------------------------------------
            AppTextField(
                value = uiState.title,
                onValueChange = { uiState = uiState.copy(title = it) },
                label = stringResource(Res.string.transaction_title_label),
                placeholder = stringResource(Res.string.transaction_title_hint),
                modifier = Modifier.fillMaxWidth()
            )

            VSpacer(Spacing.s4)

            // ----------------------------------------------------------------
            // Category selector
            // ----------------------------------------------------------------
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.transaction_category_label),
                    style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                    color = palette.textPrimary.copy(alpha = 0.9f),
                    modifier = Modifier.padding(bottom = Spacing.s2)
                )
                AppCard(
                    onClick = { uiState = uiState.copy(showCategoryPicker = true) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.s1),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Emoji badge
                            Box(
                                modifier = Modifier
                                    .size(Spacing.s10)
                                    .clip(RoundedCornerShape(Spacing.s2))
                                    .background(palette.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = uiState.selectedCategory?.emoji ?: "📦",
                                    style = H4TextStyle()
                                )
                            }
                            Text(
                                text = uiState.selectedCategory?.let {
                                    stringResource(it.labelResId)
                                } ?: stringResource(Res.string.category_other),
                                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                                color = palette.textPrimary,
                                modifier = Modifier.padding(start = Spacing.s3)
                            )
                        }
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_drop_down),
                            contentDescription = null,
                            tint = palette.textSecondary,
                            modifier = Modifier.size(Spacing.s6)
                        )
                    }
                }
            }

            VSpacer(Spacing.s4)

            // ----------------------------------------------------------------
            // Date selector — backed by FinvoraDatePickerField (reusable)
            // ----------------------------------------------------------------
            FinvoraDatePickerField(
                selectedDate = uiState.date,
                onDateSelected = { uiState = uiState.copy(date = it) },
                label = stringResource(Res.string.transaction_date_label),
                placeholder = stringResource(Res.string.transaction_date_today),
                modifier = Modifier.fillMaxWidth()
            )

            VSpacer(Spacing.s10)

            // ----------------------------------------------------------------
            // Save button
            // ----------------------------------------------------------------
            if (submitState.error != null) {
                Text(
                    text = submitState.error ?: "",
                    style = BodyNormal(),
                    color = palette.error,
                    modifier = Modifier.padding(bottom = Spacing.s2)
                )
            }
            FinvoraButton(
                text = if (submitState.isLoading) "Saving..." else stringResource(Res.string.transaction_save_btn),
                onClick = {
                    // Format LocalDate? to ISO-8601 date string; blank = ViewModel uses Clock.now()
                    val isoDate = uiState.date?.let { d ->
                        "${d.year}-${d.monthNumber.toString().padStart(2, '0')}-${d.dayOfMonth.toString().padStart(2, '0')}"
                    } ?: ""
                    viewModel.saveTransaction(
                        isExpense = uiState.isExpense,
                        amount = uiState.amount,
                        title = uiState.title,
                        category = uiState.selectedCategory?.id ?: "other",
                        emoji = uiState.selectedCategory?.emoji ?: "📦",
                        date = isoDate
                    )
                },
                enabled = !submitState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            VSpacer(Spacing.s6)
        }
    }
}

// ---------------------------------------------------------------------------
// Segment button used in the Expense/Income toggle
// ---------------------------------------------------------------------------

@Composable
private fun SegmentButton(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalAppPalette.current
    val bg = if (isSelected) palette.surfaceVariant else palette.surface
    val textColor = if (isSelected) palette.textPrimary else palette.textSecondary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.s3))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
            color = textColor
        )
    }
}

// ---------------------------------------------------------------------------
// Category picker bottom sheet
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerBottomSheet(
    categories: List<TransactionCategory>,
    sheetState: SheetState,
    onCategorySelected: (TransactionCategory) -> Unit,
    onDismiss: () -> Unit
) {
    val palette = LocalAppPalette.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = palette.surface,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.s4)
                .padding(bottom = Spacing.s6)
        ) {
            Text(
                text = stringResource(Res.string.transaction_select_category),
                style = H6TextStyle().copy(fontWeight = FontWeight.SemiBold),
                color = palette.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.s4),
                textAlign = TextAlign.Center
            )

            categories.forEach { category ->
                CategoryPickerRow(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
                VSpacer(Spacing.s2)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Single row in the category picker
// ---------------------------------------------------------------------------

@Composable
private fun CategoryPickerRow(category: TransactionCategory, onClick: () -> Unit) {
    val palette = LocalAppPalette.current

    AppCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.s1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s3)
        ) {
            // Emoji badge
            Box(
                modifier = Modifier
                    .size(Spacing.s10)
                    .clip(RoundedCornerShape(Spacing.s2))
                    .background(palette.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.emoji,
                    style = H4TextStyle()
                )
            }
            Text(
                text = stringResource(category.labelResId),
                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                color = palette.textPrimary
            )
        }
    }
}
