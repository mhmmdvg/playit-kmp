//
//  CreatePlaylistSheet.swift
//  iosApp
//
//  Created by Muhammad Vikri on 21/12/25.
//

import SwiftUI

struct CreatePlaylistSheet: View {
    @Binding var playlistName: String
    @FocusState var isTextFieldFocused: Bool
    
    let onDismiss: () -> Void
    let onCreate: () -> Void
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 24) {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Playlist Name")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    
                    TextField("My Playlist #1", text: $playlistName)
                        .textFieldStyle(.roundedBorder)
                        .focused($isTextFieldFocused)
                        .submitLabel(.done)
                        .onSubmit {
                            if !playlistName.trimmingCharacters(in: .whitespaces).isEmpty {
                                onCreate()
                            }
                        }
                }
                
                Button(action: onCreate) {
                    Text("Create")
                        .fontWeight(.semibold)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(playlistName.trimmingCharacters(in: .whitespaces).isEmpty ? Color.gray.opacity(0.3) : Color.accentColor)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                .disabled(playlistName.trimmingCharacters(in: .whitespaces).isEmpty)
                
                Spacer()
            }
            .padding()
            .navigationTitle("Create Playlist")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Cancel") {
                        onDismiss()
                    }
                }
            }
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                    isTextFieldFocused = true
                }
            }
        }
    }
}
