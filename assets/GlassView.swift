// GlassView.swift - TEMPLATE for Apple-inspired liquid glass UI in SwiftUI (iOS 15+)
import SwiftUI

public enum GlassTier {
    case thin
    case regular
    case thick

    public var material: Material {
        switch self {
        case .thin: return .ultraThinMaterial
        case .regular: return .thinMaterial
        case .thick: return .thickMaterial
        }
    }
}

public struct GlassSurface<Content: View>: View {
    public let tier: GlassTier
    public let cornerRadius: CGFloat
    @ViewBuilder public let content: () -> Content

    @Environment(\.accessibilityReduceTransparency) private var reduceTransparency
    @Environment(\.colorScheme) private var colorScheme

    public init(
        tier: GlassTier = .regular,
        cornerRadius: CGFloat = 24,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.tier = tier
        self.cornerRadius = cornerRadius
        self.content = content
    }

    public var body: some View {
        content()
            .background {
                if reduceTransparency {
                    RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                        .fill(colorScheme == .dark ? Color(red: 0.086, green: 0.114, blue: 0.188) : Color(red: 0.945, green: 0.953, blue: 0.976))
                        .overlay {
                            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                                .strokeBorder(colorScheme == .dark ? Color.white.opacity(0.12) : Color.black.opacity(0.08), lineWidth: 1)
                        }
                } else {
                    RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                        .fill(tier.material)
                        .overlay {
                            RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                                .strokeBorder(
                                    LinearGradient(
                                        colors: [
                                            Color.white.opacity(colorScheme == .dark ? 0.25 : 0.65),
                                            Color.white.opacity(colorScheme == .dark ? 0.05 : 0.15)
                                        ],
                                        startPoint: .top,
                                        endPoint: .bottom
                                    ),
                                    lineWidth: 1
                                )
                        }
                        .shadow(
                            color: Color.black.opacity(colorScheme == .dark ? 0.35 : 0.12),
                            radius: 24,
                            x: 0,
                            y: 10
                        )
                }
            }
    }
}

