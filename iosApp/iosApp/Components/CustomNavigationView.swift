//
//  CustomNavigationView.swift
//  iosApp
//
//  Created by Muhammad Vikri on 24/09/25.
//

import SwiftUI
import Shared

struct CustomNavigationView: View {
    let title: String
    
    @StateObject private var currentMeVm: CurrentMeViewModelWrapper
    @State private var showProfile = false
    
    init(title: String, profileRepository: ProfileRepositoryImpl = KoinHelper.companion.shared.provideProfileRepository()) {
        self.title = title
        self._currentMeVm = StateObject(wrappedValue: CurrentMeViewModelWrapper(profileRepository: profileRepository))
    }
    
    var body: some View {
        ZStack{
            HStack {
                Text(title)
                    .font(.largeTitle)
                    .fontWeight(.bold)
                Spacer()
                Button {
                    showProfile = true
                } label: {
                    AsyncImage(url: URL(string: currentMeVm.currentMeData?.images?[0].url ?? "https://i.scdn.co/image/ab67616d00001e02bc1028b7e9cd2b17c770a520")) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    } placeholder: {
                        Circle()
                            .fill(Color.gray.opacity(0.3))
                            .overlay(
                                Image(systemName: "person.fill")
                                    .foregroundColor(.gray)
                            )
                    }
                    .frame(width: 42, height: 42)
                    .clipShape(Circle())
                }
                
            }
        }
        .background(Color.clear)
        .padding(.top)
        .padding(.horizontal)
        .onAppear {
            currentMeVm.getCurrentMe()
        }
        .fullScreenCover(isPresented: $showProfile) {
            ProfileView(currentPlaylistRepository: KoinHelper.companion.shared.providePlaylistsRepository())
        }
    }
}
