package com.syouth.kmapper.processor.testutils

import com.syouth.kmapper.processor.base.Bundle
import com.syouth.kmapper.processor.strategies.Constatnts

internal fun createTestBundle() = Bundle().apply {
    this[Constatnts.VISITED_NODES_LIST] = mutableListOf<String>()
}