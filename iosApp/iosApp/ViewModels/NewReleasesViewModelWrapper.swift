//
//  NewReleasesViewModelWrapper.swift
//  iosApp
//
//  Created by Muhammad Vikri on 14/08/25.
//

import Combine
import Shared

@MainActor
class NewReleasesViewModelWrapper: ObservableObject {
    private let viewModel: NewReleasesViewModel
    
    @Published var newReleases: Resource<NewReleasesResponse> = ResourceSuccess(data: nil)
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var newReleasesData: NewReleasesResponse? = nil
    
    private var closeable: FlowCloseable?
    
    init(albumsRepository: AlbumsRepository) {
        self.viewModel = NewReleasesViewModel(albumsRepository: albumsRepository)
        
        startObserving()
    }
    
    deinit {
        closeable?.close()
    }
    
    func clearError() {
        errorMessage = nil
    }
    
    func retry() {
        clearError()
        getNewReleases()
    }
    
    private func startObserving() {
        closeable = viewModel.newReleasesFlow.watch(block: { [weak self] state in
            DispatchQueue.main.async {
                guard let self = self, let state = state else {
                    return
                }
                
                self.newReleases = state
                self.updateUIState(from: state)
            }
        })
    }
    
    func getNewReleases() {
        viewModel.getNewReleases()
    }
    
    private func updateUIState(from resource: Resource<NewReleasesResponse>) {
        isLoading = false
        errorMessage = nil
        newReleasesData = nil
        
        if resource is ResourceLoading {
            isLoading = true
        } else if let successResource = resource as? ResourceSuccess<NewReleasesResponse> {
            print("Resource Data \(String(describing: successResource.data))")
            newReleasesData = successResource.data
        } else if let errorResource = resource as? ResourceError<NewReleasesResponse> {
            errorMessage = errorResource.message
        }
    }
}
