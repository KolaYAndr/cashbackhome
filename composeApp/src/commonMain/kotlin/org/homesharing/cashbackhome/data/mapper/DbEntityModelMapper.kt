package org.homesharing.cashbackhome.data.repository.data.mapper

import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft

internal object DbEntityModelMapper {
    fun cashbackRuleToCashBackRuleDraft(source: CashbackRule) = CashbackRuleDraft(
        cashbackRuleId = source.cashbackRuleId,
        percentage = source.percentage,
        category = source.category.toDraftCategory(),
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )

    fun cashbackRuleToCashBackRuleDraftList(source: List<CashbackRule>) = List(source.size) {
        cashbackRuleToCashBackRuleDraft(source[it])
    }

    fun cashbackRuleDraftToCashBackRule(source: CashbackRuleDraft) = CashbackRule(
        cashbackRuleId = source.cashbackRuleId,
        percentage = source.percentage,
        category = source.category.toEntityCategory(),
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )

    fun cashbackRuleDraftToCashBackRuleList(source: List<CashbackRuleDraft>) = List(source.size) {
        cashbackRuleDraftToCashBackRule(source[it])
    }

    private fun CashbackRule.CashbackCategory.toDraftCategory(): CashbackRuleDraft.CashbackCategory = when (this) {
        CashbackRule.CashbackCategory.AllPurchases -> CashbackRuleDraft.CashbackCategory.AllPurchases
        CashbackRule.CashbackCategory.Pharmacies -> CashbackRuleDraft.CashbackCategory.Pharmacies
        CashbackRule.CashbackCategory.CafesRestaurantsFastfood -> CashbackRuleDraft.CashbackCategory.CafesRestaurantsFastfood
        CashbackRule.CashbackCategory.ClothingAndShoes -> CashbackRuleDraft.CashbackCategory.ClothingAndShoes
        CashbackRule.CashbackCategory.GasStationsFuel -> CashbackRuleDraft.CashbackCategory.GasStationsFuel
        CashbackRule.CashbackCategory.Taxi -> CashbackRuleDraft.CashbackCategory.Taxi
        CashbackRule.CashbackCategory.Marketplaces -> CashbackRuleDraft.CashbackCategory.Marketplaces
        CashbackRule.CashbackCategory.Transport -> CashbackRuleDraft.CashbackCategory.Transport
        CashbackRule.CashbackCategory.Supermarkets -> CashbackRuleDraft.CashbackCategory.Supermarkets
        CashbackRule.CashbackCategory.TrainAndAirTickets -> CashbackRuleDraft.CashbackCategory.TrainAndAirTickets
        CashbackRule.CashbackCategory.AutoServicesAndAutoGoods -> CashbackRuleDraft.CashbackCategory.AutoServicesAndAutoGoods
        CashbackRule.CashbackCategory.HomeAndRepair -> CashbackRuleDraft.CashbackCategory.HomeAndRepair
        CashbackRule.CashbackCategory.ChildrenGoods -> CashbackRuleDraft.CashbackCategory.ChildrenGoods
        CashbackRule.CashbackCategory.CommunicationInternetTv -> CashbackRuleDraft.CashbackCategory.CommunicationInternetTv
        CashbackRule.CashbackCategory.BeautyCosmetics -> CashbackRuleDraft.CashbackCategory.BeautyCosmetics
        CashbackRule.CashbackCategory.CultureEntertainment -> CashbackRuleDraft.CashbackCategory.CultureEntertainment
        CashbackRule.CashbackCategory.Books -> CashbackRuleDraft.CashbackCategory.Books
        CashbackRule.CashbackCategory.SouvenirsHobbies -> CashbackRuleDraft.CashbackCategory.SouvenirsHobbies
        CashbackRule.CashbackCategory.FlowersAndGifts -> CashbackRuleDraft.CashbackCategory.FlowersAndGifts
        CashbackRule.CashbackCategory.Jewelry -> CashbackRuleDraft.CashbackCategory.Jewelry
        CashbackRule.CashbackCategory.TechnologyElectronics -> CashbackRuleDraft.CashbackCategory.TechnologyElectronics
        CashbackRule.CashbackCategory.SportsActiveLeisure -> CashbackRuleDraft.CashbackCategory.SportsActiveLeisure
        CashbackRule.CashbackCategory.Utilities -> CashbackRuleDraft.CashbackCategory.Utilities
        CashbackRule.CashbackCategory.DutyFree -> CashbackRuleDraft.CashbackCategory.DutyFree
        CashbackRule.CashbackCategory.Groceries -> CashbackRuleDraft.CashbackCategory.Groceries
        CashbackRule.CashbackCategory.Cafe -> CashbackRuleDraft.CashbackCategory.Cafe
        CashbackRule.CashbackCategory.Restaurant -> CashbackRuleDraft.CashbackCategory.Restaurant
        CashbackRule.CashbackCategory.Travel -> CashbackRuleDraft.CashbackCategory.Travel
        CashbackRule.CashbackCategory.OnlineShopping -> CashbackRuleDraft.CashbackCategory.OnlineShopping
        CashbackRule.CashbackCategory.Flowers -> CashbackRuleDraft.CashbackCategory.Flowers
        CashbackRule.CashbackCategory.Pharmacy -> CashbackRuleDraft.CashbackCategory.Pharmacy
        CashbackRule.CashbackCategory.Other -> CashbackRuleDraft.CashbackCategory.Other
    }

