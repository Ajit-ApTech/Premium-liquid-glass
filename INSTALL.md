# premium-liquid-glass - install and use

## What it is
An Agent Skill (SKILL.md format, open specification at https://agentskills.io/specification) that teaches an AI coding/design agent to create premium, Apple-inspired liquid glass interfaces across platforms (Android Jetpack Compose, iOS SwiftUI, Flutter, and Web with CSS/Tailwind), generate reference-image prompts, and audit UI for glass misuse.

## Installation
- **Google Antigravity / Gemini CLI (Global)**:
  Place this folder at `~/.gemini/config/skills/premium-liquid-glass/`.
- **Project-Specific (.agents)**:
  Copy this folder to `.agents/skills/premium-liquid-glass/` in your repository root.
- **Claude Code**:
  Copy the folder to `.claude/skills/premium-liquid-glass/` (project) or `~/.claude/skills/premium-liquid-glass/` (personal).
- **Other Agent Skills implementations**:
  Place the folder in your agent tool's skills directory. The folder name must match the skill name (`premium-liquid-glass`).

Reload your agent session after installing.

## Usage Examples
- *"Use premium-liquid-glass to design a floating navigation bar and header in SwiftUI."*
- *"Use premium-liquid-glass to implement liquid glass sheets and floating controls in Jetpack Compose."*
- *"Use premium-liquid-glass to create a glass navbar with Tailwind CSS and ensure reduced-transparency fallback."*
- *"Use premium-liquid-glass to build a liquid glass modal bottom sheet in Flutter."*
- *"Use premium-liquid-glass to write image prompts for our mobile app screens in light and dark mode."*
- *"Use premium-liquid-glass to audit our app UI for glass misuse and check color contrast."*

The agent presents a layer map before making UI changes, verifies text contrast against worst-case backdrops, and checks performance budgets (<= 2-3 simultaneous blurred layers).

## Structure
- `SKILL.md`: Main instructions and rules
- `assets/`: Design tokens (`tokens.json`), Compose theme (`GlassTheme.kt`), SwiftUI view (`GlassView.swift`), Flutter theme (`glass_theme.dart`), Web CSS (`glass.css`, `tailwind-glass.config.js`)
- `references/`: Platform guides (`compose-implementation.md`, `swiftui-implementation.md`, `flutter-implementation.md`, `web-implementation.md`), spatial principles (`principles.md`), component specifications (`components.md`), image prompts (`image-prompts.md`), and quality checklists (`review-checklist.md`)
- `scripts/`: Local validation scripts (`contrast_check.py`, `audit_compose.py`)
- `examples/`: Sample test files

## Security Note
This skill contains two local Python helper scripts with no network dependencies or package installations. They only inspect local files passed as arguments.
