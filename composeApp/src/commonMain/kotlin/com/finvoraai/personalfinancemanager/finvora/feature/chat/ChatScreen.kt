package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyXSmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H6TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.formatChatDateLabel
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.formatChatTime
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.cd_back
import finvoraai.composeapp.generated.resources.chat_action_amount_label
import finvoraai.composeapp.generated.resources.chat_action_auto_mapped_label
import finvoraai.composeapp.generated.resources.chat_action_cancelled_label
import finvoraai.composeapp.generated.resources.chat_action_category_label
import finvoraai.composeapp.generated.resources.chat_action_date_label
import finvoraai.composeapp.generated.resources.chat_action_expired_label
import finvoraai.composeapp.generated.resources.chat_action_editing
import finvoraai.composeapp.generated.resources.chat_action_edit
import finvoraai.composeapp.generated.resources.chat_action_save_edits
import finvoraai.composeapp.generated.resources.chat_action_expense_badge
import finvoraai.composeapp.generated.resources.chat_action_income_badge
import finvoraai.composeapp.generated.resources.chat_action_review_label
import finvoraai.composeapp.generated.resources.chat_action_saved
import finvoraai.composeapp.generated.resources.chat_action_success_label
import finvoraai.composeapp.generated.resources.chat_action_superseded_label
import finvoraai.composeapp.generated.resources.chat_bot_cd
import finvoraai.composeapp.generated.resources.chat_cancel_btn
import finvoraai.composeapp.generated.resources.chat_confirm_btn
import finvoraai.composeapp.generated.resources.chat_draft_updated_label
import finvoraai.composeapp.generated.resources.chat_header_model
import finvoraai.composeapp.generated.resources.chat_input_placeholder
import finvoraai.composeapp.generated.resources.chat_loading_history
import finvoraai.composeapp.generated.resources.chat_model_active_label
import finvoraai.composeapp.generated.resources.chat_processing_status
import finvoraai.composeapp.generated.resources.chat_send_cd
import finvoraai.composeapp.generated.resources.chat_typing_indicator
import finvoraai.composeapp.generated.resources.ic_arrow_back
import finvoraai.composeapp.generated.resources.ic_arrow_forward
import finvoraai.composeapp.generated.resources.ic_arrow_up_right
import finvoraai.composeapp.generated.resources.ic_chat
import finvoraai.composeapp.generated.resources.ic_bot
import finvoraai.composeapp.generated.resources.ic_info
import finvoraai.composeapp.generated.resources.chat_input_placeholder_pending
import finvoraai.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// ---------------------------------------------------------------------------
// ChatScreen — AI Finance Intelligence
// ---------------------------------------------------------------------------

@Composable
fun ChatScreen(
    @Suppress("UnusedParameter") navController: NavController,
    viewModel: ChatViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()
    var inputText by remember { mutableStateOf("") }
    var showGuide by remember { mutableStateOf(false) }

    val hasUnresolvedAction = uiState.messages.any { msg ->
        msg.pendingActions.any { pa -> !msg.resolvedStatus.containsKey(pa.pendingId) }
    }

    if (showGuide) {
        ChatGuideSheet(onDismiss = { showGuide = false })
    }

    AppBackgroundScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
        ) {

            ChatHeader(onBackClick = { navController.popBackStack() })

            Box(modifier = Modifier.weight(1f)) {
                ChatMessageList(
                    messages = uiState.messages,
                    isHistoryLoading = uiState.isHistoryLoading,
                    activeDraftId = uiState.activeDraftId,
                    onConfirm = { msgIdx, action -> viewModel.confirmAction(msgIdx, action) },
                    onCancel = { msgIdx, action -> viewModel.cancelAction(msgIdx, action) },
                    onUpdateAction = { msgIdx, paIdx, updated -> viewModel.updateActionField(msgIdx, paIdx, updated) }
                )

                // Typing indicator overlay at the bottom of the message area
                this@Column.AnimatedVisibility(
                    visible = uiState.isTyping,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = Spacing.s4, vertical = Spacing.s2)
                    ) {
                        TypingIndicatorRow()
                    }
                }
            }

            // Suggestion chips
            if (uiState.visibleSuggestions.isNotEmpty()) {
                SuggestionChipsRow(
                    suggestions = uiState.visibleSuggestions,
                    onSuggestionClick = { suggestion ->
                        inputText = suggestion
                        viewModel.sendMessage(suggestion)
                    }
                )
            }

            // Input bar
            ChatInputBar(
                value = inputText,
                onValueChange = { inputText = it },
                isLoading = uiState.isLoading,
                hasUnresolvedAction = hasUnresolvedAction,
                onSend = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    }
                },
                onInfoClick = { showGuide = true }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------

