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
                ForEach(currPlaylistData, id: \.id) { it in
                    Button(action: {}) {
                        VStack(alignment: .leading, spacing: 8) {
                            PlaylistImage(url: it.images[0].url)

                            VStack(alignment: .leading) {
                                Text(it.name)
                                    .font(.system(size: 16, weight: .semibold))
                                    .foregroundStyle(Color.primary)
                                    .multilineTextAlignment(.leading)
                                    .lineLimit(2)
                                    .truncationMode(.tail)
                            }
//                            .frame(maxWidth: .infinity, alignment: .leading)
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
                        VStack(spacing: 16) {
                            CacheImage(url: URL(string: it.images[0].url)!) { phase in
                                if let image = phase.image {
                                    image
                                        .resizable()
                                        .scaledToFill()
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 200)
                                        .clipShape(RoundedRectangle(cornerRadius: 16))
                                        .shadow(color: .purple.opacity(0.3), radius: 12, x: 0, y: 6)
                                } else if phase.error != nil {
                                    RoundedRectangle(cornerRadius: 16)
                                        .fill(Color.gray.opacity(0.3))
                                        .frame(width: 200, height: 200)
                                        .overlay {
                                            Image(systemName: "photo")
                                                .foregroundStyle(.gray)
                                                .font(.largeTitle)
                                        }
                                } else {
                                    RoundedRectangle(cornerRadius: 16)
                                        .fill(Color.gray.opacity(0.3))
                                        .frame(width: 200, height: 200)
                                        .overlay {
                                            ProgressView()
                                                .scaleEffect(1.0)
                                        }
                                }
                            }
                            
                            VStack(spacing: 8) {
                                Text(it.name)
                                    .font(.title2)
                                    .fontWeight(.bold)
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.primary)
                                    .lineLimit(3)
                            }
                            .padding(.horizontal, 16)
                        }
                        .padding(20)
                        .frame(width: 280)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(.regularMaterial)
                        )
                    })
                    .onLongPressGesture(minimumDuration: 0, maximumDistance: .infinity, pressing: { isPressing in
                        withAnimation(.easeInOut(duration: 0.1)) {
                            pressedPlaylists = isPressing ? it.id : nil
                        }
                    }, perform: {
                        handlePlaylistTap(it.id)
                    })
//                    .frame(maxWidth: .infinity, alignment: .top)
                }
            }
        }
    }
}

struct PlaylistImage: View {
    let url: String
    var size: CGFloat = 90
    
    var body: some View {
        CacheImage(url: URL(string: url)!) { phase in
            if let img = phase.image {
                    
                img
                    .resizable()
                    .scaledToFit()
                    .frame(width: size, height: size)
                    .clipped()
                    .cornerRadius(16)
            } else if phase.error != nil {
                RoundedRectangle(cornerRadius: 16)
                    .fill(.gray.opacity(0.3))
                    .frame(width: size, height: size)
                    .overlay {
                        Image(systemName: "photo")
                            .foregroundStyle(.gray)
                    }
            } else {
                RoundedRectangle(cornerRadius: 16)
                    .fill(.gray.opacity(0.3))
                    .frame(width: size, height: size)
                    .overlay {
                        ProgressView()
                            .scaleEffect(0.7)
                    }
            }
        }
    }
}
