//
//  CurrentPlaylists.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/12/25.
//

import SwiftUI
import Shared

struct CurrentPlaylists: View {
    let isLoading: Bool
    let errorMessage: String
    let currentPlaylistsData: [PlaylistItems]?
    
    @State private var pressedPlaylists: String? = nil
    
    private func handlePlaylistTap(_ playlistId: String) {
        let impactFeedback = UIImpactFeedbackGenerator(style: .medium)
        impactFeedback.impactOccurred()
        
        print("playlist \(playlistId)")
    }
    
    private func getImageUrl(from playlist: PlaylistItems) -> String {
        guard let images = playlist.images,
              !images.isEmpty,
              let firstImage = images.first else {
            return "https://github.com/evilrabbit.png"
        }
        return firstImage.url
    }
    
    var body: some View {
        HStack {
            Text("Jump back in")
                .font(.system(size: 26, weight: .bold))
            Spacer()
            Button(action: {}) {
                Text("See all")
            }
        }
        
        HStack(alignment: .top) {
            if isLoading {
                NewAlbumsSkeleton()
            } else if errorMessage != "" {
                Text("Error: \(errorMessage)")
                    .foregroundStyle(.red)
                    .padding()
            } else if let currPlaylistData = currentPlaylistsData {
                ForEach(currPlaylistData.prefix(3), id: \.id) { it in
                    Button(action: {}) {
                        VStack(alignment: .leading, spacing: 8) {
                            PlaylistImage(url: getImageUrl(from: it))

                            VStack(alignment: .leading) {
                                Text(it.name)
                                    .font(.system(size: 16, weight: .semibold))
                                    .foregroundStyle(Color.primary)
                                    .multilineTextAlignment(.leading)
                                    .lineLimit(2)
                                    .truncationMode(.tail)
                            }
                        }
                    }
                    .scaleEffect(pressedPlaylists == it.id ? 0.95 : 1.0)
                    .animation(.easeInOut(duration: 0.1), value: pressedPlaylists)
                    .contextMenu(menuItems: {
                        Button(action: {
                            handlePlaylistTap(it.id)
                        }) {
                            Label("Play", systemImage: "play.fill")
                        }
                    }, preview: {
                        playlistPreview(for: it)
                    })
                    .onLongPressGesture(minimumDuration: 0, maximumDistance: .infinity, pressing: { isPressing in
                        withAnimation(.easeInOut(duration: 0.1)) {
                            pressedPlaylists = isPressing ? it.id : nil
                        }
                    }, perform: {
                        handlePlaylistTap(it.id)
                    })
                }
            }
        }
    }
    
    @ViewBuilder
    private func playlistPreview(for playlist: PlaylistItems) -> some View {
        let imageURLString = getImageUrl(from: playlist)
        let imageURL = URL(string: imageURLString)
        
        VStack(spacing: 16) {
            playlistPreviewImage(url: imageURL)
            playlistPreviewTitle(playlist.name)
        }
        .padding(20)
        .frame(width: 280)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(.regularMaterial)
        )
    }
    
    @ViewBuilder
    private func playlistPreviewImage(url: URL?) -> some View {
        CacheImage(url: url!) { phase in
            if let image = phase.image {
                image
                    .resizable()
                    .scaledToFill()
                    .frame(maxWidth: .infinity)
                    .frame(height: 200)
                    .clipShape(RoundedRectangle(cornerRadius: 16))
                    .shadow(color: .purple.opacity(0.3), radius: 12, x: 0, y: 6)
            } else if phase.error != nil {
                placeholderImage()
            } else {
                loadingImage()
            }
        }
    }
    
    @ViewBuilder
    private func placeholderImage() -> some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(Color.gray.opacity(0.3))
            .frame(width: 200, height: 200)
            .overlay {
                Image(systemName: "photo")
                    .foregroundStyle(.gray)
                    .font(.largeTitle)
            }
    }
    
    @ViewBuilder
    private func loadingImage() -> some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(Color.gray.opacity(0.3))
            .frame(width: 200, height: 200)
            .overlay {
                ProgressView()
                    .scaleEffect(1.0)
            }
    }
    
    @ViewBuilder
    private func playlistPreviewTitle(_ title: String) -> some View {
        VStack(spacing: 8) {
            Text(title)
                .font(.title2)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)
                .foregroundColor(.primary)
                .lineLimit(3)
        }
        .padding(.horizontal, 16)
    }
}

struct PlaylistImage: View {
    let url: String
    var size: CGFloat = 90
    
    var body: some View {
        if let validURL = URL(string: url) {
            CacheImage(url: validURL) { phase in
                if let img = phase.image {
                    img
                        .resizable()
                        .scaledToFit()
                        .frame(width: size, height: size)
                        .clipped()
                        .cornerRadius(16)
                } else if phase.error != nil {
                    placeholderView()
                } else {
                    loadingView()
                }
            }
        } else {
            placeholderView()
        }
    }
    
    @ViewBuilder
    private func placeholderView() -> some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(.gray.opacity(0.3))
            .frame(width: size, height: size)
            .overlay {
                Image(systemName: "photo")
                    .foregroundStyle(.gray)
            }
    }
    
    @ViewBuilder
    private func loadingView() -> some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(.gray.opacity(0.3))
            .frame(width: size, height: size)
            .overlay {
                ProgressView()
                    .scaleEffect(0.7)
            }
    }
}
