package com.syouth.kmapper.data.mappers.moneyMapperTest

import com.syouth.kmapper.data.mappers.CommonMapper
import com.syouth.kmapper.data.models.moneyMapperTest.MoneyDto
import com.syouth.kmapper.domain.models.moneyMapperTest.Currency
import com.syouth.kmapper.domain.models.moneyMapperTest.Money
import com.syouth.kmapper.processor_annotations.Mapper

internal interface CurrencyMapper {
    fun mapStringToCurrency(code: String?): Currency? = code?.let(::Currency)
}

@Mapper
internal interface MoneyTestCommonMapper : CurrencyMapper, CommonMapper<MoneyDto, Money>