@Composable
private fun ChatHeader(onBackClick: () -> Unit) {
    val palette = LocalAppPalette.current
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
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = stringResource(Res.string.cd_back),
                tint = palette.textPrimary
            )
        }

        Box(
            modifier = Modifier
                .size(Spacing.s10)
                .clip(RoundedCornerShape(Spacing.s3))
                .background(palette.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_bot),
                contentDescription = stringResource(Res.string.chat_bot_cd),
                tint = palette.primary,
                modifier = Modifier.size(Spacing.s5)
            )
        }

        Spacer(modifier = Modifier.width(Spacing.s3))

        Column {
            Text(
                text = stringResource(Res.string.chat_header_model),
                style = H6TextStyle().copy(fontWeight = FontWeight.Black),
                color = palette.textPrimary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sHalf)
            ) {
                // Active pulse dot
                Box(
                    modifier = Modifier
                        .size(Spacing.s1 + Spacing.sHalf)
                        .clip(CircleShape)
                        .background(palette.success)
                )
                Text(
                    text = stringResource(Res.string.chat_model_active_label),
                    style = BodyXSmall().copy(fontWeight = FontWeight.Bold),
                    color = palette.textSecondary
                )
            }
        }
    }
}


// ---------------------------------------------------------------------------
// Message List
// ---------------------------------------------------------------------------

@Composable
private fun ChatMessageList(
    messages: List<ChatMessage>,
    isHistoryLoading: Boolean,
    activeDraftId: String?,
    onConfirm: (Int, PendingActionUi) -> Unit,
    onCancel: (Int, PendingActionUi) -> Unit,
    onUpdateAction: (Int, Int, PendingActionUi) -> Unit
) {
    val palette = LocalAppPalette.current
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when messages update
    LaunchedEffect(messages.size, messages.lastOrNull()?.content) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    if (isHistoryLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                CircularProgressIndicator(color = palette.primary, strokeWidth = Spacing.sHalf)
                Text(
                    text = stringResource(Res.string.chat_loading_history),
                    style = BodySmall(),
                    color = palette.textSecondary
                )
            }
        }
        return
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = Spacing.s4,
            bottom = Spacing.s4,
            start = Spacing.s4,
            end = Spacing.s4
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.s3)
    ) {
        itemsIndexed(messages) { index, message ->
            // Date separator between messages on different days
            val showDateSeparator = index == 0 ||
                messages[index - 1].timestamp.formatChatDateLabel() != message.timestamp.formatChatDateLabel()

            if (showDateSeparator) {
                ChatDateSeparator(label = message.timestamp.formatChatDateLabel())
                Spacer(modifier = Modifier.height(Spacing.s2))
            }

            if (message.role == "user") {
                UserMessageBubble(message = message)
            } else {
                AssistantMessageBubble(
                    message = message,
                    msgIndex = index,
                    activeDraftId = activeDraftId,
                    onConfirm = onConfirm,
                    onCancel = onCancel,
                    onUpdateAction = onUpdateAction
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Date Separator
// ---------------------------------------------------------------------------

@Composable
private fun ChatDateSeparator(label: String) {
    val palette = LocalAppPalette.current
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = label,
            style = BodyXSmall().copy(fontWeight = FontWeight.SemiBold),
            color = palette.textTertiary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(Spacing.s4))
                .background(palette.surfaceVariant)
                .padding(horizontal = Spacing.s3, vertical = Spacing.sHalf)
        )
    }
}

// ---------------------------------------------------------------------------
// Message Bubbles
// ---------------------------------------------------------------------------

@Composable
private fun UserMessageBubble(message: ChatMessage) {
    val palette = LocalAppPalette.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = Spacing.authMaxWidth * 0.75f)
                .clip(
                    RoundedCornerShape(
                        topStart = Spacing.s4,
                        topEnd = Spacing.s4,
                        bottomStart = Spacing.s4,
                        bottomEnd = Spacing.s1
                    )
                )
                .background(palette.primary)
                .padding(horizontal = Spacing.s4, vertical = Spacing.s3)
        ) {
            Text(
                text = message.content,
                style = BodyNormal(),
                color = palette.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(Spacing.sHalf))
        Text(
            text = message.timestamp.formatChatTime(),
            style = BodyXSmall(),
            color = palette.textTertiary
        )
    }
}

