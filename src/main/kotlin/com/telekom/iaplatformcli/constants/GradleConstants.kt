package com.telekom.iaplatformcli.constants

class GradleConstants {

    companion object {

        // one-llm-kotlin-gradle-plugin version 1.1.999 is custom-made to avoid linting issues
        const val GRADLE_PLUGIN = """
            // SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
//
// SPDX-License-Identifier: Apache-2.0

import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import java.lang.System.getenv
import java.net.URI

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.2"
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xcontext-receivers")
    }
}
        """

        const val REPOSITORIES = """
            repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/service/local/repositories/orgeclipselmos-1003/content/")
    }
}
        """

//        const val SETTINGS_PLUGIN_MANAGEMENT = "pluginManagement {\n" +
//            "    repositories {\n" +
//            "        mavenLocal()\n" +
//            "        mavenCentral()\n" +
//            "        gradlePluginPortal()\n" +
//            "        repositories {\n" +
//            "            fun String.findProperty() = System.getenv(this) ?: (if (extra.has(this)) extra[this].toString() else null)\n" +
//            "            val mavenUser = \"ONEAI_MAVEN_USER\".findProperty()\n" +
//            "            val mavenPassword = \"ONEAI_MAVEN_TOKEN\".findProperty()\n" +
//            "\n" +
//            "            maven {\n" +
//            "                name = \"oneai\"\n" +
//            "                url = java.net.URI(\"https://artifactory.devops.telekom.de/artifactory/oneai-maven/\")\n" +
//            "                credentials {\n" +
//            "                    username = mavenUser\n" +
//            "                    password = mavenPassword\n" +
//            "                }\n" +
//            "            }\n" +
//            "        }\n" +
//            "    }\n" +
//            "}"

        const val GRADLE_WRAPPER_PROPERTIES = "" +
            "        distributionBase=GRADLE_USER_HOME\n" +
            "        distributionPath=wrapper/dists\n" +
            "        distributionUrl=https\\://services.gradle.org/distributions/gradle-8.5-bin.zip\n" +
            "        networkTimeout=10000\n" +
            "        validateDistributionUrl=true\n" +
            "        zipStoreBase=GRADLE_USER_HOME\n" +
            "        zipStorePath=wrapper/dists"

        val arcVersionL = "0.0.1-SNAPSHOT"

        val GRADLE_DEPENDENCIES = """
    val arcVersion = "$arcVersionL"

    implementation("org.eclipse.lmos:arc-scripting:${'$'}arcVersion")
    implementation("org.eclipse.lmos:arc-azure-client:${'$'}arcVersion")
    implementation("com.azure:azure-identity:1.13.1")
    implementation("org.eclipse.lmos:arc-spring-boot-starter:${'$'}arcVersion")
    implementation("org.eclipse.lmos:arc-ollama-client:${'$'}arcVersion")
    implementation("org.eclipse.lmos:arc-graphql-spring-boot-starter:${'$'}arcVersion")
    implementation("com.graphql-java:graphql-java:21.5")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Metrics
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Test
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:mongodb:1.19.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
        """

        const val GRADLE_COMPATIBILITY = "" +
            "java {\n" +
            "    sourceCompatibility = JavaVersion.VERSION_21\n" +
            "}"

        const val GRADLE_TASK_KOTLIN = "tasks.withType<KotlinCompile> {\n" +
            "    kotlinOptions {\n" +
            "        freeCompilerArgs += \"-Xjsr305=strict\"\n" +
            "        jvmTarget = \"21\"\n" +
            "    }\n" +
            "}\n"
        const val GRADLE_TASK_JAR = "\n" +
            "tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>(\"bootJar\") {\n" +
            "    this.archiveFileName.set(\"%s.jar\")\n" +
            "}"
    }
}
