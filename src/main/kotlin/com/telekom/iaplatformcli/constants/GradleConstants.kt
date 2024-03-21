package com.telekom.iaplatformcli.constants

class GradleConstants {

    companion object {

        // one-llm-kotlin-gradle-plugin version 1.1.999 is custom-made to avoid linting issues
        const val GRADLE_PLUGIN = "plugins {" +
            "id(\"one-llm-kotlin-gradle-plugin\") version \"1.1.999\"" +
            "}"

        const val SETTINGS_PLUGIN_MANAGEMENT = "pluginManagement {\n" +
            "    repositories {\n" +
            "        mavenLocal()\n" +
            "        mavenCentral()\n" +
            "        gradlePluginPortal()\n" +
            "        repositories {\n" +
            "            fun String.findProperty() = System.getenv(this) ?: (if (extra.has(this)) extra[this].toString() else null)\n" +
            "            val mavenUser = \"ONEAI_MAVEN_USER\".findProperty()\n" +
            "            val mavenPassword = \"ONEAI_MAVEN_TOKEN\".findProperty()\n" +
            "\n" +
            "            maven {\n" +
            "                name = \"oneai\"\n" +
            "                url = java.net.URI(\"https://artifactory.devops.telekom.de/artifactory/oneai-maven/\")\n" +
            "                credentials {\n" +
            "                    username = mavenUser\n" +
            "                    password = mavenPassword\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"

        const val GRADLE_WRAPPER_PROPERTIES = "" +
            "        distributionBase=GRADLE_USER_HOME\n" +
            "        distributionPath=wrapper/dists\n" +
            "        distributionUrl=https\\://services.gradle.org/distributions/gradle-8.5-bin.zip\n" +
            "        networkTimeout=10000\n" +
            "        validateDistributionUrl=true\n" +
            "        zipStoreBase=GRADLE_USER_HOME\n" +
            "        zipStorePath=wrapper/dists"
        const val GRADLE_DEPENDENCIES = "" +
            "   implementation(\"org.springframework.boot:spring-boot-starter\")\n" +
            "   implementation(\"org.jetbrains.kotlin:kotlin-reflect\")\n" +
            "   testImplementation(\"org.springframework.boot:spring-boot-starter-test\")\n" +
            "   implementation(\"com.telekom.lmos:lmos-kernel:0.4.0\")\n" +
            "   implementation(\"com.telekom.lmos:lmos-kernel-impl:0.4.0\")\n" +
            "   implementation(\"com.telekom.lmos:lmos-kernel-spring-boot-starter:0.4.0\")\n" +
            "   implementation(\"com.telekom.lmos.extension:lmos-azure-openai-spring-boot-starter:0.0.5\")"

        const val GRADLE_COMPATIBILITY = "" +
            "java {\n" +
            "    sourceCompatibility = JavaVersion.VERSION_17\n" +
            "}"

        const val GRADLE_TASK_KOTLIN = "tasks.withType<KotlinCompile> {\n" +
            "    kotlinOptions {\n" +
            "        freeCompilerArgs += \"-Xjsr305=strict\"\n" +
            "        jvmTarget = \"17\"\n" +
            "    }\n" +
            "}\n"
        const val GRADLE_TASK_JAR = "\n" +
            "tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>(\"bootJar\") {\n" +
            "    this.archiveFileName.set(\"%s.jar\")\n" +
            "}"
    }
}