@Composable
private fun AssistantMessageBubble(
    message: ChatMessage,
    msgIndex: Int,
    activeDraftId: String?,
    onConfirm: (Int, PendingActionUi) -> Unit,
    onCancel: (Int, PendingActionUi) -> Unit,
    onUpdateAction: (Int, Int, PendingActionUi) -> Unit
) {
    val palette = LocalAppPalette.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s2)
        ) {
            // Bot icon
            Box(
                modifier = Modifier
                    .size(Spacing.s8)
                    .clip(CircleShape)
                    .background(palette.primary.copy(alpha = 0.12f))
                    .border(Spacing.hairline, palette.primary.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bot),
                    contentDescription = null,
                    tint = palette.primary,
                    modifier = Modifier.size(Spacing.s4)
                )
            }

            Column(modifier = Modifier.widthIn(max = Spacing.authMaxWidth * 0.78f)) {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = Spacing.s1,
                                topEnd = Spacing.s4,
                                bottomStart = Spacing.s4,
                                bottomEnd = Spacing.s4
                            )
                        )
                        .background(palette.surface)
                        .border(Spacing.hairline, palette.outline.copy(alpha = 0.3f), RoundedCornerShape(
                            topStart = Spacing.s1,
                            topEnd = Spacing.s4,
                            bottomStart = Spacing.s4,
                            bottomEnd = Spacing.s4
                        ))
                        .padding(horizontal = Spacing.s4, vertical = Spacing.s3)
                ) {
                    Text(
                        text = message.content,
                        style = BodyNormal(),
                        color = palette.textPrimary
                    )
                }

                // Draft cards for any pending actions
                message.pendingActions.forEachIndexed { paIndex, action ->
                    val status = message.resolvedStatus[action.pendingId]
                    val isActive = action.pendingId == activeDraftId && status == null
                    Spacer(modifier = Modifier.height(Spacing.s2))
                    DraftCard(
                        action = action,
                        paIndex = paIndex,
                        msgIndex = msgIndex,
                        status = status,
                        isActive = isActive,
                        activeDraftId = activeDraftId,
                        onConfirm = onConfirm,
                        onCancel = onCancel,
                        onUpdateAction = onUpdateAction
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.sHalf))
        Text(
            text = message.timestamp.formatChatTime(),
            style = BodyXSmall(),
            color = palette.textTertiary,
            modifier = Modifier.padding(start = Spacing.s8 + Spacing.s2)
        )
    }
}

// ---------------------------------------------------------------------------
// Draft Card (Pending Action Confirmation)
// ---------------------------------------------------------------------------

