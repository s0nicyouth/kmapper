# kMapper
Library for automated data class to data class mapping.
It serves similar reason to https://mapstruct.org/ but provides native Kotlin support while MapStruct often struggles to do so.
# Exmaples
Examples can be found here https://github.com/s0nicyouth/kmapper/tree/master/testload. More extensive documentation will be added soon.
# Usage
Include dependencies:
```groovy
dependencies {
    implementation("io.github.s0nicyouth:processor_annotations:+") // Annotations for the processor
    implementation("io.github.s0nicyouth:converters:+") // Convertion helpers, not neccesery
    ksp("io.github.s0nicyouth:processor:+") // processor
}
```
kMapper uses KSP so it should be included in plugins section as well.
You can find examples of usage in testload folder.
