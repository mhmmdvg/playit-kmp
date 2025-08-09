import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var dependencyContainer = DependencyContainer.shared
    @StateObject private var authStateManager: AuthenticationStateManager
    
    init() {
        KoinBridge().doInitKoin()
        
        let container = DependencyContainer.shared
        self._authStateManager = StateObject(wrappedValue: AuthenticationStateManager(authRepository: container.authRepository))
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(authStateManager)
                .environment(\.authRepository, dependencyContainer.authRepository)
        }
    }
}
