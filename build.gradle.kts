plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "org.brunini"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.1.1")
    type.set("IU") // Altere para Ultimate Edition
    plugins.set(listOf("com.intellij.css", "org.jetbrains.plugins.htmltools"))

    // Plugins para suporte a HTML e CSS
    plugins.set(listOf("java", "com.intellij.css", "org.jetbrains.plugins.htmltools"))
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // Cliente HTTP
    implementation("com.google.code.gson:gson:2.8.8") // Manipulação de JSON
}

tasks {
    // Configuração de compatibilidade com Java 17
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")  // Build mínima compatível com o IntelliJ
        untilBuild.set("242.*") // Build máxima compatível
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
