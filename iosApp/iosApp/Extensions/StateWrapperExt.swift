//
//  StateWrapperExt.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/10/25.
//

import SwiftUI
import Shared

extension StateWrapper {
    init(
        resource: Resource<T>,
        @ViewBuilder onLoading: @escaping () -> some View,
        @ViewBuilder onFailure: @escaping (String?) -> some View,
        @ViewBuilder onSuccess: @escaping (T?) -> some View
    ) where Content == AnyView {
        self.resource = resource
        
        self.content = { state in
            switch state {
            case .loading:
                AnyView(onLoading())
            case .failure(let error):
                AnyView(onFailure(error))
            case .success(let data):
                AnyView(onSuccess(data))
            }
        }
    }
}
