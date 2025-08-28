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
    
    init(albumsRepository: AlbumsRepositoryImpl) {
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
                    
                    NewAlbums(
                        isLoading: newReleasesVm.isLoading,
                        errorMessage: newReleasesVm.errorMessage ?? "",
                        newAlbumsData: newReleasesVm.newReleasesData?.albums.items
                    )
                    
                    SongsList(
                        isLoading: newReleasesVm.isLoading,
                        errorMessage: newReleasesVm.errorMessage ?? "",
                        songsData: newReleasesVm.newReleasesData?.albums.items
                    )
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
