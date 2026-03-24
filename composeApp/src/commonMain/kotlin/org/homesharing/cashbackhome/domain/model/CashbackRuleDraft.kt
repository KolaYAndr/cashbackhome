package org.homesharing.cashbackhome.domain.model

import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

data class CashbackRuleDraft(
    val cashbackRuleId: Long = 0,
    val title: String = "",
    val percentage: Double = 0.0,
    val category: CashbackRule.CashbackCategory = CashbackRule.CashbackCategory.Other,
    val maxAmount: Double? = null,
    val expirationDate: String = ""
)