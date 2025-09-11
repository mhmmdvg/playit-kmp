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
    case profile = "Profile"
    
    var systemImage: String {
        switch self {
        case .home: return "house.fill"
        case .search: return "magnifyingglass"
        case .profile: return "person.fill"
        }
    }
    
    @ViewBuilder
    var view: some View {
        switch self {
        case .home:
            HomeView(albumsRepository: KoinHelper.companion.shared.provideAlbumsRepository())
        case .search:
            SearchView()
        case .profile:
            ProfileView()
        }
    }
}
