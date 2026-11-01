---
name: premium-liquid-glass
description: >-
  Design and build premium, Apple-inspired liquid glass interfaces across platforms (Android Jetpack Compose, iOS SwiftUI, Flutter, Web / CSS / Tailwind) — floating translucent navigation, sheets, and controls over calm opaque content, with dark/light tokens, legibility checks, accessibility fallbacks, blur-performance limits, and AI image-generation prompts for reference screens. Use when asked for a premium, Apple-like, iOS 26, liquid-glass, glassmorphism, or frosted-glass look on any platform, to write screen mock-up prompts for such apps, or to review UI code and designs for glass misuse.
license: MIT
compatibility: >-
  Multi-platform: Android (Jetpack Compose, min SDK 26/31+), iOS (SwiftUI, iOS 15+), Flutter (3.0+), Web (CSS / Tailwind with backdrop-filter). Local Python scripts require Python 3 and no network access.
metadata:
  version: "2.0"
  last-researched: "2026-11-02"
---

# Premium Liquid Glass

Goal: an interface that feels calm, spatial and expensive. Glass is a material for the controls that float above content. It is not a style to sprinkle everywhere. Success test: users say "this feels clean and layered", never "someone added blur to everything".

This skill controls material, hierarchy, depth, motion and accessibility across **Android (Compose)**, **iOS (SwiftUI)**, **Flutter**, and **Web (CSS / Tailwind)**. The project's own brand controls identity, palette and copy. Default brand tokens are in `assets/tokens.json`.

## 0. Safety and scope
- Work only inside the project the user selected. Do not read secrets, keys or environment variables. Do not make network requests or install packages; if a library is needed, name it and let the user decide.
- Propose changes before modifying existing application code.
- Use fictional example content only.
- Never use Apple fonts (San Francisco) or SF Symbols on non-Apple platforms. Use Inter, Roboto, or platform-appropriate fonts with Material Symbols/Lucide-style icons. On iOS (SwiftUI), native SF Symbols and system fonts are standard.
- Facts marked (VERIFY) can age with API versions; always confirm against project dependencies.

## 1. The five hard rules
1. **Content is never glass**. Cards, list rows, forms, page backgrounds, and media stay opaque or soft-surface.
2. **Glass only on the floating layer**: bottom navigation pill, top bar over scrolling content, floating action button, bottom sheets and menus, search field, toasts/snackbars, and small floating chips.
3. **Never glass on glass**. Inside a glass surface, separate items with solid fills, subtle dividers, or opacity differences.
4. **Legibility beats looks**. Body text needs 4.5:1 contrast (large text and icons 3:1) over the worst-case backdrop. Run `scripts/contrast_check.py`.
5. **Every glass surface has a solid fallback** (older OS versions, Reduce Transparency accessibility setting, battery saver, low GPU capacity, or browsers lacking `backdrop-filter`).

Gate question before applying glass to any element: *"What functional role does this material communicate?"*
Valid answers: it floats above scrolling content; it is temporary or contextual; it must stay reachable while content moves beneath it. No answer = no glass.

## 2. Universal workflow
1. **Inspect**: Target platform (Android Compose, iOS SwiftUI, Flutter, Web), OS version limits, existing theme tokens, navigation structure, accessibility requirements, and performance targets.
2. **Layer map (before any code)**: Table of elements -> layer (content / structure / floating) -> glass yes/no -> tier -> reason. Present it with the plan.
3. **Tokens**: Start from `assets/tokens.json`; adjust brand colors; re-run `python scripts/contrast_check.py --tokens assets/tokens.json` for every tier in both modes.
4. **Build with platform templates**:
   - **Android**: `assets/GlassTheme.kt` & `references/compose-implementation.md`
   - **iOS (SwiftUI)**: `assets/GlassView.swift` & `references/swiftui-implementation.md`
   - **Flutter**: `assets/glass_theme.dart` & `references/flutter-implementation.md`
   - **Web (CSS/Tailwind)**: `assets/glass.css`, `assets/tailwind-glass.config.js` & `references/web-implementation.md`
5. **Verify**: Check with `references/review-checklist.md` and `scripts/audit_compose.py` (Android). Report evidence (contrast ratios, glass-layer count per screen, fallback rendering).

## 3. Material tiers (starting values)

| Tier | Use | Blur | Fill (dark) | Fill (light) |
|---|---|---|---|---|
| **none** | content, cards, lists, backgrounds | - | Opaque surface | Opaque surface |
| **thin** | icon-only or large-label controls, chips | 16 dp/px | ink @ 0.55 | white @ 0.55 |
| **regular** | nav pill, top bar, search field, FAB, toasts | 24 dp/px | ink @ 0.66 | white @ 0.70 |
| **thick** | sheets, menus, dialogs, text-heavy overlays | 32 dp/px | ink @ 0.78 | white @ 0.82 |
| **scrim** | dim behind modal sheets | 0-8 dp/px | black @ 0.20-0.45 | black @ 0.20-0.45 |

Details and verified contrast numbers: `references/tokens.md`. Fills are tinted with the surface ink color, not pure white, so dark-mode text survives bright backdrops.

## 4. Platform reality checks

