package com.finvora.ai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform