//
//  HomeView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 08/08/25.
//

import SwiftUI
import Shared

struct HomeView: View {
    @EnvironmentObject private var authViewModel: AuthenticationViewModel
    @StateObject private var newReleasesVm: NewReleasesViewModelWrapper
    
    @State private var search = ""
    
    init(albumsRepository: AlbumsRepository) {
        self._newReleasesVm = StateObject(wrappedValue: NewReleasesViewModelWrapper(albumsRepository: albumsRepository))
    }
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Button(action: {
                        authViewModel.logout()
                    }) {
                        AlbumOverview(url: "https://i.scdn.co/image/ab67616d00001e02bc1028b7e9cd2b17c770a520")
                            .shadow(color: .black.opacity(0.3), radius: 8, x: 0, y: 4)
                    }
                    
                    //                 New Albums
                    NewAlbums(
                        isLoading: newReleasesVm.isLoading,
                        errorMessage: newReleasesVm.errorMessage ?? "",
                        newAlbumsData: newReleasesVm.newReleasesData?.albums.items
                    )
                    
                    HStack {
                        Text("Song List")
                            .font(.system(size: 26, weight: .bold))
                        Spacer()
                        Button(action: {}) {
                            Text("See all")
                        }
                    }
                    
                    VStack(spacing: 12) {
                        ForEach(1...3, id: \.self) { index in
                            Button(action: {}) {
                                HStack(spacing: 16) {
                                    SongImage(url: "https://avatar.vercel.sh/jane")
                                        .shadow(color: .purple.opacity(0.3), radius: 8, x: 0, y: 4)
                                    
                                    VStack(alignment: .leading) {
                                        Text("Song \(index)")
                                            .font(.system(size: 16, weight: .semibold))
                                            .foregroundStyle(Color.primary)
                                        
                                        Text("Artist \(index)")
                                            .font(.system(size: 14, weight: .regular))
                                            .foregroundStyle(.gray)
                                    }
                                }
                            }
                        }
                    }
                }
                .padding()
            }
            .onAppear {
                newReleasesVm.getNewReleases()
            }
            .navigationTitle("Music")
            .searchable(text: $search, prompt: "Search songs, artits, albums")
        }
    }
}

struct AlbumOverview: View {
    let url: String
    
    var body: some View {
        CacheImage(url: URL(string: url)!) { phase in
            if let image = phase.image {
                image
                    .resizable()
                    .scaledToFill()
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .clipped()
                    .cornerRadius(16)
            } else if phase.error != nil {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .overlay {
                        Image(systemName: "photo")
                            .foregroundStyle(.gray)
                    }
            } else {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: 150)
                    .overlay {
                        ProgressView()
                            .scaleEffect(0.7)
                    }
            }
        }
    }
}

struct AlbumImage: View {
    let url: String
    
    var body: some View {
        CacheImage(url: URL(string: url)!) { phase in
            if let image = phase.image {
                image
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity)
                    .frame(height: 110)
                    .clipped()
                    .cornerRadius(16)
            } else if phase.error != nil {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: 110)
                    .overlay {
                        Image(systemName: "photo")
                            .foregroundStyle(.gray)
                    }
            } else {
                RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.3))
                    .frame(maxWidth: .infinity)
                    .frame(height: 110)
                    .overlay {
                        ProgressView()
                            .scaleEffect(0.7)
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
