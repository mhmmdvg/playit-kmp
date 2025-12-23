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
    @StateObject private var currentMeVm: CurrentMeViewModelWrapper
    
    @State private var lastScrollOffset: CGFloat = 0
    @State private var showCreatePlaylist = false
    @State private var playlistName = ""
    @FocusState private var isTextFieldFocused: Bool
    
    init(currentPlaylistRepository: PlaylistsRepositoryImpl, currentMeRepository: ProfileRepositoryImpl) {
        self._currentPlaylistVm = StateObject(wrappedValue: CurrentPlaylistsViewModelWrapper(playlistsRepository: currentPlaylistRepository))
        self._currentMeVm = StateObject(wrappedValue: CurrentMeViewModelWrapper(profileRepository: currentMeRepository))
    }
    
    private func createPlaylist() {
        guard !playlistName.trimmingCharacters(in: .whitespaces).isEmpty else { return }
        
        // Replace "USER_ID" with actual user ID from your auth system
        let request = CreatePlaylistRequest(name: playlistName, description: nil, public: false)
        currentPlaylistVm.createPlaylist(userId: currentMeVm.currentMeData?.id ?? "", req: request)
        
        showCreatePlaylist = false
        playlistName = ""
    }
    
    var body: some View {
        NavigationStack {
            List {
                StateWrapper(resource: currentPlaylistVm.currentPlaylists) {
                    Section {
                        VStack(spacing: 12) {
                            ProgressView()
                            Text("Loading your playlists...")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 40)
                    }
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.clear)
                } onFailure: { error in
                    Section {
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
                    }
                    .listRowSeparator(.hidden)
                    .listRowBackground(Color.clear)
                } onSuccess: { data in
                    if let items = data?.items, !items.isEmpty {
                        ForEach(items, id: \.id) { playlist in
                            PlaylistCard(data: playlist)
                                .listRowSeparator(.hidden)
                                .listRowInsets(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 0))
                                .listRowBackground(Color.clear)
                        }
                        .padding(.horizontal)
                    } else {
                        Section {
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
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                    }
                }
            }
            .listStyle(.plain)
            .navigationTitle("Library")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showCreatePlaylist = true
                    } label: {
                        Image(systemName: "plus")
                    }
                    
                }
            }
        }
        .sheet(isPresented: $showCreatePlaylist) {
            CreatePlaylistSheet(
                playlistName: $playlistName,
                isTextFieldFocused: _isTextFieldFocused,
                onDismiss: {
                    showCreatePlaylist = false
                    playlistName = ""
                },
                onCreate: {
                    createPlaylist()
                }
            )
            .presentationDetents([.height(280)])
            .presentationDragIndicator(.visible)
        }
    }
}
