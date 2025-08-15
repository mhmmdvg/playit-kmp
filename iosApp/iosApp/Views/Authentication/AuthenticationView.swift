//
//  AuthenticationView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 07/08/25.
//

import SwiftUI
import Shared

struct AuthenticationView: View {
    @EnvironmentObject private var authViewModel: AuthenticationViewModel
    @State private var showingAlert = false
    
    
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
                authViewModel.signIn()
            }) {
                HStack(spacing: 12) {
                    if authViewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                            .scaleEffect(0.8)
                    } else {
                        Image(systemName: "music.note")
                            .font(.title2)
                    }
                    
                    Text(authViewModel.isLoading ? "Signing in..." : "Continue with Spotify")
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
            .animation(.easeInOut(duration: 0.1), value: authViewModel.isLoading)
        }
        .padding(.horizontal, 32)
        .padding(.vertical, 48)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(.systemBackground))
        .onAppear {
            authViewModel.checkAuthenticationStatus()
        }
        .alert("Authentication Error", isPresented: $showingAlert) {
            Button("OK", role: .cancel) {
                authViewModel.errorMessage = nil
            }
        } message: {
            Text(authViewModel.errorMessage ?? "An unknown error occurred")
        }
        .onChange(of: authViewModel.errorMessage, { _, errorMessage in
            showingAlert = errorMessage != nil
        })
    }
}

//struct AuthenticationView_Previews: PreviewProvider {
//    static var previews: some View {
//        // Mock dependencies for preview
//        let mockTokenManager = TokenManager()
//        let mockHttpClient = .shared.httpClient // You'll need to implement this
//        let mockAuthRepository = AuthenticationRepository(tokenManager: mockTokenManager, httpClient: mockHttpClient)
//        
//        AuthenticationView(authRepository: mockAuthRepository) {
//            print("Authentication successful!")
//        }
//        .preferredColorScheme(.light)
//        
//        AuthenticationView(authRepository: mockAuthRepository) {
//            print("Authentication successful!")
//        }
//        .preferredColorScheme(.dark)
//    }
//}
