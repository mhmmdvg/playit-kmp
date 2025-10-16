//
//  GradientAppBar.swift
//  iosApp
//
//  Created by Muhammad Vikri on 03/10/25.
//

import SwiftUI

struct GradientAppBar: View {
    @Environment(\.dismiss) var dismiss
    var showBackButton: Bool = false
    var title: String? = nil
    
    var body: some View {
        VStack {
            ZStack {
                // Gradient background
                LinearGradient(
                    gradient: Gradient(stops: [
                        .init(color: Color(.systemBackground), location: 0),
                        .init(color: Color(.systemBackground).opacity(0.8), location: 0.3),
                        .init(color: Color.clear, location: 1)
                    ]),
                    startPoint: .top,
                    endPoint: .bottom
                )
                .frame(height: showBackButton ? 120 : 52)
                
                // Content
                HStack {
                    if showBackButton {
                        Button {
                            dismiss()
                        } label: {
                            Image(systemName: "xmark")
                                .font(.system(size: 18, weight: .semibold))
                                .foregroundColor(.primary)
                                .frame(width: 40, height: 40)
                                .background(Color(.systemGray6).opacity(0.8))
                                .clipShape(.circle)
                        }
                        .padding(.leading, 16)
                    }
                    
                    Spacer()
                    
                    if let title = title {
                        Text(title)
                            .font(.system(size: 17, weight: .semibold))
                            .foregroundColor(.primary)
                    }
                    
                    Spacer()
                    
                    // Invisible spacer to balance the back button
                    if showBackButton {
                        Color.clear
                            .frame(width: 44, height: 44)
                            .padding(.trailing, 16)
                    }
                }
                .padding(.top, 16)
            }
            
            Spacer()
        }
        .ignoresSafeArea(.container, edges: .top)
    }
}
