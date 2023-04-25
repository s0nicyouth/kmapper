package com.syouth.kmapper.processor.strategies

import com.google.devtools.ksp.symbol.KSType
import com.syouth.kmapper.processor.base.Bundle
import com.syouth.kmapper.processor.strategies.Constatnts.Companion.VISITED_NODES_LIST

internal class CheckCycleStrategy {
    operator fun invoke(bundle: Bundle, currentNode: KSType) {
        val nodeTypeName = currentNode.toString()
        val nodes: List<String> = bundle[VISITED_NODES_LIST] ?: throw IllegalStateException("Bundle is empty")
        if (nodeTypeName in nodes) throw IllegalStateException("Cyclic definition found: ${printCycle(nodes, nodeTypeName)}")
    }

    private fun printCycle(l: List<String>, currentNode: String): String {
        val stringBuilder = StringBuilder()
        for (n in l) {
            stringBuilder.append("$n->")
        }
        stringBuilder.append(currentNode)
        return stringBuilder.toString()
    }
}