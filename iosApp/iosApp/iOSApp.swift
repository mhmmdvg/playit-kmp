import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var authenticationViewModel: AuthenticationViewModel
    
    init() {
        KoinBridge().doInitKoin()
        
        let container = DependencyContainer.shared
        self._authenticationViewModel = StateObject(wrappedValue: AuthenticationViewModel(authenticationRepository: KoinHelper.companion.shared.provideAuthenticationRepository()))
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(authenticationViewModel)
        }
    }
}
