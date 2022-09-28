package com.syouth.kmapper.converters

import java.math.BigDecimal

interface BigDecimalTypeConverters {
    fun mapBigDecimalToInt(b: BigDecimal?): Int? = b?.toInt()
    fun mapBigDecimalToLong(b: BigDecimal?): Long? = b?.toLong()
    fun mapBigDecimalToFloat(b: BigDecimal?): Float? = b?.toFloat()
    fun mapBigDecimalToDouble(b: BigDecimal?): Double? = b?.toDouble()

    fun mapIntToBigDecimal(i: Int?): BigDecimal? = i?.toBigDecimal()
    fun mapLongToBigDecimal(l: Long?): BigDecimal? = l?.toBigDecimal()
    fun mapFloatToBigDecimal(f: Float?): BigDecimal? = f?.toBigDecimal()
    fun mapDoubleToBigDecimal(d: Double?): BigDecimal? = d?.toBigDecimal()
}