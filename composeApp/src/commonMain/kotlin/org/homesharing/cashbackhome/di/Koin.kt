package org.homesharing.cashbackhome.di

import androidx.room.RoomDatabase
import org.homesharing.cashbackhome.data.local.CardCashbackDao
import org.homesharing.cashbackhome.data.local.CardCashbackDatabase
import org.homesharing.cashbackhome.data.local.getRoomDatabase
import org.homesharing.cashbackhome.data.repository.CardCashbackRepositoryImpl
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import org.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksViewModel
import org.homesharing.cashbackhome.presentation.cards.CardsViewModel
import org.homesharing.cashbackhome.presentation.home.HomeScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val sharedStaticModule =
    module {
        singleOf(::CardCashbackRepositoryImpl) {
            bind<CardCashbackRepository>()
        }
        viewModelOf(::CardsViewModel)
        viewModelOf(::AddCardWithCashbacksViewModel)
        viewModelOf(::HomeScreenViewModel)
    }

fun initKoinModules(databaseBuilder: RoomDatabase.Builder<CardCashbackDatabase>): Array<Module> {
    val sharedModule =
        module {
            single<CardCashbackDatabase> { getRoomDatabase(databaseBuilder) }
            single<CardCashbackDao> { get<CardCashbackDatabase>().cardCashbackDao() }
        }
    return arrayOf(sharedStaticModule, sharedModule)
}
