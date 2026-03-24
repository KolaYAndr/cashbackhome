package org.homesharing.cashbackhome.di

import org.homesharing.cashbackhome.data.local.createCardCashbackDatabaseBuilder
import org.koin.dsl.KoinAppDeclaration

fun koinIOSConfiguration(): KoinAppDeclaration = {
    val koinModules = initKoinModules(
        databaseBuilder = createCardCashbackDatabaseBuilder()
    )

    modules(*koinModules)
}