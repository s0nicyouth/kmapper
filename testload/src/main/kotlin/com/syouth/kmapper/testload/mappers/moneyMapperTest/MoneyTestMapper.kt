package com.syouth.kmapper.testload.mappers.moneyMapperTest

import com.syouth.kmapper.processor_annotations.Mapper
import com.syouth.kmapper.testload.domain.moneyMapperTest.Money
import com.syouth.kmapper.testload.domain.moneyMapperTest.Currency
import com.syouth.kmapper.testload.dto.moneyMapperTest.MoneyDto

internal interface CurrencyMapper {
    fun mapStringToCurrency(code: String?): Currency? = code?.let(::Currency)
}

@Mapper
internal interface MoneyTestMapper : CurrencyMapper {
    fun mapMoney(dto: MoneyDto): Money
}

internal fun MoneyDto.toMoney() = Money(
    amount = amount,
    currency = Currency(currency)
)