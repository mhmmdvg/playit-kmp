import SwiftUI
import Shared

struct ContentView: View {
    @Environment(\.authRepository) private var authRepository
    @State private var isAuthenticated = false
    
    private func checkAuthenticationStatus() {
        isAuthenticated = authRepository.isUserLoggedIn()
    }
    
    var body: some View {
        Group {
            if isAuthenticated {
                HomeView()
            } else {
                AuthenticationView(authRepository: authRepository) {
                    isAuthenticated = true
                }
            }
        }
        .onAppear {
            checkAuthenticationStatus()
        }
    }
    
    
}
