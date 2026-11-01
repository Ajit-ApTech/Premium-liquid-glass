// GlassTheme.kt - TEMPLATE for premium glass UI (Compose, min SDK 26).
// Adapt before use: imports for the chosen blur library (e.g. Haze) depend on its installed version.
// Names below follow the Haze 2 README (Nov 2026). Verify against your version and check visually on API 31+ and API 26-30.
package ui.glass

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
)

data class GlassTokens(
    val thin: GlassSpec,
    val regular: GlassSpec,
    val thick: GlassSpec,
    val text: Color,
) {
    fun spec(tier: GlassTier) = when (tier) {
        GlassTier.Thin -> thin
        GlassTier.Regular -> regular
        GlassTier.Thick -> thick
    }
}

// Values mirror assets/tokens.json (re-run scripts/contrast_check.py after any change).
val DarkGlassTokens = GlassTokens(
    thin = GlassSpec(16.dp, Color(0xFF0F1526).copy(alpha = 0.55f), Color(0xFF161D30), Color.White.copy(alpha = 0.14f)),
    regular = GlassSpec(24.dp, Color(0xFF0F1526).copy(alpha = 0.66f), Color(0xFF161D30), Color.White.copy(alpha = 0.16f)),
    thick = GlassSpec(32.dp, Color(0xFF0F1526).copy(alpha = 0.78f), Color(0xFF1B2338), Color.White.copy(alpha = 0.18f)),
    text = Color.White,
)
val LightGlassTokens = GlassTokens(
    thin = GlassSpec(16.dp, Color.White.copy(alpha = 0.55f), Color(0xFFF1F3F9), Color.White.copy(alpha = 0.70f)),
    regular = GlassSpec(24.dp, Color.White.copy(alpha = 0.70f), Color(0xFFF1F3F9), Color.White.copy(alpha = 0.80f)),
    thick = GlassSpec(32.dp, Color.White.copy(alpha = 0.82f), Color(0xFFFFFFFF), Color.White.copy(alpha = 0.90f)),
    text = Color(0xFF0A0E1A),
)

val LocalGlassTokens = compositionLocalOf { DarkGlassTokens }
/** Set from an in-app "Reduce transparency" switch and system accessibility/contrast signals. */
val LocalReduceTransparency = compositionLocalOf { false }

@Composable
fun GlassTheme(dark: Boolean, reduceTransparency: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalGlassTokens provides if (dark) DarkGlassTokens else LightGlassTokens,
        LocalReduceTransparency provides reduceTransparency,
        content = content,
    )
}

/**
 * Glass material for FLOATING UI only (nav pill, top bar, FAB, search, sheets, toasts).
 * Never use for cards, list rows or backgrounds. Never nest inside another GlassSurface.
 */
@Composable
fun GlassSurface(
    // hazeState: HazeState,          // shared per-screen state; content layer uses Modifier.hazeSource(hazeState)
    tier: GlassTier,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val tokens = LocalGlassTokens.current
    val spec = tokens.spec(tier)
    val canBlur = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !LocalReduceTransparency.current

    val base = modifier
        .shadow(elevation = 12.dp, shape = shape, ambientColor = Color.Black.copy(alpha = 0.18f), spotColor = Color.Black.copy(alpha = 0.28f))
        .clip(shape)

    val surface = if (canBlur) {
        // Backdrop blur from the shared source (Haze 2 style):
        // base.hazeBlur(input = HazeInput.Sources(hazeState), style = remember(spec.blur) { HazeBlurStyle { blurRadius(spec.blur) } })
        //     .background(spec.fill)
        base.background(spec.fill) // placeholder until the blur library is wired in
    } else {
        base.background(spec.solidFallback)
    }

    Box(modifier = surface.border(1.dp, spec.edge, shape), content = content)
}
