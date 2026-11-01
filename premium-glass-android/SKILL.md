---
name: premium-glass-android
description: >-
  Design and build premium, Apple-inspired "liquid glass" interfaces for Android apps with Jetpack Compose - floating translucent navigation, sheets and controls over calm opaque content, with dark/light tokens, legibility checks, accessibility fallbacks, blur-performance limits, and AI image-generation prompts for reference screens. Use when asked for a premium, Apple-like, iOS 26, liquid-glass, glassmorphism or frosted-glass look on Android or Compose, to write screen mock-up prompts for such an app, or to review UI code or designs for glass misuse. Not for web-only or SwiftUI-only work, and never for copying Apple fonts, icons or logos.
license: Proprietary - for the Retain project owner
compatibility: >-
  Designed for Jetpack Compose (Material 3), min SDK 26. Scripts need Python 3 and no network access. Third-party blur library (Haze, Apache-2.0) is optional and chosen by the user.
metadata:
  version: "1.0"
  last-researched: "2026-11-01"
---

# Premium Glass for Android

Goal: an interface that feels calm, spatial and expensive. Glass is a material for the controls that float above content. It is not a style to sprinkle everywhere. Success test: people say "this feels clean and layered", never "someone added blur to everything".

This skill controls material, hierarchy, depth, motion and accessibility. The project's own brand controls identity and copy. Default brand values are in `assets/tokens.json` (Retain palette) and can be replaced.

## 0. Safety and scope
- Work only inside the project the user selected. Do not read secrets, keys or environment variables. Do not make network requests or install packages; if a library is needed, name it and let the user decide.
- Do not overwrite existing files without being asked; propose first (section 2).
- Use fictional example content only.
- Never use Apple fonts (San Francisco), SF Symbols, Apple logos, or iOS chrome. Use Inter and Material Symbols/Lucide-style icons.
- Facts marked (VERIFY) can age (APIs, versions). Tell the user to confirm them.

## 1. The five hard rules
1. Content is never glass. Cards, list rows, forms, page backgrounds and media stay opaque or soft-surface.
2. Glass only on the floating layer: bottom navigation pill, top bar over scrolling content, floating action button, bottom sheets and menus, search field, toasts/snackbars, small floating chips.
3. Never glass on glass. Inside a glass surface separate items with fills, dividers or opacity.
4. Legibility beats looks. Body text needs 4.5:1 (large text and icons 3:1) over the worst-case backdrop. Run `scripts/contrast_check.py`.
5. Every glass surface has a solid fallback (old Android, Reduce transparency, high contrast, battery saver, blur disabled by the device).

Gate question before applying glass to any element: "What functional role does this material communicate?" Valid answers: it floats above scrolling content; it is temporary or contextual; it must stay reachable while content moves beneath it. No answer = no glass.

## 2. Workflow
1. Inspect: platform, min/target SDK, existing theme/tokens, navigation structure, accessibility needs, performance targets. List open questions instead of guessing.
2. Layer map (before any code): table of elements -> layer (content / structure / floating) -> glass yes/no -> tier -> reason. Present it with the plan.
3. Tokens: start from `assets/tokens.json`; adjust brand colours; re-run the contrast check for every tier in both modes.
4. Build with the templates in `assets/GlassTheme.kt` and the guidance in `references/compose-implementation.md`.
5. Verify with `references/review-checklist.md` and `scripts/audit_compose.py`. Report evidence (contrast values, glass-layer count per screen, fallback screenshots). Do not claim a check passed without evidence.

