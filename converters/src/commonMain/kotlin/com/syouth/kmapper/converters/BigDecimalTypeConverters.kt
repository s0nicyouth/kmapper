package com.syouth.kmapper.converters

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

interface BigDecimalTypeConverters {
    fun mapBigDecimalToInt(b: BigDecimal?): Int? = b?.intValue()
    fun mapBigDecimalToLong(b: BigDecimal?): Long? = b?.longValue()
    fun mapBigDecimalToFloat(b: BigDecimal?): Float? = b?.floatValue()
    fun mapBigDecimalToDouble(b: BigDecimal?): Double? = b?.doubleValue()
    fun mapBigDecimalToString(b: BigDecimal?): String? = b?.toPlainString()

    fun mapIntToBigDecimal(i: Int?): BigDecimal? = i?.toBigDecimal()
    fun mapLongToBigDecimal(l: Long?): BigDecimal? = l?.toBigDecimal()
    fun mapFloatToBigDecimal(f: Float?): BigDecimal? = f?.toBigDecimal()
    fun mapDoubleToBigDecimal(d: Double?): BigDecimal? = d?.toBigDecimal()
    fun mapStringToBigDecimal(s: String?): BigDecimal? = s?.toBigDecimal()
}