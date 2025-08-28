//
//  SongListSkeleton.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI

struct SongImageSkeleton: View {
    var body: some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(Color.gray.opacity(0.2))
            .frame(width: 60, height: 60)
            .shimmer()
    }
}

struct SongListSkeleton: View {
    
    var body: some View {
        ForEach(0..<3, id: \.self) { _ in
            HStack(spacing: 16) {
                SongImageSkeleton()
                    .shadow(color: .purple.opacity(0.3), radius: 8, x: 0, y: 4)
                
                VStack(alignment: .leading, spacing: 4) {
                    // Song title skeleton
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.gray.opacity(0.2))
                        .frame(height: 16)
                        .frame(maxWidth: 120, alignment: .leading)
                        .shimmer()
                    
                    // Artist skeleton
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.gray.opacity(0.15))
                        .frame(height: 14)
                        .frame(maxWidth: 80, alignment: .leading)
                        .shimmer()
                }
                
                Spacer()
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}
