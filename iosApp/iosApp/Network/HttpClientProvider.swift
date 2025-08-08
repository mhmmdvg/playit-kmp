//
//  HttpClientProvider.swift
//  iosApp
//
//  Created by Muhammad Vikri on 09/08/25.
//

import Foundation
import Shared

class HttpClientProvider {
    static let shared = HttpClientProvider()
    
    lazy var tokenManager: TokenManager = {
       return TokenManager()
    }()
    
    lazy var httpClient: Ktor_client_coreHttpClient = {
        return KtorClientProvider_iosKt.provideHttpClient(tokenManager: tokenManager)
    }()
    
    private init() {}

}
