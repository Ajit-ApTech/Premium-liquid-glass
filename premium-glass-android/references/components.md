# Component specs

All sizes in dp. Glass components use tokens only. "Opaque" = soft surface token (not glass).

| Component | Material | Spec |
|---|---|---|
| Bottom navigation pill | glass regular | floating, 28 radius, height 64, 16 from edges and above gesture bar, 4 tabs, active tab = accent icon + subtle fill |
| Top app bar | glass regular only once content scrolls beneath | large title collapses; max one glass bar visible |
| Floating action button (+) | glass regular, accent tint allowed | 56, circular, above the pill at right; the single primary action |
| Search field | glass regular on floating contexts; opaque field inside forms | 48 height, 16-20 radius, clear button, no nested glass |
| Bottom sheet / menu | glass thick (+ scrim) or solid fallback | 32 top radius, drag handle, content has padding 20 |
| Segmented control | glass thin/regular | selected segment is a fill, not extra glass |
| Toast / snackbar | glass regular/thick | above the pill; action text accent |
| Item card | opaque | 24 radius, border hairline, padding 16; thumbnail left (72), title, key-detail line, reason chip, date |
| Chips | opaque fill (or glass only when floating over media) | 999 radius, 32-36 height, selected = accent tint |
| Swipe backgrounds | opaque semantic colours | keep emerald, delete coral, labels + icons |
| Triage card | opaque | large preview, rotation by drag, edge tint |
| Chat bubbles | opaque (user = accent, assistant = surface) | tappable masked chips inside the assistant bubble |
| Chat input bar | glass regular | above the nav pill, mic + send, 20 radius |
| Masked-value chip | violet tinted opaque chip with eye icon | e.g. "Card ..98"; tap = reveal flow |
| Notification/settings rows | opaque | 56+ height, icon + text + trailing control |
| Paywall plan cards | opaque with accent border on the best value | badge "Best value"; one primary button |

## States (every interactive component)
rest, pressed (scale 0.97 + soft highlight), focused (visible ring, 2 dp accent), disabled (38% alpha), loading (skeleton, not spinner overlays on glass), error (coral text + icon, not colour alone).

## Empty, error and offline states
Friendly one-line explanation, one action button, small illustration or icon. Offline: neutral banner, never a red error for expected conditions. "Not fetched" and "Unavailable (snapshot)" chips for link cards. Stale metadata: subtle "Updated 3 days ago", amber banner after the staleness threshold.

## Privacy-related UI
- "AI sees masked data" chip near chat input; opens the "What the AI will see" preview sheet.
- Reveal sheet: biometric row, revealed value with countdown ring (10 s), Copy and Hide now.
- Sensitive items show a violet shield; excluded-from-AI items show a small slash-eye icon.

## Copy tone
Short, calm, concrete verbs (Keep, Delete, Remind me, Clean up). No hype words. Hindi strings supplied with every English string.
