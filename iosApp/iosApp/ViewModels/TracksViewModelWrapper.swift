//
//  TracksViewModelWrapper.swift
//  iosApp
//
//  Created by Muhammad Vikri on 17/08/25.
//

import Combine
import Shared

@MainActor
class TracksViewModelWrapper: ObservableObject {
    private let viewModel: TracksViewModel
    
    @Published var severalTracks: Resource<SeveralTracks> = ResourceSuccess(data: nil)
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var severalTracksData: SeveralTracks? = nil
    
    private var closeable: FlowCloseable?
    
    init(tracksRepository: TracksRepository) {
        self.viewModel = TracksViewModel(tracksRepository: tracksRepository)
        
        startObserving()
    }
    
    func getSeveralTracks() {
        print("Run 🚀")
        viewModel.getSeveralTracks()
    }
    
    private func startObserving() {
        closeable = viewModel.severalTracksFlow.watch(block: { [weak self] state in
            DispatchQueue.main.async {
                guard let self = self, let state = state else {
                    return
                }
                
                self.severalTracks = state
                self.updateUIState(from: state)
            }
        })
    }
    
    private func updateUIState(from resource: Resource<SeveralTracks>) {
        isLoading = false
        errorMessage = nil
        severalTracksData = nil
        
        if resource is ResourceLoading {
            isLoading = true
        } else if let successResource = resource as? ResourceSuccess<SeveralTracks> {
            print("CheckSSS \(String(describing: successResource.data))")
            severalTracksData = successResource.data
        } else if let errorResource = resource as? ResourceError<SeveralTracks> {
            errorMessage = errorResource.message
        }
    }
    
}
