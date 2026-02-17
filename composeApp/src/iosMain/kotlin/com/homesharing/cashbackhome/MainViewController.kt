package com.homesharing.cashbackhome

import androidx.compose.ui.window.ComposeUIViewController
import com.homesharing.cashbackhome.di.koinIOSConfiguration
import com.homesharing.cashbackhome.presentation.App
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = { startKoin(koinIOSConfiguration()) }
) { App() }