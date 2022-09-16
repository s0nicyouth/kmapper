package com.syouth.kmapper.processor.base

import com.google.devtools.ksp.symbol.KSType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

internal class PathHolderTest {

    private val pathHolder: PathHolder = PathHolder()

    @Test
    fun `GIVEN added path to path holder THEN path is correct`() {
        val type: KSType = mock()
        pathHolder.appendPathElement(PathHolder.PathElement("name", type))
        Assertions.assertEquals(listOf(PathHolder.PathElement("name", type)), pathHolder.path)
    }

    @Test
    fun `GIVEN added path to path holder and removed THEN path is correct`() {
        val type: KSType = mock()
        pathHolder.appendPathElement(PathHolder.PathElement("name", type))
        pathHolder.removeLastPathElement()
        Assertions.assertEquals(emptyList<PathHolder.PathElement>(), pathHolder.path)
    }

    @Test
    fun `GIVEN path appended to path THEN path is correct`() {
        val type: KSType = mock()
        pathHolder.appendPathElement(PathHolder.PathElement("1", type))
        val anotherPath = PathHolder().apply {
            appendPathElement(PathHolder.PathElement("2", type))
            appendPathElement(PathHolder.PathElement("3", type))
        }
        pathHolder.appendPath(anotherPath)
        Assertions.assertEquals(listOf(PathHolder.PathElement("1", type), PathHolder.PathElement("2", type), PathHolder.PathElement("3", type)), pathHolder.path)
    }
}