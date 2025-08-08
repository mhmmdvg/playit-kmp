import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var dependencyContainer = DependencyContainer.shared
    
    init() {
        KoinBridge().doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.authRepository, dependencyContainer.authRepository)
        }
    }
}
