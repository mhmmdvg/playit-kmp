//
//  TabItem.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI
import Shared

enum TabItem: String, CaseIterable {
    case home = "Home"
    case search = "Search"
    case library = "Library"
    
    var systemImage: String {
        switch self {
        case .home: return "house.fill"
        case .search: return "magnifyingglass"
        case .library: return "rectangle.stack"
        }
    }
    
    @ViewBuilder
    var view: some View {
        switch self {
        case .home:
            HomeView(albumsRepository: KoinHelper.companion.shared.provideAlbumsRepository(), currentPlaylistsRepository: KoinHelper.companion.shared.providePlaylistsRepository())
        case .search:
            SearchView()
        case .library:
            LibraryView(currentPlaylistRepository: KoinHelper.companion.shared.providePlaylistsRepository(), currentMeRepository: KoinHelper.companion.shared.provideProfileRepository())
        }
    }
}
