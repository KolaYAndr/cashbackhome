package com.homesharing.cashbackhome.domain.model

import com.homesharing.cashbackhome.domain.entity.CashbackRule

data class CashbackRuleDraft(
    val title: String = "",
    val percentage: Double = 0.0,
    val category: CashbackRule.CashbackCategory = CashbackRule.CashbackCategory.Other,
    val maxAmount: Double? = null,
    val expirationDate: String = ""
)