package com.syouth.kmapper.anvil

import com.squareup.anvil.annotations.MergeComponent
import com.syouth.kmapper.processor_annotations.Mapper
import javax.inject.Singleton
import kotlin.random.Random


fun main(args : Array<String>) {
    val appComponent = DaggerAppComponent.create()
    val mapper = appComponent.getTestMapper()

    val testDomain = TestDomain(Random.nextInt())
    val testDto = mapper.toDto(testDomain)
    println("Mapping result: ${testDomain.testField == testDto.testField}")
}

data class TestDto(val testField: Int)
data class TestDomain(val testField: Int)

@Mapper
interface TestMapper{
    fun toDto(testDomain: TestDomain): TestDto
}

abstract class AppScope private constructor()

@Singleton
@MergeComponent(AppScope::class)
interface AppComponent{
    fun getTestMapper(): TestMapper
}