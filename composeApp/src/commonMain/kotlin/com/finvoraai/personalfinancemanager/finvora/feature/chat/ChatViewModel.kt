package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatPayload
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatRequest
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.PendingActionData
import com.finvoraai.personalfinancemanager.finvora.data.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.getString
import finvoraai.composeapp.generated.resources.*

// ---------------------------------------------------------------------------
// Domain UI models — internal to this feature package
// ---------------------------------------------------------------------------

@Immutable
data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val pendingActions: List<PendingActionUi> = emptyList(),
    val resolvedStatus: Map<String, String> = emptyMap(),
    val streaming: Boolean = false
)

@Immutable
data class PendingActionUi(
    val type: String,
    val title: String,
    val pendingId: String,
    val amount: Double?,
    val emoji: String?,
    val date: String?,
    val category: String?,
    val isAutoLearned: Boolean = false
)

@Immutable
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val visibleSuggestions: List<String> = emptyList(),
    val isHistoryLoading: Boolean = true,
    val isLoading: Boolean = false,
    val isTyping: Boolean = false,
    val activeDraftId: String? = null
)

// ---------------------------------------------------------------------------
// Default fallback suggestions (shown while API loads)
// ---------------------------------------------------------------------------
// We fetch these asynchronously using getString in loadSuggestions() fallback
private suspend fun getDefaultSuggestions(): List<String> = listOf(
    getString(Res.string.chat_suggestion_1),
    getString(Res.string.chat_suggestion_2),
    getString(Res.string.chat_suggestion_3),
    getString(Res.string.chat_suggestion_4),
    getString(Res.string.chat_suggestion_5),
    getString(Res.string.chat_suggestion_6),
    getString(Res.string.chat_suggestion_7),
    getString(Res.string.chat_suggestion_8),
    getString(Res.string.chat_suggestion_9),
    getString(Res.string.chat_suggestion_10),
    getString(Res.string.chat_suggestion_11)
)

private const val TYPING_STEP = 3
private const val TYPING_DELAY_MS = 15L

