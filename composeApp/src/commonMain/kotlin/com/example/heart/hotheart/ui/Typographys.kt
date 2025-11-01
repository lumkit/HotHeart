package com.example.heart.hotheart.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import hotheart.composeapp.generated.resources.Res
import hotheart.composeapp.generated.resources.SourceHanSerifSC_Regular_small
import org.jetbrains.compose.resources.Font

@Composable
fun fontFamily(): FontFamily = FontFamily(
    Font(Res.font.SourceHanSerifSC_Regular_small)
)

val typography: Typography
    @Composable get() = Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(
            fontFamily = fontFamily()
        ),
        displayMedium = MaterialTheme.typography.displayMedium.copy(
            fontFamily = fontFamily()
        ),
        displaySmall = MaterialTheme.typography.displaySmall.copy(
            fontFamily = fontFamily()
        ),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(
            fontFamily = fontFamily()
        ),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(
            fontFamily = fontFamily()
        ),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(
            fontFamily = fontFamily()
        ),
        titleLarge = MaterialTheme.typography.titleLarge.copy(
            fontFamily = fontFamily()
        ),
        titleMedium = MaterialTheme.typography.titleMedium.copy(
            fontFamily = fontFamily()
        ),
        titleSmall = MaterialTheme.typography.titleSmall.copy(
            fontFamily = fontFamily()
        ),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(
            fontFamily = fontFamily()
        ),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(
            fontFamily = fontFamily()
        ),
        bodySmall = MaterialTheme.typography.bodySmall.copy(
            fontFamily = fontFamily()
        ),
        labelLarge = MaterialTheme.typography.labelLarge.copy(
            fontFamily = fontFamily()
        ),
        labelMedium = MaterialTheme.typography.labelMedium.copy(
            fontFamily = fontFamily()
        ),
        labelSmall = MaterialTheme.typography.labelSmall.copy(
            fontFamily = fontFamily()
        ),
    )