# Review checklist

## Gate checks (any "no" = revise)
| Check | Question | Evidence |
|---|---|---|
| Layering | Are content and floating UI clearly different layers? | layer map table |
| Material | Does every glass element have a functional reason? | gate-question answer per element |
| Restraint | Glass layers per screen within budget (<= 3; <= 2 on low-end)? | count per screen |
| No nesting | Any glass on glass? | audit script G2 = 0 |
| Content opaque | Any glass on cards/rows/lists/backgrounds? | audit script G1 = 0 |
| Legibility | Body text >= 4.5:1, large text/icons >= 3:1 on worst-case backdrop? | contrast script output |
| Fallbacks | Solid version for API < 31, Reduce transparency, 200% font, high contrast? | screenshots |
| Gestures | Every swipe has a button/customAction alternative and Undo? | UI test |
| Consistency | Radii, spacing, type map to tokens? | audit G5/G6 clean |
| Performance | Smooth on a 3-4 GB device in release build? | macrobenchmark / manual |
| Brand | Does it feel like this product, not a generic template? | review notes |

## Anti-patterns (fix on sight)
1. Glass card grids or glass list rows.
2. Glass page backgrounds or full-screen blur.
3. Glass inside glass.
4. White 10-15% fill in dark mode over bright content (fails contrast).
5. One glass treatment everywhere (no tiers).
6. Giant rounded rectangles; pills on everything.
7. Heavy black shadows; neon or purple "AI" gradients; random blobs inside cards.
8. Accent tint on every control (tint only the primary action).
9. Clear glass over text-heavy or light content.
10. Blur as the only affordance (controls must read as controls with glass off).
11. Animating blur radius on large areas.
12. Using Modifier.blur for backdrop effects (it blurs the element itself).
13. Apple clones: San Francisco font, SF Symbols, iOS status bar, Apple logos.
14. Glass that only appears on hover/press (frost at rest or not at all).
15. Relying on a screenshot's look in an image generator as proof of legibility.

## How to measure
- Contrast: `python scripts/contrast_check.py --text <hex> --glass <hex@alpha>` for each tier and mode; tune alpha or tint until it passes.
- Audit: `python scripts/audit_compose.py app/src --strict` (heuristic; review every hit).
- Layer count: list every blurred surface visible at once for each screen state (scrolling, sheet open, keyboard open).
- Fallback screenshots: API 30 emulator or reduced-transparency switch; 200% font; dark and light; TalkBack pass.
- Low-end device: record scroll frame times with all glass off vs on; report both.

## Delivery report template
- What is glass and why (table).
- Tier + token per element.
- Contrast results (numbers).
- Layer count per screen.
- Fallback behaviour and screenshots.
- Approximated or unverified items (VERIFY list).
- Out-of-scope issues noticed.
