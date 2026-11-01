# Compose implementation guide

Dependencies are the user's decision. If a backdrop-blur library is approved, Haze (Apache-2.0) supports Compose on Android with `haze` + `haze-blur` (and optional `haze-glass` for refraction). API names differ between major versions: always read the README of the installed version (VERIFY). Snippets below follow the Haze 2 README as researched on 1 Nov 2026 and are a starting point, not guaranteed to compile.

## Architecture
```
GlassTheme (tokens, CompositionLocals: GlassTokens, ReduceTransparency)
 └─ Scaffold-like root: Box { 
      content layer  -> Modifier.hazeSource(hazeState)   // screens, lists, images
      floating layer -> GlassSurface(...) overlays        // nav pill, top bar, FAB, search, toasts
    }
```
Rules
- One `HazeState` per screen root. `hazeSource` goes on the content layer only, never on a glass surface (prevents feedback and nested glass).
- Floating layer draws above content with `zIndex` and respects insets: `WindowInsets.systemBars`, gesture bar, IME. Nav pill floats 16 dp above the bottom inset.
- Content gets bottom/top content padding equal to the floating bar height so nothing is hidden.
- Glass components take a `tier` (thin/regular/thick) and a `shape`; they never take arbitrary alpha/blur numbers (tokens only).

## GlassSurface behaviour
1. Decide `canBlur = SDK_INT >= 31 && !reduceTransparency && blurEnabledByDevice`.
2. If `canBlur`: backdrop blur from the shared `HazeState` with the tier's radius, then draw the tier fill, edge hairline, top highlight, soft shadow.
3. Else: draw the tier's solid fallback fill + 1 dp border + same shadow. Layout must not change between modes.
4. Text colour comes from tokens; run the contrast script whenever fills change.
See `assets/GlassTheme.kt` for a template.

## Sheets, menus, dialogs
Modal sheets and dialogs usually render in their own window, so in-composition backdrop blur may not see the screen behind. Options in order:
1. Window background blur on the dialog window where supported (`Window.setBackgroundBlurRadius`, API 31+) after checking `WindowManager.isCrossWindowBlurEnabled` (VERIFY).
2. A custom in-window sheet (drawn in the same composition as the content layer) that uses the same `HazeState`.
3. Fallback: thick near-opaque sheet (fill alpha >= 0.82 or solid) plus a 35-45% scrim.

## Bottom navigation pill
Four tabs max. 28 dp radius, regular tier, height 64 dp, icons 24 dp with labels 12-13 sp. Active tab: accent tint on the icon and a subtle filled pill behind it (a fill, not another glass layer). Floating "+" button sits above the pill at the right (regular tier, accent tint allowed on this single primary action).

## Top bar
Transparent at scroll offset 0. Fades in the regular glass tier once content scrolls beneath it. Large title collapses into the bar. Only one bar is glass at a time.

## Swipe rows (Keep right / Delete left)
- Material 3 `SwipeToDismissBox` with two directions; positional threshold about 40% of the width (VERIFY the parameter name in your version).
- Right = Keep: emerald background + check icon + label "Keep". Left = Delete: coral background + trash icon + label "Delete".
- Delete moves to Trash and shows a snackbar with Undo (5 s). Never delete permanently on swipe.
- Haptic tick when the threshold is crossed (`HapticFeedbackType.TextHandleMove`/`LongPress`, pick one and keep it consistent).
- Accessibility: `semantics { customActions = listOf(CustomAccessibilityAction("Keep") {...}, CustomAccessibilityAction("Delete") {...}) }` plus buttons in the item menu.
- Settings to swap directions or disable swipe.

## Triage card stack
Top card follows the finger with rotation proportional to drag (max about 8 degrees); edge tint grows with drag distance (green right, coral left); release past 40% commits, otherwise spring back. Three bottom buttons duplicate the gestures. Summary screen shows counts with Undo all.

## Performance rules
- Max 2-3 blurred layers per screen (1-2 on low-end). Count them in review.
- Do not apply glass inside `LazyColumn` items. Keep the list item surfaces opaque.
- Avoid animating blur radius; animate alpha/offset.
- Prefer one shared `HazeState`; avoid creating blur styles on every recomposition (`remember`).
- Test on a 3-4 GB RAM device with a release build (R8, baseline profile). Use macrobenchmark for scroll jank.
- Provide a "Reduce transparency" setting in the app and honour system accessibility signals.

## Dark/light
Define both token sets; do not reuse light alphas in dark mode. Glow shapes behind the content layer (soft radial gradients, low intensity) give the blur something to work on; keep them in the background layer, not inside cards.

## Testing
- Unit test token contrast (call the script in CI or port the maths).
- Compose UI tests for: glass absent on cards/rows, fallback mode renders without blur, swipe actions + Undo, TalkBack custom actions.
- Screenshot tests (Paparazzi/Roborazzi or similar; VERIFY) for light, dark, reduced-transparency and 200% font.
