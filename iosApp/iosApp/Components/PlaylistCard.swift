//
//  PlaylistCard.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/10/25.
//

import SwiftUI
import Shared

struct PlaylistCard: View {
    @State private var pressedPlaylist: String? = nil
    var data: PlaylistItems
    
    private var imageUrl: String {
        guard let images = data.images,
              !images.isEmpty,
              let firstImage = images.first else {
            return "https://github.com/evilrabbit.png"
        }
        return firstImage.url
    }
    
    var body: some View {
        Button(action: {print("check")}) {
            HStack {
                AsyncImage(url: URL(string: imageUrl)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } placeholder: {
                    Circle()
                        .fill(Color.gray.opacity(0.3))
                        .overlay {
                            Image(systemName: "person.fill")
                                .foregroundStyle(.gray)
                        }
                }
                .frame(width: 52, height: 52)
                .clipShape(RoundedRectangle(cornerRadius: 12))
                
                VStack(alignment: .leading) {
                    Text(data.name)
                        .fontWeight(.bold)
                    Text(data.owner.displayName ?? "Anonymous")
                        .foregroundStyle(.gray)
                }
            }
        }
        .foregroundStyle(.primary)
        .animation(.easeInOut(duration: 0.1), value: pressedPlaylist)
    }
}
