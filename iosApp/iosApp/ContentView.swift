import SwiftUI
import Shared

struct ContentView: View {
    @EnvironmentObject private var authenticationViewModel: AuthenticationViewModel
    
    var body: some View {
        ZStack {
            if authenticationViewModel.isAuthenticated {
                HomeView(albumsRepository: KoinHelper.companion.shared.provdeAlbumsRepository())
                    .transition(.asymmetric(
                        insertion: .move(edge: .bottom).combined(with: .opacity),
                        removal: .move(edge: .top).combined(with: .opacity)
                    ))
                    .zIndex(1)
            } else {
                AuthenticationView()
                    .zIndex(0)
            }
        }
        .animation(.spring(response: 0.6, dampingFraction: 0.8), value: authenticationViewModel.isAuthenticated)
        .onAppear {
            authenticationViewModel.checkAuthenticationStatus()
        }
    }
    
    
}
