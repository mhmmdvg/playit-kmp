//
//  HomeView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 08/08/25.
//

import SwiftUI

struct HomeView: View {
    @EnvironmentObject private var authStateManager: AuthenticationStateManager

    
    var body: some View {
           NavigationView {
               VStack {
                   Text("Welcome to the main app!")
                       .font(.title)
                       .padding()
                   
                   VStack {
                       Text("Debug Info:")
                           .font(.headline)
                       
                       Text("Logged in: \(authStateManager.isAuthenticated ? "Yes" : "No")")
                       
                       if let token = authStateManager.getAccessToken() {
                           Text("Token: \(token.prefix(20))...")
                               .font(.caption)
                               .foregroundColor(.green)
                       } else {
                           Text("Token: Not found")
                               .font(.caption)
                               .foregroundColor(.red)
                       }
                   }
                   .padding()
                   .background(Color.gray.opacity(0.1))
                   .cornerRadius(8)
                   
                    Button("Sign Out") {
                        authStateManager.logout()
                        
                    }
                    .padding()
                    .background(Color.red)
                    .foregroundColor(.white)
                    .cornerRadius(8)
               }
               .navigationTitle("Home")
           }
       }
}
