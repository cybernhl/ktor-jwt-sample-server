val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project

plugins {
    kotlin("jvm").version("1.9.0").apply(false)
    id("io.ktor.plugin").version("2.3.2").apply(false)
    id("org.jetbrains.kotlin.plugin.serialization").version("1.9.0").apply(false)
    id("com.squareup.sqldelight").version("1.5.5").apply(false)
}