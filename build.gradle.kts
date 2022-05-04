apply {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    plugin("org.jetbrains.kotlin.jvm")
    // Kotlinx serialization for any data format
    plugin("org.jetbrains.kotlin.plugin.serialization")
    // Shade the plugin
    plugin("com.github.johnrengelman.shadow")

    // Apply the application plugin to add support for building a jar
    plugin("java")
    // Dokka documentation w/ kotlin
    plugin("org.jetbrains.dokka")
}


repositories {
    // Use mavenCentral
    mavenCentral()

    maven(url = "https://jitpack.io")
    maven(url = "https://repo.spongepowered.org/maven")
    maven(url = "https://repo.velocitypowered.com/snapshots/")
}


dependencies {

    // Use the Kotlin JDK 8 standard library.
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    // Use the Kotlin reflect library.
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

    // Use the kotlin test library
    testImplementation("io.kotest:kotest-assertions-core:5.3.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.0")

    // Add support for kotlinx courotines
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    // Compile Minestom into project
    compileOnly(project(":minestom:core"))
    compileOnly("io.github.jglrxavpok.hephaistos", "common", "2.4.4")

    // import kotlinx serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Add MiniMessage
    implementation("net.kyori:adventure-text-minimessage:4.10.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("kstom")
        mergeServiceFiles()
        minimize()

    }

    withType<Test> { useJUnitPlatform() }

    build { dependsOn(shadowJar) }

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
}

sourceSets.create("demo") {
    java.srcDir("src/demo/java")
    java.srcDir("build/generated/source/apt/demo")
    resources.srcDir("src/demo/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().output + sourceSets.main.get().runtimeClasspath
}