//
//  AuthenticationStateManager.swift
//  iosApp
//
//  Created by Muhammad Vikri on 09/08/25.
//

import Foundation
import Shared

class AuthenticationStateManager : ObservableObject {
    @Published var isAuthenticated = false
    private var authRepository: AuthenticationRepository
    
    init(authRepository: AuthenticationRepository) {
        self.authRepository = authRepository
    
        checkAuthenticationStatus()
    }
    
    func checkAuthenticationStatus() {
        isAuthenticated = authRepository.isUserLoggedIn()
    }
    
    func login() {
        isAuthenticated = true
    }
    
    func getAccessToken() -> String? {
        return authRepository.getAccessToken()
    }
    
    func logout() {
        authRepository.logout()
        isAuthenticated = false
        
        DispatchQueue.main.async {
            self.checkAuthenticationStatus()
        }
    }
}
