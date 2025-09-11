//
//  MainTabViews.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI

struct MainTabView: View {
    @State private var selectedTab: TabItem = .home
    
    var body: some View {
        TabView(selection: $selectedTab) {
            ForEach(TabItem.allCases, id: \.self) { it in
                Tab(it.rawValue, systemImage: it.systemImage, value: it) {
                    it.view
                }
            }
        }
    }
}
