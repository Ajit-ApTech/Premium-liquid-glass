#!/usr/bin/env python3
"""WCAG contrast checker for text/icons on translucent glass.

Glass hides an unknown backdrop, so the check is done against worst-case backdrops
(default: pure black, pure white and mid grey) BEFORE blur. Blur averages extremes, so
real results are usually better, but a pass here is a safe floor.

Usage:
  python scripts/contrast_check.py --text "#FFFFFF" --glass "#0F1526@0.62"
  python scripts/contrast_check.py --text "#0A0E1A" --glass "#FFFFFF@0.72" --min 4.5
  python scripts/contrast_check.py --tokens assets/tokens.json      # check every tier in both modes

Colour syntax: #RRGGBB or #RRGGBB@alpha (alpha 0..1). Text may also carry alpha.
Exit code 0 = all pass, 1 = at least one failure, 2 = bad input.
"""
import argparse, json, sys

def parse(c):
    c = c.strip()
    alpha = 1.0
    if "@" in c:
        c, a = c.split("@", 1)
        alpha = float(a)
    c = c.lstrip("#")
    if len(c) != 6:
        raise ValueError(f"bad colour: {c}")
    rgb = tuple(int(c[i:i+2], 16) for i in (0, 2, 4))
    if not 0.0 <= alpha <= 1.0:
        raise ValueError("alpha must be between 0 and 1")
    return rgb, alpha

def over(fg, alpha, bg):
    return tuple(alpha * f + (1 - alpha) * b for f, b in zip(fg, bg))

def lin(v):
    v /= 255.0
    return v / 12.92 if v <= 0.03928 else ((v + 0.055) / 1.055) ** 2.4

def lum(rgb):
    r, g, b = (lin(x) for x in rgb)
    return 0.2126 * r + 0.7152 * g + 0.0722 * b

def ratio(a, b):
    la, lb = lum(a), lum(b)
    hi, lo = max(la, lb), min(la, lb)
    return (hi + 0.05) / (lo + 0.05)

def check(text, glass, backdrops, minimum):
    t_rgb, t_a = parse(text)
    g_rgb, g_a = parse(glass)
    rows, ok = [], True
    for bd in backdrops:
        b_rgb, _ = parse(bd)
        surface = over(g_rgb, g_a, b_rgb)
        text_rgb = over(t_rgb, t_a, surface)
        r = ratio(text_rgb, surface)
        passed = r >= minimum
        ok &= passed
        rows.append((bd, tuple(round(x) for x in surface), r, passed))
    return ok, rows

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--text"); ap.add_argument("--glass")
    ap.add_argument("--backdrops", default="#000000,#FFFFFF,#808080")
    ap.add_argument("--min", type=float, default=4.5, help="4.5 body text, 3.0 large text/icons")
    ap.add_argument("--tokens", help="path to tokens.json to check all tiers")
    a = ap.parse_args()
    backs = [b.strip() for b in a.backdrops.split(",") if b.strip()]
    all_ok = True
    try:
        if a.tokens:
            data = json.load(open(a.tokens, encoding="utf-8"))
            for mode, m in data["modes"].items():
                for tier, t in m["tiers"].items():
                    minimum = t.get("minContrast", 4.5)
                    ok, rows = check(m["text"], t["fill"], backs, minimum)
                    all_ok &= ok
                    worst = min(r[2] for r in rows)
                    print(f"{mode:5} {tier:8} text {m['text']:>9} on {t['fill']:>16}  worst {worst:5.2f}:1  need {minimum}  {'PASS' if ok else 'FAIL'}")
        else:
            if not (a.text and a.glass):
                ap.error("provide --text and --glass, or --tokens")
            ok, rows = check(a.text, a.glass, backs, a.min)
            all_ok = ok
            for bd, surf, r, p in rows:
                print(f"backdrop {bd}: surface rgb{surf}  contrast {r:.2f}:1  {'PASS' if p else 'FAIL'} (min {a.min})")
    except (ValueError, KeyError, OSError) as e:
        print("error:", e, file=sys.stderr); sys.exit(2)
    sys.exit(0 if all_ok else 1)

if __name__ == "__main__":
    main()
