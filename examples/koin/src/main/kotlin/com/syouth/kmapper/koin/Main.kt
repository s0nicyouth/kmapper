package com.syouth.kmapper.koin

import com.syouth.kmapper.processor_annotations.Mapper
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.*


fun main(args: Array<String>) {
    startKoin {
        printLogger(Level.DEBUG)
        modules(defaultModule)
    }
}

data class TestDto(val testField: Int)
data class TestDomain(val testField: Int)

@Mapper
interface TestMapper {
    fun toDto(testDomain: TestDomain): TestDto
}
