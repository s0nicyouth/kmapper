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

class AnvilInjectorTest: BaseProcessorTest() {
    @Test
    fun `GIVEN anvil injector is enabled THEN @ContributesBinding is generated`() {
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

abstract class AppScope private constructor()
            """.trimIndent()
        )

        val compilationResult = compile(mapperGenerationSource, options = mutableMapOf("injector" to "anvil", "anvilBindingScope" to "com.test.AppScope"))
        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertSourceEquals(
            """
package com.test

import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(scope = AppScope::class)
public class TestMapperImpl @Inject constructor() : TestMapper {
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
    fun `GIVEN anvil injector is enabled with anvilGenerateAsSingletons set to true THEN @Singleton is generated`() {
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

abstract class AppScope private constructor()
            """.trimIndent()
        )

        val compilationResult = compile(mapperGenerationSource, options = mutableMapOf("injector" to "anvil", "anvilBindingScope" to "com.test.AppScope", "anvilGenerateAsSingletons" to "true"))
        Assertions.assertEquals(KotlinCompilation.ExitCode.OK, compilationResult.exitCode)
        assertSourceEquals(
            """
package com.test

import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(scope = AppScope::class)
public class TestMapperImpl @Inject constructor() : TestMapper {
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
    fun `GIVEN anvil injector is enabled without anvilBindingScope THEN compilation fails`() {
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

abstract class AppScope private constructor()
            """.trimIndent()
        )

        val compilationResult = compile(mapperGenerationSource, options = mutableMapOf("injector" to "anvil"))
        Assertions.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilationResult.exitCode)
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