### Android (Jetpack Compose)
- `Modifier.blur()` / RenderEffect blurs the element's own content, not what is behind it. Real backdrop blur needs a library (Haze is Apache-2.0 and supports Compose) or window blur for dialogs (API 31+).
- Blur effects need Android 12 (API 31) or higher (VERIFY). Below API 31, always render the solid fallback surface.
- Budget: At most 2-3 simultaneous blurred layers per screen. Never blur list items.
- Full details: `references/compose-implementation.md`.

### iOS (SwiftUI)
- Use native SwiftUI materials: `.ultraThinMaterial` (thin tier), `.thinMaterial` / `.regularMaterial` (regular tier), and `.thickMaterial` (thick tier).
- Support iOS continuous corners with `RoundedRectangle(cornerRadius: ..., style: .continuous)`.
- Use `.presentationBackground(.thinMaterial)` for bottom sheets (iOS 16.4+).
- Full details: `references/swiftui-implementation.md`.

### Flutter
- Use `BackdropFilter` with `ImageFilter.blur(sigmaX: tier.blur, sigmaY: tier.blur)` wrapped in `ClipRRect`.
- **Always wrap in `RepaintBoundary`** to prevent repainting the backdrop blur on every frame.
- Never use `BackdropFilter` inside `ListView.builder` or `GridView.builder` items.
- Full details: `references/flutter-implementation.md`.

### Web (CSS / Tailwind CSS)
- Always include both `-webkit-backdrop-filter: blur(...)` and `backdrop-filter: blur(...)` for cross-browser support (Safari requires `-webkit-`).
- Always specify an `@supports not (backdrop-filter: blur(1px))` fallback for older browsers.
- Always implement `@media (prefers-reduced-transparency: reduce)` fallback to solid backgrounds.
- Full details: `references/web-implementation.md`.

## 5. Accessibility modes (implement all)
- **Reduce transparency**: Check system accessibility flags (`prefers-reduced-transparency` on Web, `accessibilityReduceTransparency` in SwiftUI, `accessibleNavigation` in Flutter, or in-app toggle on Android). Swap glass for solid surface tokens with a visible 1 dp/px border.
- **Large font (200%) and screen readers**: No information conveyed only in color; labels on icon-only controls; logical focus order.
- **Reduced motion**: Replace spring movement with fast fades (150 ms); preserve state feedback.
- **Touch targets**: Minimum 48x48 dp / 44x44 pt. Gestures (swipe) always provide a button or accessible menu alternative.

## 6. Components & geometry
Specs for navigation pill, top bar, FAB, sheets, search field, chips, cards, and triage stacks: `references/components.md`.
Use consistent corner radii:
- Small chips: 999 (full pill)
- Small controls / buttons: 16 dp/pt
- Content cards (opaque): 24 dp/pt
- Floating navigation pill: 28 dp/pt
- Modal sheets: 32 dp/pt
- Nested element radius = outer radius - padding.

## 7. Motion & haptics
Spring animations (250-350 ms duration), short fades (150 ms), minimal or zero bounce overshoot. Subtle haptic tick at threshold triggers (swipe commit, pill selection). Pressed state: scale 0.97 plus a soft highlight. Honor system reduced motion settings.

## 8. Image-prompt mode (reference screens)
When generating UI mock-up prompts, follow `references/image-prompts.md`: one screen per prompt, self-contained style block, exact on-screen text, glass only on the floating layer, "cards are opaque", platform-accurate status bar. Use outputs as layout and mood references, then implement in code with design tokens.

## 9. Review mode
When asked to review a design or codebase:
1. Classify each UI element into its layer (content vs. floating).
2. Count glass layers per screen (enforce maximum 2-3 budget).
3. Scan for anti-patterns (glass on cards, glass nested in glass, missing fallbacks).
4. Run contrast verification (`python scripts/contrast_check.py --tokens assets/tokens.json`).
5. For Android projects, run `python scripts/audit_compose.py <src>`.
6. List findings by severity with corrective guidance from `references/review-checklist.md`.

## 10. References and files
- `references/principles.md` - Core spatial principles & Apple design inspiration
- `references/tokens.md` - Token tables, color values, and verified contrast numbers
- `references/compose-implementation.md` - Android Compose architecture & code snippets
- `references/swiftui-implementation.md` - iOS SwiftUI architecture & code snippets
- `references/flutter-implementation.md` - Flutter architecture & code snippets
- `references/web-implementation.md` - Web CSS & Tailwind CSS architecture & code snippets
- `references/components.md` - Component geometry, layouts, and states
- `references/image-prompts.md` - Prompt templates for AI mock-up generation
- `references/review-checklist.md` - Quality gates and anti-patterns
- `scripts/contrast_check.py` - WCAG contrast verification script
- `scripts/audit_compose.py` - Compose static scanner script
- `assets/tokens.json` - Complete design token schema
- `assets/GlassTheme.kt` - Jetpack Compose template
- `assets/GlassView.swift` - SwiftUI template
- `assets/glass_theme.dart` - Flutter template
- `assets/glass.css` & `assets/tailwind-glass.config.js` - Web CSS & Tailwind templates
