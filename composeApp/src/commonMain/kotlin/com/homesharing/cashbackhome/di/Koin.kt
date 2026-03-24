package com.homesharing.cashbackhome.di

import androidx.room.RoomDatabase
import com.homesharing.cashbackhome.data.local.database.CardCashbackDao
import com.homesharing.cashbackhome.data.local.database.CardCashbackDatabase
import com.homesharing.cashbackhome.data.local.database.getRoomDatabase
import com.homesharing.cashbackhome.data.repository.CardCashbackRepositoryImpl
import com.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import com.homesharing.cashbackhome.domain.usecase.GetAllCashbackRulesUseCase
import com.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksViewModel
import com.homesharing.cashbackhome.presentation.cards.CardsViewModel
import com.homesharing.cashbackhome.presentation.categories.CategoriesScreenViewModel
import com.homesharing.cashbackhome.presentation.home.HomeScreenViewModel
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
        singleOf(::GetAllCashbackRulesUseCase)
        viewModelOf(::CardsViewModel)
        viewModelOf(::AddCardWithCashbacksViewModel)
        viewModelOf(::HomeScreenViewModel)
        viewModelOf(::CategoriesScreenViewModel)
    }

fun initKoinModules(databaseBuilder: RoomDatabase.Builder<CardCashbackDatabase>): Array<Module> {
    val sharedModule =
        module {
            single<CardCashbackDatabase> { getRoomDatabase(databaseBuilder) }
            single<CardCashbackDao> { get<CardCashbackDatabase>().cardCashbackDao() }
        }
    return arrayOf(sharedStaticModule, sharedModule)
}