@Composable
private fun DraftCard(
    action: PendingActionUi,
    paIndex: Int,
    msgIndex: Int,
    status: String?,
    isActive: Boolean,
    activeDraftId: String?,
    onConfirm: (Int, PendingActionUi) -> Unit,
    onCancel: (Int, PendingActionUi) -> Unit,
    onUpdateAction: (Int, Int, PendingActionUi) -> Unit
) {
    val palette = LocalAppPalette.current
    val isExpense = action.type == "expense"
    val badgeColor = if (isExpense) palette.warning else palette.success
    val isResolved = status != null
    val isConfirmed = status == "confirmed"
    val isCancelled = status == "cancelled"
    val isExpired = !isActive && !isResolved

    // Editing state — local to this card
    var isEditing by remember(action.pendingId) { mutableStateOf(false) }
    var editedTitle by remember(action.pendingId) { mutableStateOf(action.title) }
    var editedAmount by remember(action.pendingId) { mutableStateOf(action.amount?.toString() ?: "") }

    val cardAlpha = if (isExpired || (isResolved && !isConfirmed)) 0.55f else 1f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.s4))
            .background(palette.surface.copy(alpha = cardAlpha))
            .border(
                width = if (isActive) Spacing.hairline + Spacing.hairline else Spacing.hairline,
                color = if (isActive) palette.primary.copy(alpha = 0.5f) else palette.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(Spacing.s4)
            )
            .padding(Spacing.s4)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {

            // Badge + status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusLabel = when {
                    isConfirmed -> stringResource(Res.string.chat_action_saved)
                    isCancelled -> stringResource(Res.string.chat_action_cancelled_label)
                    status == "superseded" -> {
                        if (action.pendingId == activeDraftId)
                            stringResource(Res.string.chat_draft_updated_label)
                        else
                            stringResource(Res.string.chat_action_superseded_label)
                    }
                    isExpired -> stringResource(Res.string.chat_action_expired_label)
                    isEditing -> stringResource(Res.string.chat_action_editing)
                    else -> stringResource(Res.string.chat_action_review_label)
                }
                Text(
                    text = statusLabel,
                    style = BodyXSmall().copy(fontWeight = FontWeight.Bold),
                    color = if (isConfirmed) palette.success else palette.textTertiary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Spacing.s4))
                        .background(badgeColor.copy(alpha = 0.12f))
                        .padding(horizontal = Spacing.s2, vertical = Spacing.sHalf)
                ) {
                    Text(
                        text = if (isExpense) stringResource(Res.string.chat_action_expense_badge)
                               else stringResource(Res.string.chat_action_income_badge),
                        style = BodyXSmall().copy(fontWeight = FontWeight.Black),
                        color = badgeColor
                    )
                }
            }

            // Emoji + title row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s2)
            ) {
                Text(text = action.emoji ?: "✨", style = H6TextStyle())
                Column {
                    if (isEditing) {
                        BasicTextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            textStyle = BodyNormal().copy(color = palette.textPrimary, fontWeight = FontWeight.Bold),
                            cursorBrush = SolidColor(palette.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Spacing.s2))
                                .background(palette.surfaceVariant)
                                .padding(horizontal = Spacing.s3, vertical = Spacing.s2)
                        )
                    } else {
                        Text(
                            text = action.title,
                            style = BodyNormal().copy(fontWeight = FontWeight.Bold),
                            color = if (isResolved || isExpired) palette.textTertiary else palette.textPrimary
                        )
                    }
                    if (action.isAutoLearned) {
                        Text(
                            text = stringResource(Res.string.chat_action_auto_mapped_label),
                            style = BodyXSmall(),
                            color = palette.primary
                        )
                    }
                }
            }

            // Amount + Date row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Spacing.s3))
                    .background(badgeColor.copy(alpha = 0.08f))
                    .padding(horizontal = Spacing.s3, vertical = Spacing.s3),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.chat_action_amount_label),
                        style = BodyXSmall().copy(fontWeight = FontWeight.Bold),
                        color = badgeColor.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(Spacing.sHalf))
                    if (isEditing) {
                        BasicTextField(
                            value = editedAmount,
                            onValueChange = { editedAmount = it },
                            textStyle = H6TextStyle().copy(color = badgeColor, fontWeight = FontWeight.Black),
                            cursorBrush = SolidColor(palette.primary),
                            modifier = Modifier
                                .width(Spacing.s20)
                                .clip(RoundedCornerShape(Spacing.s2))
                                .background(palette.surface)
                                .padding(horizontal = Spacing.s2, vertical = Spacing.s1)
                        )
                    } else {
                        Text(
                            text = "$${action.amount ?: "??"}",
                            style = H6TextStyle().copy(fontWeight = FontWeight.Black),
                            color = badgeColor
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(Res.string.chat_action_date_label),
                        style = BodyXSmall().copy(fontWeight = FontWeight.Bold),
                        color = palette.textTertiary
                    )
                    Spacer(modifier = Modifier.height(Spacing.sHalf))
                    Text(
                        text = action.date?.take(10) ?: "Today",
                        style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                        color = palette.textSecondary
                    )
                }
            }

            // Action buttons
            if (!isResolved && !isExpired) {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                    if (isEditing) {
                        // Confirm edits
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .clip(RoundedCornerShape(Spacing.s3))
                                .background(palette.primary)
                                .clickable {
                                    val updatedAmount = editedAmount.toDoubleOrNull() ?: action.amount
                                    val updated = action.copy(title = editedTitle, amount = updatedAmount)
                                    onUpdateAction(msgIndex, paIndex, updated)
                                    isEditing = false
                                }
                                .padding(vertical = Spacing.s3),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_action_save_edits),
                                style = BodySmall().copy(fontWeight = FontWeight.Bold),
                                color = palette.onPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(Spacing.s3))
                                .background(palette.surfaceVariant)
                                .clickable {
                                    editedTitle = action.title
                                    editedAmount = action.amount?.toString() ?: ""
                                    isEditing = false
                                }
                                .padding(vertical = Spacing.s3),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_cancel_btn),
                                style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                                color = palette.textSecondary
                            )
                        }
                    } else {
                        // Confirm
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .clip(RoundedCornerShape(Spacing.s3))
                                .background(palette.primary)
                                .clickable { onConfirm(msgIndex, action) }
                                .padding(vertical = Spacing.s3),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_confirm_btn),
                                style = BodySmall().copy(fontWeight = FontWeight.Bold),
                                color = palette.onPrimary
                            )
                        }
                        // Edit
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(Spacing.s3))
                                .border(Spacing.hairline, palette.outline.copy(alpha = 0.4f), RoundedCornerShape(Spacing.s3))
                                .clickable { isEditing = true }
                                .padding(vertical = Spacing.s3),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_action_edit),
                                style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                                color = palette.textSecondary
                            )
                        }
                        // Cancel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(Spacing.s3))
                                .clickable { onCancel(msgIndex, action) }
                                .padding(vertical = Spacing.s3),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.chat_cancel_btn),
                                style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                                color = palette.error.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else if (isConfirmed) {
                Text(
                    text = stringResource(Res.string.chat_action_success_label),
                    style = BodySmall().copy(fontWeight = FontWeight.Bold),
                    color = palette.success
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Typing Indicator
// ---------------------------------------------------------------------------

@Composable
private fun TypingIndicatorRow() {
    val palette = LocalAppPalette.current
    val transition = rememberInfiniteTransition(label = "typing_dots")
    val alpha1 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "dot1"
    )
    val alpha2 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, 150, LinearEasing), RepeatMode.Reverse),
        label = "dot2"
    )
    val alpha3 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, 300, LinearEasing), RepeatMode.Reverse),
        label = "dot3"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.s4))
            .background(palette.surface)
            .border(Spacing.hairline, palette.outline.copy(alpha = 0.3f), RoundedCornerShape(Spacing.s4))
            .padding(horizontal = Spacing.s4, vertical = Spacing.s2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s1)
    ) {
        listOf(alpha1, alpha2, alpha3).forEach { alpha ->
            Box(
                modifier = Modifier
                    .size(Spacing.s2)
                    .clip(CircleShape)
                    .background(palette.primary.copy(alpha = alpha))
            )
        }
        Spacer(modifier = Modifier.width(Spacing.s2))
        Text(
            text = stringResource(Res.string.chat_typing_indicator),
            style = BodySmall().copy(fontWeight = FontWeight.Medium),
            color = palette.textSecondary
        )
    }
}

