package com.homesharing.cashbackhome.data.mapper

import com.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import com.homesharing.cashbackhome.domain.model.CashbackRuleDraft

internal object DbEntityModelMapper {
    fun cashbackRuleToCashBackRuleDraft(source: CashbackRule) = CashbackRuleDraft(
        cashbackRuleId = source.cashbackRuleId,
        title = source.title,
        percentage = source.percentage,
        category = source.category,
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )

    fun cashbackRuleToCashBackRuleDraftList(source: List<CashbackRule>) = List(source.size) {
        cashbackRuleToCashBackRuleDraft(source[it])
    }

    fun cashbackRuleDraftToCashBackRule(source: CashbackRuleDraft) = CashbackRule(
        title = source.title,
        percentage = source.percentage,
        category = source.category,
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )

    fun cashbackRuleDraftToCashBackRuleList(source: List<CashbackRuleDraft>) = List(source.size) {
        cashbackRuleDraftToCashBackRule(source[it])
    }
}