# Image-generation prompts for reference screens

Use when asked for mock-up prompts. Output one self-contained prompt per screen.

## Template
```
[COMMON STYLE BLOCK]
[MODE LINE: dark or light]
SCREEN: <exact layout, exact on-screen text, which parts are glass>
AVOID: garbled text, extra logos, watermarks, phone frame, iOS status bar, Dynamic Island, glass on cards, stock photos of people.
```

## Common style block (copy as is)
```
Premium glass UI design reference, ONE Android phone screen, flat front view, 1080x2400 (9:20), no hands, no phone bezel, Android status bar and gesture bar (no iOS elements, no Apple logo). Calm, premium, editorial, generous spacing, Apple-inspired materials with Android-native layout. Font: Inter (large title 34px semi-bold, body 16px, caption 13px). Accent Retain Indigo #5B6CFF; Keep emerald #2BD48A; Delete coral #FF5C6C; Remind amber #FFB84D; Sensitive violet #8B7CFF. Glass material (28px blur, translucent fill, thin bright edge, soft shadow) is used ONLY on the floating bottom navigation pill, top bar, floating + button, bottom sheets, search field and toasts. Cards and list rows are opaque soft surfaces, NOT glass. Never place glass on glass. Rounded line icons, cards radius 24, nav pill radius 28, sheets radius 32, 20px side margins. Use exactly the text given, crisp readable text, no lorem ipsum.
```
Mode lines:
- dark: `MODE: dark - background ink #0A0E1A with soft aurora gradients (indigo, teal, rose) at low intensity; surfaces #131A2B with 1px border white 8%; text white 92% / 64% / 40%.`
- light: `MODE: light - background warm white #F5F6FA with subtle pastel mesh gradients (lavender, mint, peach); surfaces white with 1px border ink 6%; text ink 92% / 62% / 40%.`
Swap palette, name and accent for other projects; keep the glass rules.

## Rules for good prompts
1. One screen per prompt; give exact text for every label, amount and button.
2. State which elements are glass; everything else is opaque.
3. Include Android chrome only (status bar, gesture bar).
4. Ask for dark and light versions of key screens; Hindi variants for localisation checks (generators often garble Devanagari).
5. Use outputs as layout and mood references. Rebuild precisely in Figma/Compose with the tokens; never ship generated images as UI.
6. Keep wording of the style block identical across prompts for consistency; reuse a good result as a reference image if the generator supports it.

## Mapping a generated image to code
List each visible element -> layer -> glass yes/no -> tier -> token. Reject any glass on cards/rows. Check contrast with `scripts/contrast_check.py`. Check the glass-layer count (<= 3). Provide the solid fallback version.

## Worked examples (append the style block and a mode line to each)
**Today**
```
SCREEN: Today tab. Large title "Today" with search and settings icons. Opaque recap card: "Yesterday you saved 6 things", "3 screenshots · 2 links · 1 note", button "View yesterday". Section "Needs review (4)" with three opaque item cards: "Electricity bill / ₹1,842 · due 5 Nov / chip Pay", "How to fix slow Wi-Fi / YouTube · 12 min / chip Watch later", "Ideas for Diwali gifts / Note / chip Try this". Section "Due today": reminder card "Pay rent · 6:00 PM". Floating glass bottom navigation pill with tabs Today (active), Timeline, Library, Ask, and a floating glass + button.
```
**Ask (chat)**
```
SCREEN: Ask tab. Top: title "Ask" with chips "AI sees masked data" and "Gemini". User bubble (indigo): "How much did I pay for electricity last month?" Assistant bubble (opaque): "You paid [₹1,842] on 5 Oct using [Card ••98]." with the bracketed items as tappable glass chips with an eye icon. Two mini source cards below. Glass input bar "Ask about your saved things" with mic and send. Floating glass nav pill with Ask active.
```
**Triage**
```
SCREEN: Triage mode. Title "Review", progress "7 of 20", close button. A large opaque card with a screenshot preview and key detail "₹842 · Zomato order · 2 Nov", rotated slightly and dragged right; a green "Keep" label with a check at its left edge and a faint green edge glow. Two cards peek behind. Three round glass buttons at the bottom: Delete (coral), Keep detail only (indigo), Keep (emerald).
```
**Add provider**
```
SCREEN: "Add provider". Preset chips: OpenAI, Gemini, Anthropic, Groq, OpenRouter, Mistral, DeepSeek, xAI, Ollama, Custom (selected). Opaque fields: Name "My server"; Base URL "https://api.example.com/v1"; API key (masked, eye icon); Model "gpt-4o-mini" with link "Load models". Violet note card "Your key is stored encrypted on this phone." Buttons "Test connection" and "Save".
```
**What the AI will see**
```
SCREEN: Glass bottom sheet "What the AI will see" over a dimmed chat. Opaque preview: "Paid [AMT_1] to [NAME_1] using [CARD_1] ••98" with placeholders highlighted in violet. Row "Gemini (your key)". Note "Real values never leave your phone." Buttons "Send" and "Edit".
```
**Paywall**
```
SCREEN: Paywall with aurora background. Title "Retain Plus". Checklist: "Multiple AI providers with auto-fallback", "More on-device AI", "Encrypted Drive backup", "Smart rules and bulk cleanup". Plan cards: Monthly ₹79; Yearly ₹599 with "Best value" badge; Lifetime ₹1,999. Button "Start 7-day free trial". Small text "Cancel anytime · Restore purchases".
```