// ---------------------------------------------------------------------------
// Suggestion Chips
// ---------------------------------------------------------------------------

@Composable
private fun SuggestionChipsRow(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    val palette = LocalAppPalette.current
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.surface.copy(alpha = 0.5f))
            .padding(horizontal = Spacing.s4, vertical = Spacing.s2),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s2)
    ) {
        items(suggestions) { suggestion ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Spacing.s4))
                    .background(palette.surfaceVariant)
                    .border(Spacing.hairline, palette.outline.copy(alpha = 0.4f), RoundedCornerShape(Spacing.s4))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSuggestionClick(suggestion) }
                    .padding(horizontal = Spacing.s3, vertical = Spacing.s2)
            ) {
                Text(
                    text = suggestion,
                    style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                    color = palette.textPrimary,
                    maxLines = 1
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Input Bar
// ---------------------------------------------------------------------------

@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    hasUnresolvedAction: Boolean,
    onSend: () -> Unit,
    onInfoClick: () -> Unit
) {
    val palette = LocalAppPalette.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.surface)
            .border(width = Spacing.hairline, color = palette.outline.copy(alpha = 0.3f))
            .padding(horizontal = Spacing.s4, vertical = Spacing.s3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s3)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(Spacing.s4))
                .background(palette.surfaceVariant)
                .border(Spacing.hairline, palette.outline.copy(alpha = 0.3f), RoundedCornerShape(Spacing.s4))
                .padding(horizontal = Spacing.s4, vertical = Spacing.s3)
        ) {
            if (isLoading) {
                Text(
                    text = stringResource(Res.string.chat_processing_status),
                    style = BodyNormal(),
                    color = palette.textTertiary
                )
            } else if (value.isEmpty()) {
                val phText = if (hasUnresolvedAction) {
                    stringResource(Res.string.chat_input_placeholder_pending)
                } else {
                    stringResource(Res.string.chat_input_placeholder)
                }
                Text(
                    text = phText,
                    style = BodyNormal(),
                    color = palette.textTertiary
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = BodyNormal().copy(color = palette.textPrimary),
                cursorBrush = SolidColor(palette.primary),
                modifier = Modifier.fillMaxWidth().padding(end = Spacing.s6),
                enabled = !isLoading
            )
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier.align(Alignment.CenterEnd).size(Spacing.s5)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_info),
                    contentDescription = stringResource(Res.string.chat_guide_cd),
                    tint = palette.textTertiary
                )
            }
        }

        // Send button
        Box(
            modifier = Modifier
                .size(Spacing.s13)
                .clip(RoundedCornerShape(Spacing.s3))
                .background(if (value.isNotBlank() && !isLoading) palette.primary else palette.outline.copy(alpha = 0.3f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = value.isNotBlank() && !isLoading
                ) { onSend() },
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = palette.onPrimary,
                    modifier = Modifier.size(Spacing.s5),
                    strokeWidth = Spacing.sHalf
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = stringResource(Res.string.chat_send_cd),
                    tint = palette.onPrimary,
                    modifier = Modifier.size(Spacing.s5)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Guide Bottom Sheet
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatGuideSheet(onDismiss: () -> Unit) {
    val palette = LocalAppPalette.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = palette.surface,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = Spacing.s4, topEnd = Spacing.s4)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.s4)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(Spacing.s4)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.s2)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_star), // Fallback for sparkles
                    contentDescription = null,
                    tint = palette.primary,
                    modifier = Modifier.size(Spacing.s5)
                )
                Text(
                    text = stringResource(Res.string.chat_guide_title),
                    style = H4TextStyle().copy(fontWeight = FontWeight.Bold),
                    color = palette.textPrimary
                )
            }
            Text(
                text = stringResource(Res.string.chat_guide_subtitle),
                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                color = palette.textSecondary
            )

            Spacer(modifier = Modifier.height(Spacing.s2))

            // Track Section
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                Text(
                    text = stringResource(Res.string.chat_guide_section_track),
                    style = BodySmall().copy(fontWeight = FontWeight.Black),
                    color = palette.textTertiary
                )
                Text(
                    text = stringResource(Res.string.chat_guide_section_track_desc),
                    style = BodySmall(),
                    color = palette.textSecondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s1)) {
                    val trackExamples = listOf(
                        Res.string.chat_guide_ex_track_1,
                        Res.string.chat_guide_ex_track_2,
                        Res.string.chat_guide_ex_track_3,
                        Res.string.chat_guide_ex_track_4,
                        Res.string.chat_guide_ex_track_5
                    )
                    trackExamples.forEach { resId ->
                        Text(
                            text = "\"${stringResource(resId)}\"",
                            style = BodyXSmall().copy(fontWeight = FontWeight.Medium),
                            color = palette.success, // similar to emerald-800
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(palette.success.copy(alpha = 0.1f), RoundedCornerShape(Spacing.s2))
                                .border(Spacing.hairline, palette.success.copy(alpha = 0.2f), RoundedCornerShape(Spacing.s2))
                                .padding(Spacing.s2)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.s1))

            // Check Section
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                Text(
                    text = stringResource(Res.string.chat_guide_section_check),
                    style = BodySmall().copy(fontWeight = FontWeight.Black),
                    color = palette.textTertiary
                )
                Text(
                    text = stringResource(Res.string.chat_guide_section_check_desc),
                    style = BodySmall(),
                    color = palette.textSecondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s1)) {
                    val checkExamples = listOf(
                        Res.string.chat_guide_ex_check_1,
                        Res.string.chat_guide_ex_check_2,
                        Res.string.chat_guide_ex_check_3,
                        Res.string.chat_guide_ex_check_4,
                        Res.string.chat_guide_ex_check_5
                    )
                    checkExamples.forEach { resId ->
                        Text(
                            text = "\"${stringResource(resId)}\"",
                            style = BodyXSmall().copy(fontWeight = FontWeight.Medium),
                            color = palette.primary, // similar to blue-800
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(palette.primary.copy(alpha = 0.1f), RoundedCornerShape(Spacing.s2))
                                .border(Spacing.hairline, palette.primary.copy(alpha = 0.2f), RoundedCornerShape(Spacing.s2))
                                .padding(Spacing.s2)
                        )
                    }
                }
            }

            // Note Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.warning.copy(alpha = 0.1f), RoundedCornerShape(Spacing.s3))
                    .border(Spacing.hairline, palette.warning.copy(alpha = 0.3f), RoundedCornerShape(Spacing.s3))
                    .padding(Spacing.s3),
                verticalArrangement = Arrangement.spacedBy(Spacing.s1)
            ) {
                Text(
                    text = stringResource(Res.string.chat_guide_note_title),
                    style = BodySmall().copy(fontWeight = FontWeight.Black),
                    color = palette.warning
                )
                Text(
                    text = stringResource(Res.string.chat_guide_note_body),
                    style = BodyXSmall().copy(fontWeight = FontWeight.Medium),
                    color = palette.warning
                )
            }

            // Date Tip
            Text(
                text = "✨ \"${stringResource(Res.string.chat_guide_tip)}\"",
                style = BodyXSmall().copy(fontWeight = FontWeight.Medium),
                color = palette.textSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.surfaceVariant, RoundedCornerShape(Spacing.s3))
                    .padding(Spacing.s3)
            )
        }
    }
}

