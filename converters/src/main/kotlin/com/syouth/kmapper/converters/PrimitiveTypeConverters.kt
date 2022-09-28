package com.syouth.kmapper.converters

interface PrimitiveTypeConverters {
    fun mapIntToFloat(n: Int?): Float? = n?.toFloat()
    fun mapIntToDouble(n: Int?): Double? = n?.toDouble()
    fun mapIntToLong(n: Int?): Long? = n?.toLong()

    fun mapFloatToInt(f: Float?): Int? = f?.toInt()
    fun mapFloatToDouble(f: Float?): Double? = f?.toDouble()
    fun mapFloatToLong(f: Float?): Long? = f?.toLong()

    fun mapDoubleToInt(d: Double?): Int? = d?.toInt()
    fun mapDoubleToFloat(d: Double?): Float? = d?.toFloat()
    fun mapDoubleToLong(d: Double?): Long? = d?.toLong()

    fun mapLongToInt(l: Long?): Int? = l?.toInt()
    fun mapLongToFloat(l: Long?): Float? = l?.toFloat()
    fun mapLongToDouble(l: Long?): Double? = l?.toDouble()
}