    private fun CashbackRuleDraft.CashbackCategory.toEntityCategory(): CashbackRule.CashbackCategory = when (this) {
        CashbackRuleDraft.CashbackCategory.AllPurchases -> CashbackRule.CashbackCategory.AllPurchases
        CashbackRuleDraft.CashbackCategory.Pharmacies -> CashbackRule.CashbackCategory.Pharmacies
        CashbackRuleDraft.CashbackCategory.CafesRestaurantsFastfood -> CashbackRule.CashbackCategory.CafesRestaurantsFastfood
        CashbackRuleDraft.CashbackCategory.ClothingAndShoes -> CashbackRule.CashbackCategory.ClothingAndShoes
        CashbackRuleDraft.CashbackCategory.GasStationsFuel -> CashbackRule.CashbackCategory.GasStationsFuel
        CashbackRuleDraft.CashbackCategory.Taxi -> CashbackRule.CashbackCategory.Taxi
        CashbackRuleDraft.CashbackCategory.Marketplaces -> CashbackRule.CashbackCategory.Marketplaces
        CashbackRuleDraft.CashbackCategory.Transport -> CashbackRule.CashbackCategory.Transport
        CashbackRuleDraft.CashbackCategory.Supermarkets -> CashbackRule.CashbackCategory.Supermarkets
        CashbackRuleDraft.CashbackCategory.TrainAndAirTickets -> CashbackRule.CashbackCategory.TrainAndAirTickets
        CashbackRuleDraft.CashbackCategory.AutoServicesAndAutoGoods -> CashbackRule.CashbackCategory.AutoServicesAndAutoGoods
        CashbackRuleDraft.CashbackCategory.HomeAndRepair -> CashbackRule.CashbackCategory.HomeAndRepair
        CashbackRuleDraft.CashbackCategory.ChildrenGoods -> CashbackRule.CashbackCategory.ChildrenGoods
        CashbackRuleDraft.CashbackCategory.CommunicationInternetTv -> CashbackRule.CashbackCategory.CommunicationInternetTv
        CashbackRuleDraft.CashbackCategory.BeautyCosmetics -> CashbackRule.CashbackCategory.BeautyCosmetics
        CashbackRuleDraft.CashbackCategory.CultureEntertainment -> CashbackRule.CashbackCategory.CultureEntertainment
        CashbackRuleDraft.CashbackCategory.Books -> CashbackRule.CashbackCategory.Books
        CashbackRuleDraft.CashbackCategory.SouvenirsHobbies -> CashbackRule.CashbackCategory.SouvenirsHobbies
        CashbackRuleDraft.CashbackCategory.FlowersAndGifts -> CashbackRule.CashbackCategory.FlowersAndGifts
        CashbackRuleDraft.CashbackCategory.Jewelry -> CashbackRule.CashbackCategory.Jewelry
        CashbackRuleDraft.CashbackCategory.TechnologyElectronics -> CashbackRule.CashbackCategory.TechnologyElectronics
        CashbackRuleDraft.CashbackCategory.SportsActiveLeisure -> CashbackRule.CashbackCategory.SportsActiveLeisure
        CashbackRuleDraft.CashbackCategory.Utilities -> CashbackRule.CashbackCategory.Utilities
        CashbackRuleDraft.CashbackCategory.DutyFree -> CashbackRule.CashbackCategory.DutyFree
        CashbackRuleDraft.CashbackCategory.Groceries -> CashbackRule.CashbackCategory.Groceries
        CashbackRuleDraft.CashbackCategory.Cafe -> CashbackRule.CashbackCategory.Cafe
        CashbackRuleDraft.CashbackCategory.Restaurant -> CashbackRule.CashbackCategory.Restaurant
        CashbackRuleDraft.CashbackCategory.Travel -> CashbackRule.CashbackCategory.Travel
        CashbackRuleDraft.CashbackCategory.OnlineShopping -> CashbackRule.CashbackCategory.OnlineShopping
        CashbackRuleDraft.CashbackCategory.Flowers -> CashbackRule.CashbackCategory.Flowers
        CashbackRuleDraft.CashbackCategory.Pharmacy -> CashbackRule.CashbackCategory.Pharmacy
        CashbackRuleDraft.CashbackCategory.Other -> CashbackRule.CashbackCategory.Other
    }
}
