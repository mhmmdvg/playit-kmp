//
//  NewAlbums.swift
//  iosApp
//
//  Created by Muhammad Vikri on 15/08/25.
//

import SwiftUI
import Shared

struct NewAlbums: View {
    let isLoading: Bool
    let errorMessage: String
    let newAlbumsData: [Item]?
    
    @State private var pressedAlbumId: String? = nil
    
    private func handleAlbumTap(_ albumId: String) {
        let impactFeedback = UIImpactFeedbackGenerator(style: .medium)
        impactFeedback.impactOccurred()
        
        print("album \(albumId)")
    }
    
    
    var body: some View {
        HStack {
            Text("New Albums")
                .font(.system(size: 26, weight: .bold))
            Spacer()
            Button(action: {}) {
                Text("See all")
            }
        }
        
        HStack(alignment: .top, spacing: 16) {
            if isLoading {
                NewAlbumsSkeleton()
            } else if errorMessage != "" {
                Text("Error: \(errorMessage)")
                    .foregroundColor(.red)
                    .padding()
            } else if let albumsData = newAlbumsData {
                ForEach(albumsData.prefix(3), id: \.id) { item in
                    Button(action: {}) {
                        VStack(alignment: .leading, spacing: 8) {
                            AlbumImage(url: item.images[0].url)
                                .shadow(color: .purple.opacity(0.5), radius: 8, x: 0, y: 4)
                            VStack(alignment: .leading) {
                                Text(item.name)
                                    .font(.system(size: 16, weight: .semibold))
                                    .foregroundStyle(Color.primary)
                                    .multilineTextAlignment(.leading)
                                    .lineLimit(2)
                                    .truncationMode(.tail)
                                Text(item.artists[0].name)
                                    .font(.system(size: 14, weight: .regular))
                                    .foregroundStyle(.gray)
                                    .lineLimit(1)
                                    .truncationMode(.tail)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                    .scaleEffect(pressedAlbumId == item.id ? 0.95 : 1.0)
                    .animation(.easeInOut(duration: 0.1), value: pressedAlbumId)
                    .contextMenu(menuItems: {
                        Button(action: {
                            handleAlbumTap(item.id)
                        }) {
                            Label("Play", systemImage: "play.fill")
                        }
                    }, preview: {
                        VStack(spacing: 16) {
                            CacheImage(url: URL(string: item.images[0].url)!) { phase in
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
                                Text(item.name)
                                    .font(.title2)
                                    .fontWeight(.bold)
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.primary)
                                    .lineLimit(3)
                                
                                Text(item.artists[0].name)
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                                    .lineLimit(2)
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
                            pressedAlbumId = isPressing ? item.id : nil
                        }
                    }, perform: {
                        handleAlbumTap(item.id)
                    })
                    .frame(maxWidth: .infinity, alignment: .top)
                }
            }
        }
    }
}

struct AlbumImage: View {
    let url: String
    var height: CGFloat = 110
    
    var body: some View {
        CacheImage(url: URL(string: url)!) { phase in
            if let image = phase.image {
                image
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .frame(height: height)
                    .clipped()
                    .cornerRadius(16)
            } else if phase.error != nil {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: height)
                    .overlay {
                        Image(systemName: "photo")
                            .foregroundStyle(.gray)
                    }
            } else {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: height)
                    .overlay {
                        ProgressView()
                            .scaleEffect(0.7)
                    }
            }
        }
    }
}
