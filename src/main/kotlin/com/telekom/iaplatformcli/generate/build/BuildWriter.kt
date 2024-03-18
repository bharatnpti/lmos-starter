package com.telekom.iaplatformcli.generate.build

import com.telekom.iaplatformcli.constants.GradleConstants
import com.telekom.iaplatformcli.service.writer.IndentedWriter
import java.io.File

interface BuildWriter {
    fun createBuildFiles(projectDir: String, packageName: String): BuildWriter
    fun getProjectType(): String
}

class GradleBuildWriter : BuildWriter {
    private val buildType = "GRADLE"

    override fun createBuildFiles(projectDir: String, packageName: String): BuildWriter {
        createBuildGradle(projectDir, packageName)
        createSettingsGradle(projectDir)
        createGradleProperties(projectDir)
        createGradleWrappers(projectDir)
        println("Successfully generated build files in dir: $projectDir")
        return this
    }

    private fun createGradleWrappers(projectDir: String) {
        val gradleWrapperDir = File(projectDir, "gradle/wrapper")
        gradleWrapperDir.mkdirs()

        val gradleWrapperPropertiesFile = File(gradleWrapperDir, "gradle-wrapper.properties")
        gradleWrapperPropertiesFile.writeText("")
        gradleWrapperPropertiesFile.writeText(
            """
        ${GradleConstants.GRADLE_WRAPPER_PROPERTIES}
            """.trimIndent(),
        )
    }

    private fun createGradleProperties(projectDir: String) {
        val gradlePropertiesFile = File(projectDir, "gradle.properties")
        gradlePropertiesFile.writeText("") // write oneAI_MAVEN_USER credentials
    }

    private fun createSettingsGradle(projectDir: String) {
        val settingsGradleFile = File(projectDir, "settings.gradle.kts")

        settingsGradleFile.writeText(
            """
        rootProject.name = "${projectDir.split("/").last()}"
        
        ${GradleConstants.SETTINGS_PLUGIN_MANAGEMENT}
            """.trimIndent(),
        )
    }

    private fun createBuildGradle(projectDir: String, packageName: String) {
        val buildFile = File("$projectDir/build.gradle.kts")
        val writer = IndentedWriter(buildFile)
        // remove any existing content from file
        buildFile.writeText("")
        writer.writeLine(GradleConstants.GRADLE_PLUGIN)
        writer.writeLine("group = \"$packageName\"")
        writer.writeLine(
            """
        version = "0.0.1-SNAPSHOT"
        
        dependencies {
            ${GradleConstants.GRADLE_DEPENDENCIES}
        }
            """.trimIndent(),
        )
    }

    override fun getProjectType(): String {
        return this.buildType
    }
}
