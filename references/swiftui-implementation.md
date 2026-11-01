# SwiftUI (iOS) Implementation Guide

Native iOS implementation for Apple-inspired liquid glass interfaces using SwiftUI.

## Architecture & Layering

```
ZStack(alignment: .bottom) {
    // 1. Content Layer (ScrollView with opaque cards)
    ScrollView {
        VStack(spacing: 16) {
            // Opaque content cards & lists
        }
        .padding(.bottom, 96) // Space for floating glass bar
    }

    // 2. Floating Layer (Glass controls overlay)
    GlassNavigationBar(...)
        .padding(.horizontal, 20)
        .padding(.bottom, 16)
}
```

### Core Rules
- Glass belongs ONLY to the floating layer: floating navigation bar, collapsed navigation bar, FAB, action sheets, search bar pill, toasts.
- Content layer cards, rows, form inputs, and list cells must remain opaque (`Color(uiColor: .secondarySystemBackground)` or custom opaque tokens).
- Never place Material inside a Material (no glass-on-glass).

## SwiftUI Material Tiers

| Tier | SwiftUI Material | Usage | Fallback (Reduce Transparency) |
|---|---|---|---|
| **Thin** | `.ultraThinMaterial` | Floating chips, small badges, icon-only controls | `Color(uiColor: .secondarySystemBackground)` |
| **Regular** | `.thinMaterial` / `.regularMaterial` | Floating bottom navigation pill, top bar, search pill | `Color(uiColor: .systemBackground)` with 1pt border |
| **Thick** | `.thickMaterial` | Sheets, action sheets, dialogs, full overlays | `Color(uiColor: .systemBackground)` |

## GlassSurface Component Pattern

```swift
import SwiftUI

enum GlassTier {
    case thin, regular, thick

    var material: Material {
        switch self {
        case .thin: return .ultraThinMaterial
        case .regular: return .thinMaterial
        case .thick: return .thickMaterial
        }
    }
}

struct GlassSurface<Content: View>: View {
    let tier: GlassTier
    let cornerRadius: CGFloat
    @ViewBuilder let content: () -> Content

    @Environment(\.accessibilityReduceTransparency) private var reduceTransparency
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        content()
            .background {
                if reduceTransparency {
                    RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                        .fill(colorScheme == .dark ? Color(hex: 0x161D30) : Color(hex: 0xF1F3F9))
                        .overlay {
                            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                                .strokeBorder(colorScheme == .dark ? Color.white.opacity(0.12) : Color.black.opacity(0.1), lineWidth: 1)
                        }
                } else {
                    RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                        .fill(tier.material)
                        .overlay {
                            // Dual-tone edge highlight: top light reflection, bottom soft edge
                            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                                .strokeBorder(
                                    LinearGradient(
                                        colors: [
                                            Color.white.opacity(colorScheme == .dark ? 0.25 : 0.60),
                                            Color.white.opacity(colorScheme == .dark ? 0.05 : 0.20)
                                        ],
                                        startPoint: .top,
                                        endPoint: .bottom
                                    ),
                                    lineWidth: 1
                                )
                        }
                        .shadow(
                            color: Color.black.opacity(colorScheme == .dark ? 0.35 : 0.12),
                            radius: 20,
                            x: 0,
                            y: 10
                        )
                }
            }
    }
}
```

## Floating Navigation Pill

```swift
struct FloatingNavPill: View {
    @Binding var selectedTab: Int

    var body: some View {
        GlassSurface(tier: .regular, cornerRadius: 28) {
            HStack(spacing: 24) {
                TabItem(icon: "house.fill", label: "Today", isSelected: selectedTab == 0) { selectedTab = 0 }
                TabItem(icon: "clock.fill", label: "Timeline", isSelected: selectedTab == 1) { selectedTab = 1 }
                TabItem(icon: "square.grid.2x2.fill", label: "Library", isSelected: selectedTab == 2) { selectedTab = 2 }
                TabItem(icon: "sparkles", label: "Ask", isSelected: selectedTab == 3) { selectedTab = 3 }
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 12)
        }
        .frame(height: 64)
    }
}
```

## Modal Sheets (iOS 16.4+)

In iOS 16.4+, use `.presentationBackground` to style modal sheets:

```swift
.sheet(isPresented: $showSheet) {
    SheetContent()
        .presentationDetents([.medium, .large])
        .presentationBackground(.thinMaterial)
        .presentationCornerRadius(32)
}
```

## Accessibility & Performance
1. **Reduce Transparency**: Always read `@Environment(\.accessibilityReduceTransparency)`. When true, swap out `.fill(material)` for an opaque solid background with a high-contrast 1pt border.
2. **Dynamic Type**: All text must support Dynamic Type (`.font(.system(.body, design: .default))`).
3. **Materials Budget**: Keep materials limited to 1-2 simultaneous floating elements on screen. Never use Materials in `List` items, `LazyVStack` elements, or table rows.

