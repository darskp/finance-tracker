package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
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
import finvoraai.composeapp.generated.resources.transaction_select_category
import org.jetbrains.compose.resources.stringResource

data class TransactionCategory(
    val id: String,
    val labelResId: org.jetbrains.compose.resources.StringResource
)

val defaultCategories: List<TransactionCategory> = listOf(
    TransactionCategory("shopping", Res.string.category_shopping),
    TransactionCategory("food", Res.string.category_food),
    TransactionCategory("transport", Res.string.category_transport),
    TransactionCategory("health", Res.string.category_health),
    TransactionCategory("bills", Res.string.category_bills),
    TransactionCategory("income", Res.string.category_income),
    TransactionCategory("other", Res.string.category_other)
)

data class EmojiSection(val label: String, val emojis: List<String>)

val emojiSections: List<EmojiSection> = listOf(
    EmojiSection("💰 Money & Finance", listOf(
        "💰", "💵", "💴", "💶", "💷", "💸", "💳", "🏦", "🪙", "💹",
        "📈", "📉", "🤑", "💎", "🏧", "💼", "🧾", "🏷️", "🎰", "🪄"
    )),
    EmojiSection("🛒 Shopping", listOf(
        "🛒", "🛍️", "👗", "👟", "👠", "👜", "💍", "💄", "🧴", "🪒",
        "🧹", "🧺", "📦", "🎁", "🪑", "🛋️", "🖥️", "📱", "⌚", "🎮"
    )),
    EmojiSection("🍔 Food & Drinks", listOf(
        "🍔", "🍕", "🍣", "🍜", "🍱", "🌮", "🥗", "🍰", "🧃", "☕",
        "🍺", "🍷", "🍦", "🍩", "🥐", "🍳", "🥩", "🥦", "🍎", "🍌"
    )),
    EmojiSection("🚕 Transport", listOf(
        "🚕", "🚗", "🚌", "🚇", "🚂", "✈️", "🚀", "🛳️", "🚲", "🛵",
        "🏍️", "⛽", "🚦", "🅿️", "🗺️", "🧳", "🎟️", "🛺", "🚁", "🛸"
    )),
    EmojiSection("💊 Health & Fitness", listOf(
        "💊", "🏥", "🩺", "🩹", "💉", "🧬", "🏋️", "🧘", "🚴", "🤸",
        "🍏", "🥕", "💧", "😴", "🧠", "👁️", "🦷", "🦺", "🩻", "🌡️"
    )),
    EmojiSection("🏠 Home & Bills", listOf(
        "🏠", "🏡", "🔑", "💡", "🔌", "📡", "🛁", "🚿", "🧽", "🪴",
        "🌊", "🔥", "❄️", "🌬️", "📺", "📻", "☎️", "🔧", "🪛", "🛠️"
    )),
    EmojiSection("📚 Education & Work", listOf(
        "📚", "✏️", "📖", "🎓", "🏫", "💻", "🖨️", "📋", "📌", "📎",
        "🗂️", "📊", "📝", "🖊️", "🔬", "🔭", "🎨", "🎭", "🎤", "🎧"
    )),
    EmojiSection("🎉 Entertainment", listOf(
        "🎉", "🎬", "🎵", "🎸", "🎹", "🎲", "♟️", "🎯", "🎳", "🎮",
        "🏆", "🥇", "⚽", "🏀", "🎾", "🏖️", "🏕️", "🎡", "🎢", "🎠"
    )),
    EmojiSection("🌿 Nature & Travel", listOf(
        "🌿", "🌸", "🌻", "🌴", "🏔️", "🌋", "🗼", "🗽", "🏰", "🌅",
        "🌄", "🌠", "🌈", "⛅", "🌊", "🐬", "🦋", "🐝", "🦁", "🐘"
    )),
    EmojiSection("😊 Smileys & Feelings", listOf(
        "😊", "😍", "🥳", "😎", "🤩", "🙏", "👍", "❤️", "🔥", "✨",
        "⭐", "🌟", "💫", "🎊", "🎈", "🥰", "😄", "🤗", "💪", "🫶"
    ))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPickerBottomSheet(
    sheetState: SheetState,
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val palette = LocalAppPalette.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = palette.surface,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Pick an Emoji",
                style = H6TextStyle().copy(fontWeight = FontWeight.SemiBold),
                color = palette.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s4)
                    .padding(bottom = Spacing.s3),
                textAlign = TextAlign.Center
            )

            // Fixed height grid so the sheet has a predictable size
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .padding(horizontal = Spacing.s3),
                verticalArrangement = Arrangement.spacedBy(Spacing.s1),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s1)
            ) {
                emojiSections.forEach { section ->
                    // Section header spans the full grid width
                    item(span = { GridItemSpan(8) }) {
                        Text(
                            text = section.label,
                            style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
                            color = palette.textSecondary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.s2)
                        )
                    }
                    items(section.emojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(Spacing.s2))
                                .clickable { onEmojiSelected(emoji) }
                                .background(palette.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                style = TextStyle(fontSize = 22.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            VSpacer(Spacing.s4)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerBottomSheet(
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

@Composable
private fun CategoryPickerRow(category: TransactionCategory, onClick: () -> Unit) {
    val palette = LocalAppPalette.current

    AppCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.s13)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(category.labelResId),
                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                color = palette.textPrimary
            )
        }
    }
}
