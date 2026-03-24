package org.homesharing.cashbackhome

import android.app.Application
import org.homesharing.cashbackhome.di.koinAndroidConfiguration
import org.koin.core.context.startKoin

class CashbackHomeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(koinAndroidConfiguration(this@CashbackHomeApp))
    }
}