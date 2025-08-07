package com.playit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform