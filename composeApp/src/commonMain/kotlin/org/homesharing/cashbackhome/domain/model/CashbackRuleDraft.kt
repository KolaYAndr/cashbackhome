package org.homesharing.cashbackhome.domain.model

import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

data class CashbackRuleDraft(
    val cashbackRuleId: Long = 0,
    val bankCardId: Long = 0,
    val bankName: String = "",
    val percentage: Double = 0.0,
    val category: CashbackRule.CashbackCategory = CashbackRule.CashbackCategory.Other,
    val maxAmount: Double? = null,
    val startDate: String = "",
    val expirationDate: String = ""
) {
}
