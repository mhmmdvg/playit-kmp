//
//  GradientAppBar.swift
//  iosApp
//
//  Created by Muhammad Vikri on 03/10/25.
//

import SwiftUI

struct GradientAppBar: View {
    var body: some View {
        VStack {
            LinearGradient(
                gradient: Gradient(stops: [
                    .init(color: Color(.systemBackground), location: 0),
                    .init(color: Color(.systemBackground).opacity(0.8), location: 0.3),
                    .init(color: Color.clear, location: 1)
                ]),
                startPoint: .top,
                endPoint: .bottom
            )
            .frame(height: 56)
            .allowsHitTesting(false)
            
            Spacer()
        }
        .ignoresSafeArea(.container, edges: .top)
    }
}
