# premium-glass-android - install and use

## What it is
An Agent Skill (SKILL.md format, open specification at https://agentskills.io/specification) that teaches an AI coding/design agent to create premium, Apple-inspired glass interfaces for Android (Jetpack Compose), write reference-image prompts for screens, and review UI for glass misuse.

## Install
- Claude Code (project): copy the folder `premium-glass-android/` to `.claude/skills/premium-glass-android/` in your repo. Personal: `~/.claude/skills/premium-glass-android/`.
- claude.ai: upload the zip (it contains the `premium-glass-android/` folder) under Settings > Features (custom Skills need a plan with code execution enabled).
- Other agents that implement the Agent Skills specification: place the folder in that tool's skills directory (check its docs). The folder name must equal the skill name.
- Reload the agent session after installing.
- Optional: put the 45-screen `Retain_Screen_Prompts.md` file in `assets/` as `retain-screen-prompts.md`.

## Use
- "Use the premium-glass-android skill to design the Today screen for Retain."
- "Use premium-glass-android to implement the floating navigation pill and top bar in Compose."
- "Use premium-glass-android to write image prompts for the Ask screen, dark and light."
- "Use premium-glass-android to review this Compose module for glass misuse."
The agent should present a layer map before changing UI, run the contrast and audit scripts, and report evidence.

## Security note
Skills can include scripts. This one has two small local Python scripts (no network, no installs; they only read the files you point them at). Read them before use, as you would any software.

## Files
SKILL.md; references/ (principles, tokens, compose-implementation, components, image-prompts, review-checklist); scripts/ (contrast_check.py, audit_compose.py); assets/ (tokens.json, GlassTheme.kt template).

## Verify before shipping
Haze version and API names, blur behaviour on API 31+ target devices, cross-window blur for sheets, swipe-to-dismiss parameter names in your Material 3 version, and that the GlassTheme.kt template compiles after wiring in the blur library.

