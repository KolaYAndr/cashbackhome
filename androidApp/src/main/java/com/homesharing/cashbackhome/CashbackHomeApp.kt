package com.homesharing.cashbackhome

import android.app.Application
import com.homesharing.cashbackhome.di.koinAndroidConfiguration
import org.koin.core.context.startKoin

class CashbackHomeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(koinAndroidConfiguration(this@CashbackHomeApp))
    }
}