// GlassTheme.kt - TEMPLATE for premium glass UI (Compose, min SDK 26).
// Adapt before use: imports for the chosen blur library (e.g. Haze) depend on its installed version.
// Names below follow the Haze 2 README (Nov 2026). Verify against your version and check visually on API 31+ and API 26-30.
package app.retain.ui.glass

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
// import dev.chrisbanes.haze.* ; import dev.chrisbanes.haze.blur.*   // <- adjust to the installed Haze version

enum class GlassTier { Thin, Regular, Thick }

data class GlassSpec(
    val blur: Dp,
    val fill: Color,          // tinted with surface ink, see tokens.json
    val solidFallback: Color, // used when blur is unavailable
    val edge: Color,          // 1 dp hairline
    val highlightTop: Color   // soft inner reflection at the top edge
)

data class GlassTokens(
    val thin: GlassSpec,
    val regular: GlassSpec,
    val thick: GlassSpec,
    val isDark: Boolean
)

val LocalGlassTokens = compositionLocalOf<GlassTokens> { error("No GlassTokens provided") }
val LocalReduceTransparency = compositionLocalOf { false }

@Composable
fun GlassTheme(
    isDark: Boolean,
    reduceTransparency: Boolean = false,
    content: @Composable () -> Unit
) {
    val tokens = remember(isDark) {
        if (isDark) {
            GlassTokens(
                thin = GlassSpec(16.dp, Color(0x8C0F1526), Color(0xFF161D30), Color(0x1FFFFFFF), Color(0x33FFFFFF)),
                regular = GlassSpec(24.dp, Color(0xA80F1526), Color(0xFF161D30), Color(0x1FFFFFFF), Color(0x33FFFFFF)),
                thick = GlassSpec(32.dp, Color(0xC70F1526), Color(0xFF1B2338), Color(0x1FFFFFFF), Color(0x33FFFFFF)),
                isDark = true
            )
        } else {
            GlassTokens(
                thin = GlassSpec(16.dp, Color(0x8CFFFFFF), Color(0xFFF1F3F9), Color(0x99FFFFFF), Color(0x99FFFFFF)),
                regular = GlassSpec(24.dp, Color(0xB3FFFFFF), Color(0xFFF1F3F9), Color(0x99FFFFFF), Color(0x99FFFFFF)),
                thick = GlassSpec(32.dp, Color(0xD1FFFFFF), Color(0xFFFFFFFF), Color(0x99FFFFFF), Color(0x99FFFFFF)),
                isDark = false
            )
        }
    }
    CompositionLocalProvider(
        LocalGlassTokens provides tokens,
        LocalReduceTransparency provides reduceTransparency,
        content = content
    )
}

/**
 * Foundation glass container.
 * Wire your HazeState here once the library is chosen.
 */
@Composable
fun GlassSurface(
    tier: GlassTier,
    shape: Shape,
    modifier: Modifier = Modifier,
    // hazeState: HazeState? = null,    // uncomment when Haze is wired
    content: @Composable BoxScope.() -> Unit
) {
    val tokens = LocalGlassTokens.current
    val spec = when (tier) {
        GlassTier.Thin -> tokens.thin
        GlassTier.Regular -> tokens.regular
        GlassTier.Thick -> tokens.thick
    }
    val reduceTransparency = LocalReduceTransparency.current
    val canBlur = Build.VERSION.SDK_INT >= 31 && !reduceTransparency

    val baseMod = modifier
        .shadow(elevation = 10.dp, shape = shape, spotColor = Color(0x47000000), ambientColor = Color(0x20000000))
        .clip(shape)

    val surfaceMod = if (canBlur) {
        baseMod
            // .hazeEffect(state = hazeState, style = HazeDefaults.style(backgroundColor = spec.fill, blurRadius = spec.blur))
            .background(spec.fill)
            .border(1.dp, spec.edge, shape)
    } else {
        baseMod
            .background(spec.solidFallback)
            .border(1.dp, if (tokens.isDark) Color(0x26FFFFFF) else Color(0x1A0A0E1A), shape)
    }

    Box(modifier = surfaceMod, content = content)
}

