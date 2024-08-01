package dev.mahdidroid.compose_lock.utils

import androidx.compose.ui.graphics.Color
import dev.mahdidroid.compose_lock.ui.CustomColors
import androidx.compose.material3.ColorScheme as ColorSchemeM3
import androidx.compose.material.Colors as ColorSchemeM2
import androidx.compose.material3.Shapes as ShapesM3
import androidx.compose.material.Shapes as ShapesM2
import androidx.compose.material3.Typography as TypographyM3
import androidx.compose.material.Typography as TypographyM2

sealed class ThemeConfiguration {
    data class Material3Config(
        val colorScheme: ColorSchemeM3,
        val shapes: ShapesM3 = ShapesM3(),
        val typography: TypographyM3 = TypographyM3()
    ) : ThemeConfiguration()

    data class Material2Config(
        val colorScheme: ColorSchemeM2,
        val shapes: ShapesM2 = ShapesM2(),
        val typography: TypographyM2 = TypographyM2()
    ) : ThemeConfiguration()

    //todo
    /*  data class CustomBuilder(
          val primaryColor: Color,
          val backgroundColor: Color
      ) : ThemeBuilder()*/
}

fun ColorSchemeM3.toCustomColor(): CustomColors = CustomColors(
    primary = this.primary,
    onPrimary = this.onPrimary,
    primaryContainer = this.primaryContainer,
    onPrimaryContainer = this.onPrimaryContainer,
    inversePrimary = this.inversePrimary,
    secondary = this.secondary,
    onSecondary = this.onSecondary,
    secondaryContainer = this.secondaryContainer,
    onSecondaryContainer = this.onSecondaryContainer,
    tertiary = this.tertiary,
    onTertiary = this.onTertiary,
    tertiaryContainer = this.tertiaryContainer,
    onTertiaryContainer = this.onTertiaryContainer,
    background = this.background,
    onBackground = this.onBackground,
    surface = this.surface,
    onSurface = this.onSurface,
    surfaceVariant = this.surfaceVariant,
    onSurfaceVariant = this.onSurfaceVariant,
    surfaceTint = this.primary,  // Assuming surfaceTint should be primary color
    inverseSurface = this.inverseSurface,
    inverseOnSurface = this.inverseOnSurface,
    error = this.error,
    onError = this.onError,
    errorContainer = this.errorContainer,
    onErrorContainer = this.onErrorContainer,
    outline = this.outline,
    outlineVariant = this.outlineVariant,
    scrim = this.scrim,
    surfaceBright = this.surfaceBright,
    surfaceContainer = this.surfaceContainer,
    surfaceContainerHigh = this.surfaceContainerHigh,
    surfaceContainerHighest = this.surfaceContainerHighest,
    surfaceContainerLow = this.surfaceContainerLow,
    surfaceContainerLowest = this.surfaceContainerLowest,
    surfaceDim = this.surfaceDim
)

fun ColorSchemeM2.toCustomColor(): CustomColors = CustomColors(
    primary = this.primary,
    onPrimary = this.onPrimary,
    primaryContainer = this.primaryVariant,  // Assuming primaryVariant maps to primaryContainer
    onPrimaryContainer = this.onPrimary,  // Assuming onPrimary maps to onPrimaryContainer
    inversePrimary = this.primary,  // Assuming inversePrimary is similar to primary
    secondary = this.secondary,
    onSecondary = this.onSecondary,
    secondaryContainer = this.secondaryVariant,  // Assuming secondaryVariant maps to secondaryContainer
    onSecondaryContainer = this.onSecondary,  // Assuming onSecondary maps to onSecondaryContainer
    tertiary = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    onTertiary = Color.Transparent,
    tertiaryContainer = Color.Transparent,
    onTertiaryContainer = Color.Transparent,
    background = this.background,
    onBackground = this.onBackground,
    surface = this.surface,
    onSurface = this.onSurface,
    surfaceVariant = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    onSurfaceVariant = Color.Transparent,
    surfaceTint = this.primary,  // Assuming surfaceTint should be primary color
    inverseSurface = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    inverseOnSurface = Color.Transparent,
    error = this.error,
    onError = this.onError,
    errorContainer = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    onErrorContainer = Color.Transparent,
    outline = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    outlineVariant = Color.Transparent,
    scrim = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    surfaceBright = Color.Transparent,
    surfaceContainer = Color.Transparent,  // Assuming no direct mapping, use default or transparent
    surfaceContainerHigh = Color.Transparent,
    surfaceContainerHighest = Color.Transparent,
    surfaceContainerLow = Color.Transparent,
    surfaceContainerLowest = Color.Transparent,
    surfaceDim = Color.Transparent
)