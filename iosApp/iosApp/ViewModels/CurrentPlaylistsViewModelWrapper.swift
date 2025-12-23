//
//  CurrentPlaylistsViewModel.swift
//  iosApp
//
//  Created by Muhammad Vikri on 07/08/25.
//

import SwiftUI
import Combine
import Shared

@MainActor
class CurrentPlaylistsViewModelWrapper: ObservableObject {
    private let viewModel: CurrentPlaylistsViewModel
    
    @Published var currentPlaylists: Resource<CurrentPlaylistsResponse> = ResourceSuccess(data: nil)
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var currentPlaylistsData: CurrentPlaylistsResponse? = nil
    
    private var closeable: FlowCloseable?
    
    init(playlistsRepository: PlaylistsRepositoryImpl) {
        self.viewModel = CurrentPlaylistsViewModel(playlistsRepositoryImpl: playlistsRepository)
    
        startObserving()
    }
    
    deinit {
        closeable?.close()
    }
    
    func clearError() {
        errorMessage = nil
    }
    
    func getCurrentPlaylists() {
        viewModel.loadCurrentPlaylists(forceRefresh: false)
    }
    
    func createPlaylist(userId: String, req: CreatePlaylistRequest) {
        viewModel.createPlaylist(userId: userId, request: req)
    }
    
    private func startObserving() {
        closeable = viewModel.currentPlaylistsFlow.watch(block: { [weak self] state in
            DispatchQueue.main.async {
                guard let self = self, let state = state else { return }
                
                self.currentPlaylists = state
                self.updateUIState(from: state)
            }
        })
    }
    
    private func updateUIState(from resource: Resource<CurrentPlaylistsResponse>) {
        isLoading = false
        errorMessage = nil
        currentPlaylistsData = nil
        
        if resource is ResourceLoading {
            isLoading = true
        } else if let successResource = resource as? ResourceSuccess<CurrentPlaylistsResponse> {
            currentPlaylistsData = successResource.data
        } else if let errorResource = resource as? ResourceError<CurrentPlaylistsResponse> {
            errorMessage = errorResource.message
        }
    }
}
