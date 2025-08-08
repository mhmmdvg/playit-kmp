//
//  HomeView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 08/08/25.
//

import SwiftUI

struct HomeView: View {
    @Environment(\.authRepository) private var authRepository
    
    var body: some View {
           NavigationView {
               VStack {
                   Text("Welcome to the main app!")
                       .font(.title)
                       .padding()
                   
                    Button("Sign Out") {
                        authRepository.logout()
                        // You might want to notify ContentView to update isAuthenticated
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