// ---------------------------------------------------------------------------
// ChatViewModel
// ---------------------------------------------------------------------------

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
        loadSuggestions()
    }

    // ── Suggestions ───────────────────────────────────────────────────────

    private fun loadSuggestions() {
        viewModelScope.launch {
            chatRepository.getSuggestions().collect { result ->
                result.onSuccess { list ->
                    _uiState.update { it.copy(visibleSuggestions = list.shuffled().take(3)) }
                }.onFailure {
                    val defaultSugs = getDefaultSuggestions()
                    _uiState.update { it.copy(visibleSuggestions = defaultSugs.shuffled().take(3)) }
                }
            }
        }
    }

    fun shuffleSuggestions() {
        viewModelScope.launch {
            // Because we only rely on default suggestions when there's an error,
            // we will fetch defaults here and use them for the shuffle pool.
            // Ideally we'd use the successful API ones, but since this is localized we can just use defaults.
            val defaultSugs = getDefaultSuggestions()
            _uiState.update { it.copy(visibleSuggestions = defaultSugs.shuffled().take(3)) }
        }
    }

    // ── History ───────────────────────────────────────────────────────────

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isHistoryLoading = true) }
            chatRepository.getHistory(skip = 0, limit = 10).collect { result ->
                result.onSuccess { response ->
                    val messages = if (response.history.isNotEmpty()) {
                        response.history.map { dto ->
                            ChatMessage(
                                role = dto.role,
                                content = dto.content,
                                timestamp = parseIsoToMillis(dto.createdAt ?: ""),
                                pendingActions = dto.pendingActions?.map { pa ->
                                    PendingActionUi(
                                        type = pa.type,
                                        title = pa.title ?: "",
                                        pendingId = pa.pendingId,
                                        amount = pa.amount,
                                        emoji = pa.emoji,
                                        date = pa.date,
                                        category = pa.category,
                                        isAutoLearned = pa.isAutoLearned
                                    )
                                } ?: emptyList(),
                                resolvedStatus = dto.resolvedStatus ?: emptyMap()
                            )
                        }
                    } else {
                        listOf(buildWelcomeMessage())
                    }
                    _uiState.update {
                        it.copy(
                            messages = messages,
                            isHistoryLoading = false,
                            activeDraftId = response.activePendingIds.firstOrNull()
                        )
                    }
                }.onFailure {
                    _uiState.update {
                        it.copy(
                            messages = listOf(buildWelcomeMessage()),
                            isHistoryLoading = false
                        )
                    }
                }
            }
        }
    }

    // ── Send / Confirm / Cancel ───────────────────────────────────────────

    fun sendMessage(userMessage: String, payload: AiChatPayload? = null) {
        if (userMessage.isBlank() && payload == null) return

        // Optimistic user bubble — skip when it's a confirm/cancel payload
        if (payload == null) {
            _uiState.update { state ->
                val userMsg = ChatMessage(role = "user", content = userMessage)
                state.copy(messages = state.messages + userMsg, isLoading = true)
            }
        } else {
            _uiState.update { it.copy(isLoading = true) }
        }

        val request = AiChatRequest(message = userMessage.ifBlank { "" }, payload = payload)

        viewModelScope.launch {
            chatRepository.sendMessage(request).collect { result ->
                result.onSuccess { response ->
                    _uiState.update { it.copy(isLoading = false) }

                    // Typing effect — add empty streaming bubble then fill it
                    streamBotMessage(response.reply)

                    val pendingUi = response.pendingActions?.map { pa ->
                        PendingActionUi(
                            type = pa.type,
                            title = pa.title ?: "",
                            pendingId = pa.pendingId,
                            amount = pa.amount,
                            emoji = pa.emoji,
                            date = pa.date,
                            category = pa.category,
                            isAutoLearned = pa.isAutoLearned
                        )
                    } ?: emptyList()

                    _uiState.update { state ->
                        var msgs = state.messages.toMutableList()
                        val lastIdx = msgs.lastIndex

                        // Attach pending actions to the streamed message
                        if (lastIdx >= 0) {
                            msgs[lastIdx] = msgs[lastIdx].copy(
                                content = response.reply,
                                pendingActions = pendingUi,
                                streaming = false
                            )
                        }

                        // Supersede matching drafts in older messages
                        if (pendingUi.isNotEmpty()) {
                            val newIds = pendingUi.map { it.pendingId }.toSet()
                            msgs = msgs.mapIndexed { idx, msg ->
                                if (idx == lastIdx || msg.pendingActions.isEmpty()) return@mapIndexed msg
                                val toMark = msg.pendingActions.filter { pa ->
                                    newIds.contains(pa.pendingId) && !msg.resolvedStatus.containsKey(pa.pendingId)
                                }
                                if (toMark.isEmpty()) return@mapIndexed msg
                                val updated = msg.resolvedStatus.toMutableMap()
                                toMark.forEach { pa -> updated[pa.pendingId] = "superseded" }
                                msg.copy(resolvedStatus = updated)
                            }.toMutableList()
                        }

                        // Apply resolution metadata from the API
                        response.metadata?.resolvedStatus?.let { resolutions ->
                            msgs = msgs.map { msg ->
                                if (msg.pendingActions.isEmpty()) return@map msg
                                val updated = msg.resolvedStatus.toMutableMap()
                                var changed = false
                                resolutions.forEach { (id, status) ->
                                    if (
                                        msg.pendingActions.any { pa -> pa.pendingId == id } &&
                                        updated[id] != "superseded"
                                    ) {
                                        updated[id] = status
                                        changed = true
                                    }
                                }
                                if (changed) msg.copy(resolvedStatus = updated) else msg
                            }.toMutableList()
                        }

                        val newActiveDraft = if (pendingUi.isNotEmpty()) pendingUi.last().pendingId else state.activeDraftId
                        state.copy(messages = msgs, isTyping = false, activeDraftId = newActiveDraft)
                    }

                    shuffleSuggestions()
                }.onFailure {
                    _uiState.update { state ->
                        val errMsg = ChatMessage(
                            role = "assistant",
                            content = "Sorry, I had an error connecting to the AI. Please try again."
                        )
                        state.copy(messages = state.messages + errMsg, isLoading = false, isTyping = false)
                    }
                }
            }
        }
    }

    fun confirmAction(msgIndex: Int, action: PendingActionUi) {
        if (_uiState.value.messages.getOrNull(msgIndex)?.resolvedStatus?.containsKey(action.pendingId) == true) return

        // Mark confirmed optimistically
        _uiState.update { state ->
            val msgs = state.messages.toMutableList()
            val actualIdx = msgs.indexOfLast { m -> m.pendingActions.any { pa -> pa.pendingId == action.pendingId } }
            if (actualIdx >= 0) {
                val status = msgs[actualIdx].resolvedStatus.toMutableMap()
                status[action.pendingId] = "confirmed"
                msgs[actualIdx] = msgs[actualIdx].copy(resolvedStatus = status)
            }
            state.copy(
                messages = msgs,
                activeDraftId = if (state.activeDraftId == action.pendingId) null else state.activeDraftId
            )
        }

        sendMessage(
            userMessage = "Confirmed",
            payload = AiChatPayload(
                pendingId = action.pendingId,
                data = PendingActionData(
                    title = action.title,
                    amount = action.amount ?: 0.0,
                    category = action.category,
                    date = action.date,
                    emoji = action.emoji
                )
            )
        )
    }

    fun cancelAction(msgIndex: Int, action: PendingActionUi) {
        if (_uiState.value.messages.getOrNull(msgIndex)?.resolvedStatus?.containsKey(action.pendingId) == true) return

        // Mark cancelled optimistically
        _uiState.update { state ->
            val msgs = state.messages.toMutableList()
            val actualIdx = msgs.indexOfLast { m -> m.pendingActions.any { pa -> pa.pendingId == action.pendingId } }
            if (actualIdx >= 0) {
                val status = msgs[actualIdx].resolvedStatus.toMutableMap()
                status[action.pendingId] = "cancelled"
                msgs[actualIdx] = msgs[actualIdx].copy(resolvedStatus = status)
            }
            state.copy(
                messages = msgs,
                activeDraftId = if (state.activeDraftId == action.pendingId) null else state.activeDraftId
            )
        }

        sendMessage(
            userMessage = "cancel",
            payload = AiChatPayload(pendingId = action.pendingId)
        )
    }

    /** Updates a single pending action field locally (for inline editing). */
    fun updateActionField(msgIndex: Int, paIndex: Int, updated: PendingActionUi) {
        _uiState.update { state ->
            val msgs = state.messages.toMutableList()
            if (msgIndex in msgs.indices) {
                val msg = msgs[msgIndex]
                val actions = msg.pendingActions.toMutableList()
                if (paIndex in actions.indices) {
                    actions[paIndex] = updated
                    msgs[msgIndex] = msg.copy(pendingActions = actions)
                }
            }
            state.copy(messages = msgs)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private suspend fun streamBotMessage(fullReply: String) {
        val baseMsg = ChatMessage(role = "assistant", content = "", streaming = true)
        _uiState.update { it.copy(messages = it.messages + baseMsg, isTyping = true) }

        var i = 0
        while (i <= fullReply.length) {
            val partial = fullReply.take(i)
            _uiState.update { state ->
                val msgs = state.messages.toMutableList()
                val lastIdx = msgs.lastIndex
                if (lastIdx >= 0) msgs[lastIdx] = msgs[lastIdx].copy(content = partial)
                state.copy(messages = msgs)
            }
            delay(TYPING_DELAY_MS)
            i += TYPING_STEP
        }
    }

    private suspend fun buildWelcomeMessage() = ChatMessage(
        role = "assistant",
        content = getString(Res.string.chat_welcome_message)
    )

    private fun parseIsoToMillis(isoString: String): Long {
        return try {
            Instant.parse(isoString).toEpochMilliseconds()
        } catch (e: Exception) {
            Clock.System.now().toEpochMilliseconds()
        }
    }
}
