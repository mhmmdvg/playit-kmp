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
            ScrollView {
                StateWrapper(resource: currentPlaylistVm.currentPlaylists) {
                    VStack(spacing: 12) {
                        ProgressView()
                        Text("Loading your playlists...")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 40)
                } onFailure: { error in
                    VStack(spacing: 16) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .font(.largeTitle)
                            .foregroundColor(.red)
                        
                        Text(error ?? "Failed to load playlists")
                            .font(.body)
                            .multilineTextAlignment(.center)
                            .foregroundColor(.secondary)
                        
                        Button(action: {
                            currentPlaylistVm.getCurrentPlaylists()
                        }) {
                            Text("Retry")
                                .fontWeight(.semibold)
                                .padding(.horizontal, 24)
                                .padding(.vertical, 10)
                                .background(Color.accentColor)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 40)
                } onSuccess: { data in
                    if let items = data?.items, !items.isEmpty {
                        LazyVStack(alignment: .leading, spacing: 16) {
                            ForEach(items, id: \.id) { playlist in
                                PlaylistCard(data: playlist)
                            }
                        }
                        .padding(.top, 75)
                        .padding(.bottom, 24)
                    } else {
                        VStack(spacing: 16) {
                            Image(systemName: "music.note.list")
                                .font(.system(size: 60))
                                .foregroundColor(.secondary)
                            
                            Text("No Playlists Yet")
                                .font(.title3)
                                .fontWeight(.semibold)
                            
                            Text("Create your first playlist to get started")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                                .multilineTextAlignment(.center)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 60)
                    }
                    
                }
                
            }
            .padding(.horizontal)
            .onScrollGeometryChange(for: Double.self, of: { geo in
                geo.contentOffset.y
            }, action: { oldValue, newValue in
                if oldValue != newValue {
                    lastScrollOffset = newValue
                }
            })
            .onAppear(perform: {
                currentPlaylistVm.getCurrentPlaylists()
            })
            
            GradientAppBar()
            
            CustomNavigationView(title: "Library")
                .opacity(lastScrollOffset.isFinite && lastScrollOffset > -50 ? 0 : 1)
                .animation(.easeInOut(duration: 0.3), value: lastScrollOffset)
        }
    }
}
