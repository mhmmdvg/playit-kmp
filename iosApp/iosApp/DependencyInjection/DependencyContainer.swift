//
// Created by Muhammad Vikri on 09/08/25.
//

import Foundation
import Shared
import SwiftUICore

class DependencyContainer: ObservableObject {
    static let shared = DependencyContainer()
    
    lazy var authRepository: AuthenticationRepository = {
        return AuthenticationRepository(tokenManager: HttpClientProvider.shared.tokenManager, httpClient: HttpClientProvider.shared.httpClient)
    }()
    
    private init() {}
}

private struct AuthRepositoryKey: EnvironmentKey {
    static let defaultValue: AuthenticationRepository = DependencyContainer.shared.authRepository
}

extension EnvironmentValues {
    var authRepository: AuthenticationRepository {
         get { self[AuthRepositoryKey.self] }
         set { self[AuthRepositoryKey.self] = newValue }
     }
}
