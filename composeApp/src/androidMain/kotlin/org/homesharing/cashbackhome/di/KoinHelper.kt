package org.homesharing.cashbackhome.di

import android.content.Context
import org.homesharing.cashbackhome.data.local.createCardCashbackDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.KoinAppDeclaration

fun koinAndroidConfiguration(context: Context): KoinAppDeclaration = {
    val koinModules = initKoinModules(
        databaseBuilder = createCardCashbackDatabaseBuilder(context)
    )

    androidContext(context)
    androidLogger()
    modules(*koinModules)
}