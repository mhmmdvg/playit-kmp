//
//  LibraryVIew.swift
//  iosApp
//
//  Created by Muhammad Vikri on 03/10/25.
//

import SwiftUI
import Shared

struct LibraryView: View {
    
    @StateObject private var currentPlaylistVm: CurrentPlaylistsViewModelWrapper
    
    @State private var lastScrollOffset: CGFloat = 0
    
    init(currentPlaylistRepository: PlaylistsRepositoryImpl) {
        self._currentPlaylistVm = StateObject(wrappedValue: CurrentPlaylistsViewModelWrapper(playlistsRepository: currentPlaylistRepository))
    }
    
    
    var body: some View {
        ZStack(alignment: .top) {
            CustomNavigationView(title: "Library", profileRepository: KoinHelper.companion.shared.provideProfileRepository())
                .opacity(lastScrollOffset > -50 ? 0 : 1)
                .animation(.easeInOut(duration: 0.3), value: lastScrollOffset)
            
            GradientAppBar()
            
            ScrollView {
                LazyVStack(alignment: .leading) {
                    Text("Check")
                }
            }
        }
    }
}
