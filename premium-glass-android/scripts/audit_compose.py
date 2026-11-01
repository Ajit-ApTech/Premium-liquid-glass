#!/usr/bin/env python3
"""Heuristic design-rule scanner for Jetpack Compose projects (premium glass UI).

It cannot understand Kotlin fully; it flags likely violations for a human to review.
Rules
  G1 error  glass used in card / row / item / list files (glass belongs to the floating navigation layer)
  G2 error  glass nested inside glass (brace-depth heuristic)
  G3 warn   Modifier.blur(...) on a composable that has children (it blurs its own content, not the backdrop)
  G4 warn   more than 3 glass uses in one file (blur budget)
  G5 warn   hard-coded Color(0x...) outside theme/token files
  G6 warn   hard-coded .sp font sizes outside typography files
  G7 warn   swipe-to-dismiss without any delete/undo handling nearby (no 'Undo' / 'snackbar' text in file)
  G8 warn   clickable/IconButton without contentDescription hint in the same file (rough check)

Usage: python scripts/audit_compose.py path/to/app/src [--strict]
Exit code: 1 if any error (or any finding with --strict), else 0.
"""
import os, re, sys

GLASS_CALL = re.compile(r"\b(GlassSurface|glassSurface|hazeBlur|hazeGlass|hazeEffect|GlassBar|FloatingGlass\w*|GlassSheet|GlassTopBar)\s*\(")
CARD_FILE = re.compile(r"(card|row|item|cell|tile|listing)", re.I)
THEME_FILE = re.compile(r"(theme|token|color|palette|type|typography)", re.I)

def scan_file(path):
    findings = []
    try:
        text = open(path, encoding="utf-8").read()
    except OSError:
        return findings
    lines = text.splitlines()
    name = os.path.basename(path)
    glass_lines = [i for i, l in enumerate(lines, 1) if GLASS_CALL.search(l) and not l.strip().startswith(("//", "*", "import"))]
    is_def = bool(re.search(r"fun\s+(GlassSurface|glassSurface)\b", text))
    if glass_lines and CARD_FILE.search(name) and not is_def:
        findings.append((glass_lines[0], "G1", "error", f"glass in a card/row/item file ({name}): use opaque surfaces for content"))
    if len(glass_lines) > 3 and not is_def:
        findings.append((glass_lines[3], "G4", "warn", f"{len(glass_lines)} glass uses in one file: check the blur budget"))
    # G2: nested glass via brace depth
    depth, stack = 0, []
    for i, l in enumerate(lines, 1):
        code = l.split("//")[0]
        if GLASS_CALL.search(code) and not is_def:
            if stack:
                findings.append((i, "G2", "error", f"glass nested inside glass (outer at line {stack[-1][0]})"))
            stack.append((i, depth))
        depth += code.count("{") - code.count("}")
        while stack and depth <= stack[-1][1]:
            stack.pop()
    for i, l in enumerate(lines, 1):
        code = l.split("//")[0]
        if "Modifier.blur(" in code or ".blur(" in code and "Modifier" in code:
            findings.append((i, "G3", "warn", "Modifier.blur blurs the element's own content, not what is behind it; use a backdrop blur library for glass"))
        if re.search(r"Color\(\s*0x[0-9A-Fa-f]{6,8}\s*\)", code) and not THEME_FILE.search(name):
            findings.append((i, "G5", "warn", "hard-coded colour: use theme/tokens"))
        if re.search(r"\b\d+(\.\d+)?\.sp\b", code) and not THEME_FILE.search(name):
            findings.append((i, "G6", "warn", "hard-coded font size: use typography tokens"))
    if "SwipeToDismissBox" in text and not re.search(r"Undo|snackbar|Snackbar|trash|Trash", text):
        i = next(i for i, l in enumerate(lines, 1) if "SwipeToDismissBox" in l)
        findings.append((i, "G7", "warn", "swipe-to-dismiss without visible Undo/Trash handling in this file"))
    if re.search(r"\.clickable\(|IconButton\(", text) and "contentDescription" not in text and "semantics" not in text:
        i = next(i for i, l in enumerate(lines, 1) if ".clickable(" in l or "IconButton(" in l)
        findings.append((i, "G8", "warn", "interactive element without any contentDescription/semantics in this file"))
    return findings

def main():
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    strict = "--strict" in sys.argv
    if not args:
        print(__doc__); sys.exit(2)
    errs = warns = 0
    for base in args:
        for dp, _, files in os.walk(base):
            if any(p in dp for p in ("/build", "\\build", "/.git", "/test", "/androidTest")):
                continue
            for f in files:
                if f.endswith(".kt"):
                    p = os.path.join(dp, f)
                    for line, rule, sev, msg in scan_file(p):
                        print(f"{p}:{line}: [{rule}/{sev}] {msg}")
                        errs += sev == "error"; warns += sev == "warn"
    print(f"\n{errs} error(s), {warns} warning(s)")
    sys.exit(1 if errs or (strict and warns) else 0)

if __name__ == "__main__":
    main()
