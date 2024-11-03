import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("plugin.serialization") version "2.0.21"
    id("com.ncorti.ktfmt.gradle") version "0.20.1"
}

group = "me.centralhardware.telegram.lovcen.to.firefly"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val ktorVersion = "3.0.1"

dependencies {
    implementation("dev.inmo:tgbotapi:20.0.0")
    implementation("com.github.centralhardware:telegram-bot-commons:f950fa2e33")
    implementation("com.github.centralhardware:ktgbotapi-restrict-access-middleware:0b03d52e37")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "MainKt"))
        }
    }
}

ktfmt {
    kotlinLangStyle()
}
