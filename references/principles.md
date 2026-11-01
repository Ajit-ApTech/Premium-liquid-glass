# Principles (Apple-derived, adapted for Android)

## Sources (read the originals when in doubt)
- Apple Human Interface Guidelines - Materials: https://developer.apple.com/design/human-interface-guidelines/materials
- Apple WWDC25 "Meet Liquid Glass": https://developer.apple.com/videos/play/wwdc2025/219/
- Apple Liquid Glass overview: https://developer.apple.com/documentation/TechnologyOverviews/liquid-glass
- Haze (Compose backdrop blur/glass, Apache-2.0): https://github.com/chrisbanes/haze
- Agent Skills specification (format of this skill): https://agentskills.io/specification
Take the principles; never copy Apple's visual assets, fonts or icons. The Apple font licence does not allow using San Francisco in UI mock-ups for non-Apple platforms.

## What Apple's guidance says (as researched 1 Nov 2026)
- Glass forms a distinct functional layer for controls and navigation (tab bars, toolbars, sidebars) that floats above the content layer and sets a clear hierarchy.
- Do not put glass in the content layer. Use it sparingly.
- Do not stack glass on glass; inside a glass surface use fills and transparency to separate items.
- Regular glass is the workhorse where there is text. Clear glass is only for media-rich backgrounds; over bright content add a dim layer (Apple's figure is about 35%) and keep foreground bold and bright.
- Glass must adapt to Reduce Transparency, Increase Contrast and Reduce Motion.
- Tint only to emphasise the primary action.

## Why these rules matter on Android
1. Native Apple glass bends light (lensing) and adapts per element. On Android you realistically get blur + tint + edge + shadow. Say so; do not oversell.
2. Legibility is the common failure. Default to the more opaque side; clear glass is the exception.
3. Blur over a flat backdrop is invisible. Smooth gradients blur into themselves. Put structure behind glass: scrolling content, imagery, or a deliberate soft glow placed behind the bar.
4. Every blurred layer re-samples what is behind it every frame. Budget them.
5. Premium comes from restraint: typography, spacing, alignment, one primary action per screen, calm colour, consistent radii, soft shadows.

## Layer model
| Layer | What | Glass? |
|---|---|---|
| 1 Content | what the user came for: cards, lists, notes, images, files, chat bubbles | No |
| 2 Structure | spacing, grids, type hierarchy, dividers | No |
| 3 Floating UI | nav pill, top bar over scroll, FAB, sheets, menus, search, toasts | Candidate (gate question) |

## Material properties
- Transparency is contextual per component, never one global value.
- Tint the material with the brand surface ink (not pure white) so text stays legible.
- Edge: 1 dp hairline at low alpha plus a faint top highlight; no thick white outlines.
- Shadow: soft, low alpha, large radius, small vertical offset, derived from the ink colour.
- Saturation boost, if any, is restrained (110-150%); above ~180% reads as candy.
- Adaptivity: raise opacity over busy regions, switch foreground light/dark, add a short scroll-edge fade under fixed bars, or fall back to solid.

## Shape language
- One radius per role: chip 999, control 16, card 24, nav pill 28, sheet 32.
- Nested shapes are concentric: inner radius = outer radius - padding.
- Pills are for filters, tags, statuses, compact actions and the floating nav; not for every button or card.

## Colour and type
- Colour comes from the brand tokens. No neon, rainbow or "AI gradient" as a modern signal. Backgrounds are continuous; soft aurora glows are allowed at low intensity because they give glass something to refract.
- Type: Inter (open licence). Prefer medium/semi-bold for small text on glass; thin weights fail over translucency.

## Accessibility is non-negotiable
Body text 4.5:1, large text and icons 3:1 over the worst-case backdrop. If glass hurts readability fix in this order: raise opacity -> change placement -> add contrast (scrim, stronger edge, heavier weight) -> remove glass.
