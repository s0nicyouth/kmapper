package com.syouth.kmapper.processor.strategies

import com.google.devtools.ksp.symbol.KSType
import com.syouth.kmapper.processor.base.Bundle
import com.syouth.kmapper.processor.strategies.Constatnts.Companion.VISITED_NODES_LIST

internal class VisitNodeStrategy {
    fun<T> scoped(bundle: Bundle, currentNode: KSType, fn: () -> T): T {
        val nodeTypeName = currentNode.toString()
        val nodes: MutableList<String> = bundle[VISITED_NODES_LIST] ?: throw IllegalStateException("Bundle is empty")
        nodes += nodeTypeName
        val result = fn()
        nodes.removeLast()
        return result
    }
}