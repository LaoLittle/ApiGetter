plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.0-RC2"
}

group = "org.laolittle.plugin"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/central")
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
    mavenCentral()
}

dependencies {
    implementation("xyz.cssxsh.mirai:mirai-skija-plugin:1.0.0-RC1")
    implementation("io.ktor:ktor-client-cio:1.6.7")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}