import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    val kotlinxCoroutinesVersion = "1.6.1"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    // org.jetbrains.kotlinx:kotlinx-coroutines-jdk8

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.assertj:assertj-core:3.22.0") // offers assertThat
    // testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