## 3. Material tiers (starting values, dp)
| Tier | Use | Blur | Fill (dark) | Fill (light) |
|---|---|---|---|---|
| none | content, cards, lists, backgrounds | - | - | - |
| thin | icon-only or large-label controls | 16 | ink @ 0.55 | white @ 0.55 |
| regular | nav pill, top bar, search, FAB, toasts | 24 | ink @ 0.66 | white @ 0.70 |
| thick | sheets, menus, dialogs, text-heavy overlays | 32 | ink @ 0.78 | white @ 0.82 |
| scrim | dim behind modal sheets | 0-8 | black @ 0.20-0.45 | black @ 0.20-0.45 |
Details and verified contrast numbers: `references/tokens.md`. Fills are tinted with the surface ink colour, not pure white, so dark-mode text survives bright backdrops.

## 4. Android reality check (read before promising anything)
- `Modifier.blur()` / RenderEffect blur the element's own content, not what is behind it. Real backdrop blur needs a library (Haze is Apache-2.0 and supports Compose on Android) or window blur for dialogs. Library API names change between versions: read the installed version's README (VERIFY).
- Blur effects need Android 12 (API 31) or higher (VERIFY). Below that, use the solid fallback surface.
- Modal bottom sheets and dialogs live in a separate window, so in-composition backdrop blur may not see the content behind them. Use window background blur where supported and check `WindowManager.isCrossWindowBlurEnabled` (VERIFY), otherwise a thick near-opaque sheet with a scrim.
- Blur over a flat gradient is invisible; glass needs structure behind it (content, imagery, or a deliberate soft glow). Otherwise it costs performance for nothing.
- Budget: at most 2-3 simultaneous blurred layers per screen (1-2 on low-end phones). Never blur list items or whole screens. Do not animate blur radius on large surfaces; animate alpha or transform.
- Test on a low-end phone (3-4 GB RAM) in a release-like build.

## 5. Accessibility modes (implement all)
- Reduce transparency (in-app setting + system "remove animations"/high contrast signals): swap glass for solid surface tokens with a visible 1 dp border.
- Large font (200%) and TalkBack: no information only in colour; labels on icon-only controls; focus order sane.
- Reduced motion: replace movement with fades; keep state feedback.
- Touch targets at least 48 dp. Gestures (swipe) always have a button or customActions alternative.

## 6. Components
Specs for navigation pill, top bar, FAB, sheets, search field, chips, cards, swipe rows, triage stack, chat input, masked-value chips: `references/components.md`. Use one radius per role (chip 999, control 16, card 24, nav pill 28, sheet 32); nested radius = outer radius - padding.

## 7. Motion
Springs 250-350 ms, short fades 150 ms, no bounce overshoot. Haptic tick at swipe threshold. Pressed state: scale 0.97 plus a soft highlight. Honour reduced motion.

## 8. Image-prompt mode (reference screens)
When asked for mock-up prompts, follow `references/image-prompts.md`: one screen per prompt, self-contained style block, exact on-screen text, glass only on the floating layer, "cards are opaque", Android status bar, no Apple elements. Six worked screen prompts are in the reference. Remind the user that generators garble small text and Devanagari; use outputs as layout and mood references, then rebuild in Figma/Compose.

## 9. Review mode
When asked to review a design or code: classify each element by layer, count glass layers per screen, run the audit script (`python scripts/audit_compose.py <src>`), run the contrast script, then list findings by severity with the fix from `references/review-checklist.md`.

## 10. Output expectations
For any task, state: what is glass and why; which tier; contrast evidence; fallbacks; anything approximated or unverified. Keep changes scoped: do not redesign unrelated UX; list unrelated issues separately.

## References and files
- `references/principles.md` - Apple-derived principles and sources
- `references/tokens.md` - token tables and verified contrast results
- `references/compose-implementation.md` - Compose architecture, snippets, fallbacks, performance
- `references/components.md` - component specs and states
- `references/image-prompts.md` - prompt template and workflow
- `references/review-checklist.md` - quality gates and anti-patterns
- `scripts/contrast_check.py`, `scripts/audit_compose.py` - local checkers (no network)
- `assets/tokens.json`, `assets/GlassTheme.kt` (optional: add the 45-screen Retain prompt set as `assets/retain-screen-prompts.md`)

