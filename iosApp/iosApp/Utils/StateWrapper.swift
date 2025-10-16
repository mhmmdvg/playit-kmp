//
//  StateWrapper.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/10/25.
//

import SwiftUI
import Shared

struct StateWrapper<T: AnyObject, Content: View>: View {
    let resource: Resource<T>
    let content: (StateWrapperEnum<T>) -> Content
    
    init(resource: Resource<T>, content: @escaping (StateWrapperEnum<T>) -> Content) {
        self.resource = resource
        self.content = content
    }
    
    var body: some View {
        content(currentState)
    }
    
    private var currentState: StateWrapperEnum<T> {
        switch resource {
        case is ResourceLoading<T>:
            return .loading
        case let error as ResourceError<T>:
            return .failure(error.message)
        case let success as ResourceSuccess<T>:
            return .success(success.data)
        default:
            return .loading
        }
    }
}
