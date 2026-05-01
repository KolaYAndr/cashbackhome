package org.homesharing.cashbackhome.di

import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.homesharing.cashbackhome.data.local.database.CardCashbackDao
import org.homesharing.cashbackhome.data.local.database.CardCashbackDatabase
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.data.local.database.getRoomDatabase
import org.homesharing.cashbackhome.data.repository.CardCashbackRepositoryImpl
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import org.homesharing.cashbackhome.presentation.addcard.AddCardWithCashbacksViewModel
import org.homesharing.cashbackhome.presentation.cards.CardDetailsViewModel
import org.homesharing.cashbackhome.presentation.cards.CardsViewModel
import org.homesharing.cashbackhome.presentation.cards.UpsertCardScreenViewModel
import org.homesharing.cashbackhome.presentation.categories.CategoriesScreenViewModel
import org.homesharing.cashbackhome.presentation.categories.UpsertCategoriesScreenViewModel
import org.homesharing.cashbackhome.presentation.home.HomeScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val sharedStaticModule =
    module {
        single<CoroutineScope>() {
            CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
        singleOf(::CardCashbackRepositoryImpl) {
            bind<CardCashbackRepository>()
        }
        viewModelOf(::CardsViewModel)
        viewModelOf(::AddCardWithCashbacksViewModel)
        viewModelOf(::HomeScreenViewModel)
        viewModelOf(::CategoriesScreenViewModel)
        viewModel { (card: BankCard?) ->
            UpsertCardScreenViewModel(get(), card)
        }
        viewModel { (card: BankCard) ->
            CardDetailsViewModel(get(), get(), card)
        }
        viewModel { (category: CashbackRule?) ->
            UpsertCategoriesScreenViewModel(get(), get(), category)
        }
    }

fun initKoinModules(databaseBuilder: RoomDatabase.Builder<CardCashbackDatabase>): Array<Module> {
    val sharedModule =
        module {
            single<CardCashbackDatabase> { getRoomDatabase(databaseBuilder) }
            single<CardCashbackDao> { get<CardCashbackDatabase>().cardCashbackDao() }
        }
    return arrayOf(sharedStaticModule, sharedModule)
}
