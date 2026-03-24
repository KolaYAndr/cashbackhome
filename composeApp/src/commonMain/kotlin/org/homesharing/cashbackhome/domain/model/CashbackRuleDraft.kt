package org.homesharing.cashbackhome.domain.model

import org.homesharing.cashbackhome.domain.entity.CashbackRule

data class CashbackRuleDraft(
    val title: String = "",
    val percentage: Double = 0.0,
    val category: CashbackRule.CashbackCategory = CashbackRule.CashbackCategory.Other,
    val maxAmount: Double? = null,
    val expirationDate: String = ""
)