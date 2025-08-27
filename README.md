# kMapper - Kotlin Multiplatform Mapper

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![KSP](https://img.shields.io/badge/KSP-2.2.0--2.0.2-orange.svg)](https://github.com/google/ksp)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

kMapper is a powerful, type-safe Kotlin Multiplatform library for automated data class mapping.
It provides a simple and efficient way to map between different data classes while maintaining type
safety and compile-time validation.
It serves similar reason to https://mapstruct.org/ but provides native Kotlin support while
MapStruct often struggles to do so.

## 🚀 Features

- **Kotlin Multiplatform** support (JVM, Android, iOS, JS, Native)
- **Compile-time** code generation (no reflection)
- **Type-safe** mappings with compile-time validation
- **Seamless integration** with dependency injection frameworks (Koin, Anvil)
- **Custom converters** for complex type mappings
- **Null safety** and default value support
- **Extensible** architecture

## 📦 Installation

1. Android/Jvm only: Add the following dependency to your `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"

}

dependencies {
    // Core dependencies
    implementation("io.github.s0nicyouth:processor_annotations:1.3.0")
    implementation("io.github.s0nicyouth:converters:1.3.0")
    ksp("io.github.s0nicyouth:processor:1.3.0")
}
```

2. Kotlin Multiplatform: Add the following dependency to your `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

kotlin {
    // ...
    // KSP Common sourceSet
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}
dependencies {
    // Core dependencies
    implementation("io.github.s0nicyouth:processor_annotations:1.3.0")
    implementation("io.github.s0nicyouth:converters:1.3.0")

    with("io.github.s0nicyouth:processor:1.3.0") {
        add("kspCommonMainMetadata", this)
        add("kspAndroid", this)
        add("kspIosX64", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }

}

// KSP Metadata Trigger
project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
```

Or just simple use "kmapper-gradle-plugin"

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("io.github.s0nicyouth.kmapper-plugin")
}
```

## 🏗 Project Structure

```
kmapper/
├── examples/                    # Example projects
│   ├── anvil/                  # Anvil integration example
│   ├── koin/                   # Koin integration example
│   ├── media/                  # Demo videos and screenshots
│   └── sampleApplication/      # Sample application
│       ├── composeApp/         # Shared Compose Multiplatform code
│       └── iosApp/             # iOS application
├── kmapper_plugin/             # Gradle plugin
├── processor/                  # KSP processor
├── processor_annotations/      # Annotations for the processor
└── converters/                 # Built-in converters
```

## 🛠 Usage

### Basic Mapping

1. **Define your data classes**:

```kotlin
data class UserDto(
    val id: Long,
    val name: String,
    val email: String
)

data class User(
    val id: Long,
    val name: String,
    val email: String
)
```

2. **Create a mapper interface** with `@Mapper` annotation:

```kotlin
@Mapper
interface UserMapper {
    fun toDto(user: User): UserDto
    fun fromDto(dto: UserDto): User
}
```

The implementation will be generated at compile time. You can then use it like this:

```kotlin
val user = User(1, "John Doe", "john@example.com")
val userDto = UserMapperImpl().toDto(user)
```

### Custom Mappings

For custom field mappings, you can use the `@Mapping` annotation:

```kotlin
data class UserDto(
    val userId: Long,
    val fullName: String,
    val emailAddress: String
)

data class User(
    val id: Long,
    val name: String,
    val email: String
)

@Mapper
interface UserMapper {
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "fullName", target = "name")
    @Mapping(source = "emailAddress", target = "email")
    fun toDto(user: User): UserDto
    
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "name", target = "fullName")
    @Mapping(source = "email", target = "emailAddress")
    fun fromDto(dto: UserDto): User
}
```

### Dependency Injection with Koin

```kotlin
// 1. Define your mapper interface
@Mapper
interface UserMapper {
    fun toDto(user: User): UserDto
    fun fromDto(dto: UserDto): User
}

// 2. Create a Koin module
val appModule = module {
    factory { UserMapperImpl() }
    factory { UserRepository(get()) }
}

// 3. Start Koin with your module
fun main() {
    startKoin {
        modules(appModule)
    }
}

// or with Koin Annotations 

fun main(args: Array<String>) {
   startKoin {
      modules(defaultModule)
   }
}

// 4. Inject and use the mapper
class UserRepository (
    private val userMapper: UserMapper
) {
    fun getUser(id: Long): User {
        val dto = api.getUser(id)
        return userMapper.fromDto(dto)
    }
}
```

### Dependency Injection with Anvil

```kotlin
// 1. Define your mapper interface
@Mapper
interface UserMapper {
    fun toDto(user: User): UserDto
    fun fromDto(dto: UserDto): User
}

// 2. Define your scope
abstract class AppScope private constructor()

// 3. Create a Dagger module for mappers
@Module
@ContributesTo(AppScope::class)
object MappersModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideUserMapper(): UserMapper = UserMapperImpl()
}

// 4. Create your component
@Singleton
@MergeComponent(AppScope::class)
interface AppComponent {
    fun getUserMapper(): UserMapper
}

// 5. Use the mapper
fun main() {
    val component = DaggerAppComponent.create()
    val mapper = component.getUserMapper()
    
    val user = User(1, "John Doe", "john@example.com")
    val dto = mapper.toDto(user)
}
```

## 🎥 Demo

Check out the demo videos in the [examples/media](examples/media) directory:

- [Android Sample](examples/media/android-sample.gif)
- [Desktop Sample](examples/media/desktop-sample.gif)
- [iOS Sample](examples/media/ios-sample.gif)
- [WebAssembly Sample](examples/media/wasm-sample.gif)

## 🏃‍♂️ Running the Examples

### Android

1. Open the project in Android Studio
2. Select the `sampleApplication` run configuration
3. Run on an Android device or emulator

### iOS

1. Open the Xcode project at `examples/sampleApplication/iosApp/iosApp.xcworkspace`
2. Select a simulator or device
3. Build and run the project

### Desktop

1. Run the following command:
   ```bash
   ./gradlew :examples:sampleApplication:composeApp:run
   ```

## 📝 License

```
Copyright 2023

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
