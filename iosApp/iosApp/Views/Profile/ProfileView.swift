//
//  ProfileView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI
import Shared

struct ProfileView: View {
    @StateObject private var currentPlaylistVm: CurrentPlaylistsViewModelWrapper
    
    init(currentPlaylistRepository: PlaylistsRepositoryImpl) {
        self._currentPlaylistVm = StateObject(wrappedValue: CurrentPlaylistsViewModelWrapper(playlistsRepository: currentPlaylistRepository))
    }
    
    var body: some View {
        NavigationStack {
            ZStack(alignment: .top) {
                ScrollView {
                    ProfileSection()
                        .padding(.top, 36)
                        .padding(.bottom, 16)
                    
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
                                Task {
                                    await currentPlaylistVm.getCurrentPlaylists()
                                }
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
                                Text("Your Playlists")
                                    .font(.title2)
                                    .fontWeight(.bold)
                                    .padding(.bottom, 8)
                                
                                ForEach(items, id: \.id) { playlist in
                                    PlaylistCard(data: playlist)
                                }
                            }
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
                .padding(.horizontal, 24)
                
                GradientAppBar(showBackButton: true)
            }
        }
        .transition(.move(edge: .bottom))
    }
}

//struct ProfileView_Preview: PreviewProvider {
//    static var previews: some View {
//        ProfileView()
//    }
//}
