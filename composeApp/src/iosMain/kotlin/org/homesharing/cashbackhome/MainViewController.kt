package org.homesharing.cashbackhome

import androidx.compose.ui.window.ComposeUIViewController
import org.homesharing.cashbackhome.di.koinIOSConfiguration
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = { startKoin(koinIOSConfiguration()) }
) { App() }