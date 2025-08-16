package com.syouth.kmapper.data.models.moneyMapperTest

import com.syouth.kmapper.data.models.Creator

data class MoneyDto(
    val amount: Long,
    val currency: String
) {
    companion object : Creator<MoneyDto> {
        override fun create() = MoneyDto(amount = 2142, currency = "fames")
    }
}