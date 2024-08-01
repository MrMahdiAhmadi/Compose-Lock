package dev.mahdidroid.compose_lock.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.utils.LockViewModel
import dev.mahdidroid.compose_lock.utils.ThemeConfiguration
import dev.mahdidroid.compose_lock.utils.toCustomColor
import androidx.compose.material.MaterialTheme as MaterialThemeM2
import androidx.compose.material3.MaterialTheme as MaterialThemeM3

@Composable
internal fun ApplyTheme(
    viewModel: LockViewModel, content: @Composable (CustomColors) -> Unit
) {
    val theme = viewModel.theme.collectAsState()

    when (theme.value) {
        is ThemeConfiguration.Material2Config -> {
            MaterialThemeM2(
                colors = (theme.value as ThemeConfiguration.Material2Config).colorScheme,
                typography = (theme.value as ThemeConfiguration.Material2Config).typography,
                shapes = (theme.value as ThemeConfiguration.Material2Config).shapes
            ) {
                content((theme.value as ThemeConfiguration.Material2Config).colorScheme.toCustomColor())
            }
        }

        is ThemeConfiguration.Material3Config -> {
            MaterialThemeM3(
                colorScheme = (theme.value as ThemeConfiguration.Material3Config).colorScheme,
                typography = (theme.value as ThemeConfiguration.Material3Config).typography,
                shapes = (theme.value as ThemeConfiguration.Material3Config).shapes
            ) {
                Log.i(
                    "TAG",
                    "ApplyTheme: ${(theme.value as ThemeConfiguration.Material3Config).colorScheme.primary}"
                )
                content((theme.value as ThemeConfiguration.Material3Config).colorScheme.toCustomColor())
            }
        }
    }
}

data class CustomColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val surfaceBright: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val surfaceDim: Color,
)