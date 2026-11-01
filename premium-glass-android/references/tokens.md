# Tokens and verified contrast

Source of truth: `assets/tokens.json`. After any change run:
`python scripts/contrast_check.py --tokens assets/tokens.json`

## Colour
| Role | Dark | Light |
|---|---|---|
| Background | #0A0E1A (+ faint aurora glows: indigo, teal, rose) | #F5F6FA (+ pastel mesh) |
| Surface (content cards) | #131A2B | #FFFFFF |
| Border | white 8% | ink 6% |
| Text / secondary | white / white 64% | #0A0E1A / ink 62% |
| Accent (indigo) | #5B6CFF | #5B6CFF |
| Keep / Delete / Remind / Sensitive | #2BD48A / #FF5C6C / #FFB84D / #8B7CFF | same |

## Glass tiers and verified worst-case contrast
Worst case = text over the glass fill composited on pure black, pure white and mid grey, before blur (blur only helps).
| Mode | Tier | Fill | Blur | Worst contrast | Required | Result |
|---|---|---|---|---|---|---|
| dark | thin | ink #0F1526 @ 0.55 | 16 dp | 4.04:1 | 3.0 (icons/large text only) | PASS |
| dark | regular | ink #0F1526 @ 0.66 | 24 dp | 5.86:1 | 4.5 | PASS |
| dark | thick | ink #0F1526 @ 0.78 | 32 dp | 9.06:1 | 4.5 | PASS |
| light | thin | white @ 0.55 | 16 dp | 5.74:1 | 3.0 | PASS |
| light | regular | white @ 0.70 | 24 dp | 9.13:1 | 4.5 | PASS |
| light | thick | white @ 0.82 | 32 dp | 12.62:1 | 4.5 | PASS |
Thin tier is for icon-only or large-label controls; do not put body text on it.

## A cautionary example
A "translucent white 14%" fill in dark mode (common in mock-ups) gives 1.00:1 contrast for white text over a white backdrop (and 3.12:1 over mid grey). It looks glassy in a screenshot over dark art but fails in use. Tint dark glass with ink instead. (The image-prompt style block describes the look; the code tokens above are what you ship.)

## Geometry and type
- Radius: chip 999, control 16, card 24, nav pill 28, sheet 32. Spacing: 4, 8, 12, 16, 20, 24, 32 dp. Side margins 20 dp. Nav pill floats 16 dp from the screen edges and above the gesture bar.
- Type (Inter): title 34/40 semi-bold, headline 20/28 semi-bold, body 16/24, caption 13/18 medium. Use tabular figures for amounts.
- Edge: 1 dp hairline; highlight alpha dark 0.20 / light 0.60 on the top-left inner edge.
- Shadow: y 10, blur 28, alpha 0.28 (ink-derived, not pure black).
- Motion: 150 / 250 / 350 ms.

## Solid fallbacks
dark #161D30 (thick #1B2338), light #F1F3F9 (thick #FFFFFF), each with a 1 dp border. Use when API < 31, Reduce transparency is on, high contrast/large text mode is detected, or blur is unavailable.
