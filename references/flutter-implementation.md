# Flutter Implementation Guide

Cross-platform implementation for Apple-inspired liquid glass interfaces in Flutter.

## Architecture & Layering

```dart
Stack(
  children: [
    // 1. Content Layer (ScrollView with opaque cards)
    CustomScrollView(
      slivers: [
        SliverToBoxAdapter(child: ContentSection()),
        // Add bottom padding so content scrolls past floating bar
        const SliverPadding(padding: EdgeInsets.only(bottom: 96)),
      ],
    ),

    // 2. Floating Layer (Glass controls overlay)
    Positioned(
      left: 20,
      right: 20,
      bottom: MediaQuery.paddingOf(context).bottom + 16,
      child: const FloatingNavPill(),
    ),
  ],
)
```

## Material Tiers (Blur Sigma & Fill)

| Tier | Blur Sigma (X/Y) | Fill (Dark) | Fill (Light) | Use Case |
|---|---|---|---|---|
| **Thin** | 16.0 | `Color(0xFF0F1526).withOpacity(0.55)` | `Color(0xFFFFFFFF).withOpacity(0.55)` | Floating action chips, icon buttons |
| **Regular** | 24.0 | `Color(0xFF0F1526).withOpacity(0.66)` | `Color(0xFFFFFFFF).withOpacity(0.70)` | Floating nav bar, top app bar, search bar |
| **Thick** | 32.0 | `Color(0xFF0F1526).withOpacity(0.78)` | `Color(0xFFFFFFFF).withOpacity(0.82)` | Bottom sheets, modal dialogs, drawers |

## GlassSurface Component Pattern

```dart
import 'dart:ui';
import 'package:flutter/material.dart';

enum GlassTier { thin, regular, thick }

extension GlassTierProps on GlassTier {
  double get blur => switch (this) {
    GlassTier.thin => 16.0,
    GlassTier.regular => 24.0,
    GlassTier.thick => 32.0,
  };

  double darkOpacity(bool fallback) => switch (this) {
    GlassTier.thin => fallback ? 1.0 : 0.55,
    GlassTier.regular => fallback ? 1.0 : 0.66,
    GlassTier.thick => fallback ? 1.0 : 0.78,
  };

  double lightOpacity(bool fallback) => switch (this) {
    GlassTier.thin => fallback ? 1.0 : 0.55,
    GlassTier.regular => fallback ? 1.0 : 0.70,
    GlassTier.thick => fallback ? 1.0 : 0.82,
  };
}

class GlassSurface extends StatelessWidget {
  final Widget child;
  final GlassTier tier;
  final double borderRadius;
  final bool? forceFallback;

  const GlassSurface({
    super.key,
    required this.child,
    this.tier = GlassTier.regular,
    this.borderRadius = 24.0,
    this.forceFallback,
  });

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    final reduceTransparency = forceFallback ?? MediaQuery.of(context).accessibleNavigation;

    // Solid fallback for accessibility and low-performance modes
    if (reduceTransparency) {
      return Container(
        decoration: BoxDecoration(
          color: isDark ? const Color(0xFF161D30) : const Color(0xFFF1F3F9),
          borderRadius: BorderRadius.circular(borderRadius),
          border: Border.all(
            color: isDark ? Colors.white.withOpacity(0.12) : Colors.black.withOpacity(0.08),
            width: 1.0,
          ),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(isDark ? 0.35 : 0.10),
              blurRadius: 20,
              offset: const Offset(0, 10),
            ),
          ],
        ),
        child: child,
      );
    }

    // High performance backdrop filter
    return RepaintBoundary(
      child: ClipRRect(
        borderRadius: BorderRadius.circular(borderRadius),
        child: BackdropFilter(
          filter: ImageFilter.blur(sigmaX: tier.blur, sigmaY: tier.blur),
          child: Container(
            decoration: BoxDecoration(
              color: isDark
                  ? const Color(0xFF0F1526).withOpacity(tier.darkOpacity(false))
                  : Colors.white.withOpacity(tier.lightOpacity(false)),
              borderRadius: BorderRadius.circular(borderRadius),
              border: Border.all(
                color: isDark ? Colors.white.withOpacity(0.15) : Colors.white.withOpacity(0.60),
                width: 1.0,
              ),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(isDark ? 0.35 : 0.12),
                  blurRadius: 28,
                  offset: const Offset(0, 10),
                ),
              ],
            ),
            child: child,
          ),
        ),
      ),
    );
  }
}
```

## Performance & Flutter Best Practices
1. **Always wrap `BackdropFilter` in `RepaintBoundary`**: Prevents the backdrop blur layer from repainting whenever unrelated UI changes or ticks.
2. **Never use in `ListView.builder` or `GridView.builder` items**: Content surfaces must remain solid opaque cards. Blur inside scrolling items causes severe GPU frame drops.
3. **Budget**: At most 1-2 `BackdropFilter` instances on screen at any time (e.g., bottom nav pill + top bar).
4. **Platform Checks**: On low-end Android or Flutter Web, support a user setting or automatic fallback to solid surfaces.

