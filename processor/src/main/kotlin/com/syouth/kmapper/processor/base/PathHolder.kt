package com.syouth.kmapper.processor.base

import com.google.devtools.ksp.symbol.KSType

internal class PathHolder {

    internal data class PathElement(
        val elementName: String,
        val elementType: KSType
    )

    private val _path: MutableList<PathElement> = mutableListOf()
    val path: List<PathElement> = _path

    fun appendPathElement(element: PathElement) {
        _path += element
    }

    fun removeLastPathElement() {
        _path.removeLast()
    }

    fun appendPath(other: PathHolder) {
        _path.addAll(other._path)
    }

    override fun toString(): String = _path.joinToString(".") { it.elementName }
}