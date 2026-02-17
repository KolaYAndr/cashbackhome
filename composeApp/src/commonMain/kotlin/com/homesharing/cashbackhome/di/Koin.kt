package com.homesharing.cashbackhome.di

import androidx.room.RoomDatabase
import com.homesharing.cashbackhome.data.local.CardCashbackDatabase
import com.homesharing.cashbackhome.data.local.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoinModules(
    databaseBuilder: RoomDatabase.Builder<CardCashbackDatabase>
) : Array<Module>{
    val sharedModule = module {
        single<CardCashbackDatabase> { getRoomDatabase(databaseBuilder) }
    }

    return arrayOf(sharedModule)
}