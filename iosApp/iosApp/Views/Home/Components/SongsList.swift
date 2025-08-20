//
//  SongsList.swift
//  iosApp
//
//  Created by Muhammad Vikri on 17/08/25.
//

import SwiftUI
import Shared

struct SongsList: View {
    let isLoading: Bool
    let errorMessage: String
    let songsData: [Item]?
    
    @State private var pressedSongId: String? = nil
    
    private func handleSongTap(_ songId: String) {
        let impactFeedback = UIImpactFeedbackGenerator(style: .medium)
        impactFeedback.impactOccurred()
        
        print("Song id \(songId)")
    }
    
    var body: some View {
        HStack {
            Text("Song List")
                .font(.system(size: 26, weight: .bold))
            Spacer()
            Button(action: {}) {
                Text("See all")
            }
        }
        
        VStack(alignment: .leading, spacing: 12) {
            if isLoading {
                Spacer()
                ProgressView()
                    .scaleEffect(1.2)
                Spacer()
            } else if errorMessage != "" {
                Text("Error: \(errorMessage)")
                    .foregroundStyle(.red)
                    .padding()
            } else if let songsData = songsData {
                ForEach(songsData.suffix(3), id: \.id) { item in
                    Button(action: {}) {
                        HStack(spacing: 16) {
                            SongImage(url: item.images[0].url)
                                .shadow(color: .purple.opacity(0.3), radius: 8, x: 0, y: 4)
                            
                            VStack(alignment: .leading) {
                                Text(item.name)
                                    .font(.system(size: 16, weight: .semibold))
                                    .foregroundStyle(Color.primary)
                                
                                Text(item.artists[0].name)
                                    .font(.system(size: 14, weight: .regular))
                                    .foregroundStyle(.gray)
                            }
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .scaleEffect(pressedSongId == item.id ? 0.95 : 1.0)
                    .animation(.easeInOut(duration: 0.1), value: pressedSongId)
                    .contextMenu(menuItems: {
                        Button(action: {
                            handleSongTap(item.id)
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
                    .onLongPressGesture(minimumDuration: 0, maximumDistance: .infinity) {
                        handleSongTap(item.id)
                    } onPressingChanged: { isPressing in
                        withAnimation(.easeInOut(duration: 0.1)) {
                            pressedSongId = isPressing ? item.id : nil
                        }
                    }
                }
            }
        }
    }
}

struct SongImage: View {
    let url: String
    
    var body: some View {
        CacheImage(url: URL(string: url)!) { phase in
            if let image = phase.image {
                image
                    .resizable()
                    .scaledToFill()
                    .frame(width: 60, height: 60)
                    .clipped()
                    .cornerRadius(16)
                
            } else if phase.error != nil {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 60, height: 60)
                    .overlay(
                        Image(systemName: "photo")
                            .foregroundColor(.gray)
                    )
            } else {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(width: 60, height: 60)
                    .overlay(
                        ProgressView()
                            .scaleEffect(0.7)
                    )
            }
            
        }
    }
}
