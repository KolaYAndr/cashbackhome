package com.homesharing.cashbackhome

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform