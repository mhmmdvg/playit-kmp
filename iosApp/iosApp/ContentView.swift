import SwiftUI
import Shared

struct ContentView: View {
    @EnvironmentObject private var authStateManager: AuthenticationStateManager
    @Environment(\.authRepository) private var authRepository
    
    var body: some View {
        Group {
            if authStateManager.isAuthenticated {
                HomeView()
            } else {
                AuthenticationView(authRepository: authRepository) {
                    authStateManager.login()
                }
            }
        }
        .onAppear {
            authStateManager.checkAuthenticationStatus()
        }
    }
    
    
}
