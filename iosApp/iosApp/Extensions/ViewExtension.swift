//
//  ViewExtension.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI

struct SkeletonAnimation: ViewModifier {
    @State private var isAnimating = false
    
    func body(content: Content) -> some View {
        content
            .opacity(isAnimating ? 0.6 : 1.0)
            .animation(
                Animation
                    .easeInOut(duration: 1.0)
                    .repeatForever(autoreverses: true),
                value: isAnimating
            )
            .onAppear {
                isAnimating = true
            }
    }
}

struct ShimmerEffect: ViewModifier {
    @State private var phase: CGFloat = 0
    
    func body(content: Content) -> some View {
        content
            .overlay {
                LinearGradient(
                    gradient: Gradient(colors: [
                        Color.white.opacity(0),
                        Color.white.opacity(0.4),
                        Color.white.opacity(0)
                    ]),
                    startPoint: .leading,
                    endPoint: .leading
                )
                .rotationEffect(.degrees(30))
                .offset(x: phase)
                .animation(
                    Animation.linear
                        .repeatForever(autoreverses: false),
                    value: phase
                )
            }
            .onAppear {
                phase = 300
            }
    }
}

extension View {
    func skeleton() -> some View {
        modifier(SkeletonAnimation())
    }
    
    func shimmer() -> some View {
        modifier(ShimmerEffect())
    }
}
