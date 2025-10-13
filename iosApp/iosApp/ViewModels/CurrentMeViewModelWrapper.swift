//
//  CurrentMeViewModelWrapper.swift
//  iosApp
//
//  Created by Muhammad Vikri on 26/09/25.
//

import Combine
import Shared

@MainActor
class CurrentMeViewModelWrapper: ObservableObject {
    private let viewModel: CurrentMeViewModel
    
    @Published var currentMe: Resource<ProfileResponse> = ResourceSuccess(data: nil)
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var currentMeData: ProfileResponse? = nil
    
    private var closeable: FlowCloseable?
    
    init(profileRepository: ProfileRepositoryImpl) {
        self.viewModel = CurrentMeViewModel(profileRepositoryImpl: profileRepository)
        
        startObserving()
    }
    
    deinit {
        closeable?.close()
    }
    
    func getCurrentMe() {
        viewModel.getCurrentMe()
    }
    
    private func startObserving() {
        closeable = viewModel.currentMeFlow.watch(block: { [weak self] state in
            DispatchQueue.main.async {
                guard let self = self, let state = state else {
                    return
                }
                
                self.currentMe = state
                self.updateUIState(from: state)
            }
        })
    }
    
    private func updateUIState(from resource: Resource<ProfileResponse>) {
        isLoading = false
        errorMessage = nil
        currentMeData = nil
        
        if resource is ResourceLoading {
            isLoading = true
        } else if let successResource = resource as? ResourceSuccess<ProfileResponse> {
            currentMeData = successResource.data
        } else if let errorResource = resource as? ResourceError<ProfileResponse> {
            errorMessage = errorResource.message
        }
    }
}

