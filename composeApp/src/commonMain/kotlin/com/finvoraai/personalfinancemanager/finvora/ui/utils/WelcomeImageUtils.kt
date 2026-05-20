package com.finvoraai.personalfinancemanager.finvora.ui.utils

import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.finvora_financial_visualization
import finvoraai.composeapp.generated.resources.finvora_financial_visualization_arrow
import org.jetbrains.compose.resources.DrawableResource

data class WelcomeImage(
    val id: String,
    val resource: DrawableResource
)

object WelcomeImageProvider {
    val images = listOf(
        WelcomeImage(
            id = "visualization_standard",
            resource = Res.drawable.finvora_financial_visualization
        ),
        WelcomeImage(
            id = "visualization_arrow",
            resource = Res.drawable.finvora_financial_visualization_arrow
        )
    )

    fun getRandomImage(): WelcomeImage {
        return images.random()
    }
}
