package org.homesharing.cashbackhome.data.repository.data.mapper

import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft

internal class DbEntityModelMapper {
    fun cashbackRuleToCashBackRuleDraft(source: CashbackRule) = CashbackRuleDraft(
        cashbackRuleId = source.cashbackRuleId,
        title = source.title,
        percentage = source.percentage,
        category = source.category,
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )

    fun cashbackRuleDraftToCashBackRule(source: CashbackRuleDraft) = CashbackRule(
        title = source.title,
        percentage = source.percentage,
        category = source.category,
        maxAmount = source.maxAmount,
        expirationDate = source.expirationDate
    )
}