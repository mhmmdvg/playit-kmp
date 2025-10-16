//
//  ProfileSection.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/10/25.
//

import SwiftUI

struct ProfileSection: View {
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                VStack {
                    Text("mvigi")
                        .font(.system(size: 24, weight: .bold))
                        .foregroundStyle(.primary)
                    Text("@mvigi")
                }
                Spacer()
                AsyncImage(url: URL(string: "https://i.scdn.co/image/ab67616d00001e02bc1028b7e9cd2b17c770a520")) { image in
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
                .frame(width: 64, height: 64)
                .clipShape(Circle())
            }
            HStack {
                Text("**2** Followers")
                Text("**2** Followings")
            }
    
                Button {
                    print("Check")
                } label: {
                    Text("Edit profile")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 44)
                        .background(
                            LinearGradient(
                                gradient: Gradient(colors: [Color.green, Color.green.opacity(0.8)]),
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }

        }
    }
}
