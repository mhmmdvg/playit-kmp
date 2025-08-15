//
//  AuthenticationViewModel.swift
//  iosApp
//
//  Created by Muhammad Vikri on 08/08/25.
//

import Foundation
import AuthenticationServices
import Combine
import Shared

enum AuthError: Error, LocalizedError {
    case invalidCallback
    case tokenExchangeFailed
    case userCancelled
    case custom(String)
    
    var errorDescription: String? {
        switch self {
        case .invalidCallback:
            return "Invalid authorization callback"
        case .tokenExchangeFailed:
            return "Failed to exchange authorization code for token"
        case .userCancelled:
            return "Authentication was cancelled"
        case .custom(let message):
            return message
        }
    }
}

class AuthenticationViewModel: NSObject, ObservableObject, ASWebAuthenticationPresentationContextProviding {
    
    private var subscriptions = [AnyCancellable]()
    private let authenticationRepository: AuthenticationRepository
    
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var isAuthenticated = false

    init(authenticationRepository: AuthenticationRepository) {
        self.authenticationRepository = authenticationRepository
        super.init()
        checkAuthenticationStatus()
    }

    func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        return ASPresentationAnchor()
    }

    func signIn() {
        isLoading = true
        errorMessage = nil

        let signInPromise = Future<String, Error> { completion in
            
            let authUrl = URL(string: SpotifyConfig.shared.AUTH_URL + SpotifyConfig.shared.URL_PARAMS)!
            
            let authSession = ASWebAuthenticationSession(url: authUrl, callbackURLScheme: "playit") { url, error in
                if let error = error {
                    
                    if let authError = error as? ASWebAuthenticationSessionError,
                       authError.code == .canceledLogin {
                        self.isLoading = false
                        return
                    } else {
                        completion(.failure(error))
                    }
                } else if let url = url,
                          let code = URLComponents(string: url.absoluteString)?.queryItems?.first(where: { $0.name == "code" })?.value {
                              completion(.success(code))
                } else {
                    completion(.failure(AuthError.invalidCallback))
                }
            }
            
            authSession.presentationContextProvider = self
            authSession.prefersEphemeralWebBrowserSession = true
            authSession.start()
        }
        
        signInPromise
             .flatMap { code in
                 Future<Bool, Error> { completion in
                     self.authenticationRepository.exchangeCodeForToken(
                         code: code,
                         completion: { result in
                             
                             DispatchQueue.main.async {
                                 if result is ResourceSuccess {
                                     completion(.success(true))
                                 } else if result is ResourceError {
                                     let errorMsg = result.message ?? "Authentication failed"
                                     completion(.failure(AuthError.custom(errorMsg)))
                                 } else {
                                     completion(.failure(AuthError.tokenExchangeFailed))
                                 }
                             }
                         }
                     )
                 }
             }
             .sink { [weak self] completion in
                 DispatchQueue.main.async {
                     self?.isLoading = false
                     
                     switch completion {
                     case .failure(let error):
                         if case AuthError.userCancelled = error {
                             self?.errorMessage = nil
                         } else {
                             self?.errorMessage = error.localizedDescription
                         }
                     case .finished:
                         break
                     }
                 }
             } receiveValue: { [weak self] success in
                 if success {
                     self?.checkAuthenticationStatus()
                 }
             }
             .store(in: &subscriptions)
    }
    
    
    func checkAuthenticationStatus() {
        isAuthenticated = authenticationRepository.isUserLoggedIn()
    }
    
    func getAccessToken() -> String? {
        return authenticationRepository.getAccessToken()
    }
    
    func logout() {
        authenticationRepository.logout()
        checkAuthenticationStatus()
    }


}
