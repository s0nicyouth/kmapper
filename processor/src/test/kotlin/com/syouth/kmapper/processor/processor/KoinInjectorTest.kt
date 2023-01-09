package com.syouth.kmapper.processor.processor

import com.syouth.kmapper.processor.provider.KMapperProcessorProvider
import com.syouth.kmapper.processor.base.BaseProcessorTest
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class KoinInjectorTest: BaseProcessorTest() {
    @Test
    fun `GIVEN koin injector enabled without koinInjectionType set THEN @Factory is generated`() {
        val mapperGenerationSource = SourceFile.kotlin(
            "mapper.kt",
            """
package com.test
import com.syouth.kmapper.processor_annotations.Mapper

data class TestDto(val testField: Int)
data class TestDomain(val testField: Int)

@Mapper
interface TestMapper{
    fun toDto(testDomain: TestDomain): TestDto
}
            """.trimIndent()
        )

        val compilationResult = compile(mapperGenerationSource, options = mutableMapOf("injector" to "koin"))
        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertSourceEquals(
            """
package com.test

import org.koin.core.`annotation`.Factory

@Factory
internal class TestMapperImpl : TestMapper {
  public override fun toDto(testDomain: TestDomain): TestDto = TestDto(
    testField = testDomain.testField.let {
      it
    }
  )
}
            """.trimIndent(),
            compilationResult.sourceFor("TestMapperImpl.kt")
        )
    }

    @Test
    fun `GIVEN koin injector enabled with koinInjectionType set to single THEN @Single is generated`() {
        val mapperGenerationSource = SourceFile.kotlin(
            "mapper.kt",
            """
package com.test
import com.syouth.kmapper.processor_annotations.Mapper

data class TestDto(val testField: Int)
data class TestDomain(val testField: Int)

@Mapper
interface TestMapper{
    fun toDto(testDomain: TestDomain): TestDto
}
            """.trimIndent()
        )

        val compilationResult = compile(mapperGenerationSource, options = mutableMapOf("injector" to "koin", "koinInjectionType" to "single"))
        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertSourceEquals(
            """
package com.test

import org.koin.core.`annotation`.Single

@Single
internal class TestMapperImpl : TestMapper {
  public override fun toDto(testDomain: TestDomain): TestDto = TestDto(
    testField = testDomain.testField.let {
      it
    }
  )
}
            """.trimIndent(),
            compilationResult.sourceFor("TestMapperImpl.kt")
        )
    }

    @TempDir
    lateinit var tempDir: Path

    private fun compile(vararg source: SourceFile, options: MutableMap<String, String> = mutableMapOf()) = KotlinCompilation().apply {
        sources = source.toList()
        symbolProcessorProviders = listOf(KMapperProcessorProvider())
        kspArgs = options
        workingDir = tempDir.toFile()
        inheritClassPath = true
        verbose = false
    }.compile()
}