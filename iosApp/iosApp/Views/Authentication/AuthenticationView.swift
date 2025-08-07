//
//  AuthenticationView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 07/08/25.
//

import SwiftUI

struct AuthenticationView: View {
    var body: some View {
        VStack(spacing: 24) {
            // Header Section
            VStack(spacing: 8) {
                Text("Welcome Back")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                
                Text("Login with your Spotify Account")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }
            
            // Divider Line
            Rectangle()
                .fill(Color.gray.opacity(0.3))
                .frame(height: 1)
                .padding(.horizontal, 20)
            
            // Button Section
            Button(action: {
                print("Spotify login tapped")
            }) {
                HStack(spacing: 12) {
                    Image(systemName: "music.note")
                        .font(.title2)
                    
                    Text("Continue with Spotify")
                        .fontWeight(.semibold)
                        .font(.body)
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 54)
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [Color.green, Color.green.opacity(0.8)]),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .cornerRadius(16)
                .shadow(color: .green.opacity(0.3), radius: 8, x: 0, y: 4)
            }
            .buttonStyle(PlainButtonStyle())
            .scaleEffect(1.0)
            .animation(.easeInOut(duration: 0.1), value: false)
        }
        .padding(.horizontal, 32)
        .padding(.vertical, 48)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
    }
}

struct AuthenticationView_Previews: PreviewProvider {
    static var previews: some View {
        AuthenticationView()
            .preferredColorScheme(.light)
        
        AuthenticationView()
            .preferredColorScheme(.dark)
    }
}
