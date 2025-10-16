//
//  StateWrapperEnum.swift
//  iosApp
//
//  Created by Muhammad Vikri on 16/10/25.
//

enum StateWrapperEnum<T> {
    case loading
    case failure(String?)
    case success(T?)
}
