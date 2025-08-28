//
//  NewAlbumsSkeleton.swift
//  iosApp
//
//  Created by Muhammad Vikri on 28/08/25.
//

import SwiftUI

struct AlbumImageSkeleton: View {
    var height: CGFloat = 110
    
    var body: some View {
        RoundedRectangle(cornerRadius: 16)
            .fill(Color.gray.opacity(0.2))
            .frame(maxWidth: .infinity)
            .frame(height: height)
            .shimmer()
    }
}

struct NewAlbumsSkeleton: View {
    
    var body: some View {
        ForEach(0..<3, id: \.self) { _ in
            VStack(alignment: .leading, spacing: 8) {
                AlbumImageSkeleton()
                    .shadow(color: .purple.opacity(0.5), radius: 8, x: 0, y: 4)
                
                VStack(alignment: .leading, spacing: 4) {
                    // Title skeleton
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.gray.opacity(0.2))
                        .frame(height: 16)
                        .frame(maxWidth: .infinity)
                        .shimmer()
                    
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.gray.opacity(0.2))
                        .frame(height: 16)
                        .frame(width: 80)
                        .shimmer()
                    
                    // Artist skeleton
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.gray.opacity(0.15))
                        .frame(height: 14)
                        .frame(width: 60)
                        .shimmer()
                }
            }
        }
    }
}
        
        
