import SwiftUI
import Shared

@main
struct iOSApp: App {
    @StateObject private var authenticationViewModel: AuthenticationViewModel
    
    init() {
        KoinBridgeKt.doInitKoin(platformModules: nil)
        
        self._authenticationViewModel = StateObject(wrappedValue: AuthenticationViewModel(authenticationRepository: KoinHelper.companion.shared.provideAuthenticationRepository()))
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(authenticationViewModel)
        }
    }
}
