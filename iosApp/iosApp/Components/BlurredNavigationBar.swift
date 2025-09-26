//
//  BlurNavigationView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 24/09/25.
//

import SwiftUI

struct BlurredNavigationBar: View {
    let title: String
    let imageUrl: String
    let opacity: Double
    
    var body: some View {
        VStack(spacing: 0) {
            // Status bar background
            Color.clear
                .frame(height: UIApplication.shared.statusBarHeight)
            
            // Navigation bar content
            HStack {
                Text(title)
                    .font(.title2)
                    .fontWeight(.semibold)
                
                Spacer()
                
                AsyncImage(url: URL(string: imageUrl.isEmpty ? "https://i.scdn.co/image/ab67616d00001e02bc1028b7e9cd2b17c770a520" : imageUrl)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay(
                            Image(systemName: "person.fill")
                                .foregroundColor(.gray)
                        )
                }
                .frame(width: 32, height: 32)
                .clipShape(Circle())
            }
            .padding(.horizontal)
            .padding(.vertical, 8)
        }
        .background(
            // Blur effect
            Rectangle()
                .fill(.ultraThinMaterial)
                .ignoresSafeArea(.all, edges: .top)
        )
        .opacity(opacity)
    }
}
