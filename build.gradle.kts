import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // newest from https://kotlinlang.org/docs/releases.html#release-details as ctrl-shift-alt-s (project settings) show: invalid source root: C:\Users\mwe\.gradle\caches\modules-2\files-2.1\org.jetbrains.kotlin\kotlin-stdlib\1.6.21\bc58085192d5abb48080e3670915133715a33ce0\kotlin-stdlib-1.6.21-sources.jar
    kotlin("jvm") version "1.6.21" // "1.6.21", 1.5.31 bundled with gradlew 7.4.2 `$ ./gradlew --version`. Sometimes intelli breaks and this version has to be changed to the one used by gradle
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
    val kotlinxCoroutinesVersion = "1.6.4"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    // org.jetbrains.kotlinx:kotlinx-coroutines-jdk8

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0") {
        // https://stackoverflow.com/questions/62386086/how-to-correctly-exclude-junit-vintage-engine-in-gradle-build-file
        exclude(group= "org.junit.vintage", module= "junit-vintage-engine")
    }

    testImplementation("io.mockk:mockk:1.12.7")
    testImplementation("org.assertj:assertj-core:3.23.1") // offers assertThat
    // testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
}

// seems not to be needed although mentioned: https://stackoverflow.com/questions/62386086/how-to-correctly-exclude-junit-vintage-engine-in-gradle-build-file
//tasks.withType<Test> {
//    useJUnitPlatform()
//}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
