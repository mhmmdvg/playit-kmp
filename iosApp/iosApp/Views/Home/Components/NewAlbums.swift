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
    
    private func checkClick() {
        print("Album data \(newAlbumsData)")
        print("Images url \(newAlbumsData?[0].images[0].url)")
        print("Name \(newAlbumsData?[0].name)")
        print("Artist \(newAlbumsData?[0].artists[0].name)")
    }
    
    private func handleAlbumTap(_ albumId: String) {
        // Haptic feedback
        let impactFeedback = UIImpactFeedbackGenerator(style: .medium)
        impactFeedback.impactOccurred()
        
        // Add your album tap logic here
        print("Album tapped: \(albumId)")
    }
    
    var body: some View {
        HStack {
            Text("New Albums")
                .font(.system(size: 26, weight: .bold))
            Spacer()
            Button(action: {
                checkClick()
            }) {
                Text("See all")
            }
        }
        
        HStack(alignment: .top, spacing: 16) {
            
            if isLoading {
                Spacer()
                ProgressView()
                    .scaleEffect(1.2)
                Spacer()
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
                    .scaleEffect(pressedAlbumId == item.id ? 0.95 : 1.0) // Scale animation
                    .animation(.easeInOut(duration: 0.1), value: pressedAlbumId) // Smooth animation
                    .onLongPressGesture(minimumDuration: 0, maximumDistance: .infinity, pressing: { isPressing in
                        withAnimation(.easeInOut(duration: 0.1)) {
                            pressedAlbumId = isPressing ? item.id : nil
                        }
                    }, perform: {
                        // This handles the actual tap
                        handleAlbumTap(item.id)
                    })
                    .frame(maxWidth: .infinity, alignment: .top)
                }
            }
        }
    }
}
