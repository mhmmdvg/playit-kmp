//
//  StatusBarHeight.swift
//  iosApp
//
//  Created by Muhammad Vikri on 24/09/25.
//

import SwiftUI

extension UIApplication {
    var statusBarHeight: CGFloat {
        guard let windowScene = connectedScenes.first as? UIWindowScene,
              let statusBarManager = windowScene.statusBarManager else {
            return 0
        }
        return statusBarManager.statusBarFrame.height
    }
}
