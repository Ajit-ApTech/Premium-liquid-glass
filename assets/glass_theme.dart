// glass_theme.dart - TEMPLATE for Apple-inspired liquid glass UI in Flutter (3.0+)
import 'dart:ui';
import 'package:flutter/material.dart';

enum GlassTier { thin, regular, thick }

extension GlassTierExtension on GlassTier {
  double get blurSigma => switch (this) {
    GlassTier.thin => 16.0,
    GlassTier.regular => 24.0,
    GlassTier.thick => 32.0,
  };

  double get darkOpacity => switch (this) {
    GlassTier.thin => 0.55,
    GlassTier.regular => 0.66,
    GlassTier.thick => 0.78,
  };

  double get lightOpacity => switch (this) {
    GlassTier.thin => 0.55,
    GlassTier.regular => 0.70,
    GlassTier.thick => 0.82,
  };
}

class GlassSurface extends StatelessWidget {
  final Widget child;
  final GlassTier tier;
  final double borderRadius;
  final EdgeInsetsGeometry? padding;
  final bool? reduceTransparency;

  const GlassSurface({
    super.key,
    required this.child,
    this.tier = GlassTier.regular,
    this.borderRadius = 24.0,
    this.padding,
    this.reduceTransparency,
  });

  @override
  Widget build(BuildContext context) {
    final isDark = Theme.of(context).brightness == Brightness.dark;
    final fallback = reduceTransparency ?? MediaQuery.of(context).accessibleNavigation;

    if (fallback) {
      return Container(
        padding: padding,
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
              blurRadius: 18,
              offset: const Offset(0, 8),
            ),
          ],
        ),
        child: child,
      );
    }

    return RepaintBoundary(
      child: ClipRRect(
        borderRadius: BorderRadius.circular(borderRadius),
        child: BackdropFilter(
          filter: ImageFilter.blur(sigmaX: tier.blurSigma, sigmaY: tier.blurSigma),
          child: Container(
            padding: padding,
            decoration: BoxDecoration(
              color: isDark
                  ? const Color(0xFF0F1526).withOpacity(tier.darkOpacity)
                  : Colors.white.withOpacity(tier.lightOpacity),
              borderRadius: BorderRadius.circular(borderRadius),
              border: Border.all(
                color: isDark ? Colors.white.withOpacity(0.15) : Colors.white.withOpacity(0.65),
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

