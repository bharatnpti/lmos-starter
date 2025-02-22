package com.telekom.iaplatformcli.generate.build

import com.telekom.iaplatformcli.constants.GradleConstants
import com.telekom.iaplatformcli.constants.refactored.BuildScriptGenerator
import com.telekom.iaplatformcli.service.writer.IndentedWriter
import com.telekom.iaplatformcli.utils.FileUtil
import java.io.File

interface BuildWriter {
    fun createBuildFiles(projectDir: String, packageName: String, projectName: String): BuildWriter
    fun getProjectType(): String
}

class GradleBuildWriter : BuildWriter {
    private val buildType = "GRADLE"

    override fun createBuildFiles(projectDir: String, packageName: String, projectName: String): BuildWriter {
        createBuildGradle(projectDir, packageName, projectName)
        createSettingsGradle(projectDir, projectName)
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

        FileUtil.copyResourceToDirectory("gradle-wrapper.jar", gradleWrapperDir.absolutePath)
    }

    private fun createGradleProperties(projectDir: String) {
//        val gradlePropertiesFile = File(projectDir, "gradle.properties")
//        gradlePropertiesFile.writeText("version=0.1.0-SNAPSHOT") // write oneAI_MAVEN_USER credentials
    }

    private fun createSettingsGradle(projectDir: String, projectName: String) {
        val settingsGradleFile = File(projectDir, "settings.gradle.kts")

        settingsGradleFile.writeText(
            """
        rootProject.name = "$projectName"

            """.trimIndent(),
        )
    }

    private fun createBuildGradle(projectDir: String, packageName: String, projectName: String) {
        val buildFile = File("$projectDir/build.gradle.kts")
        val writer = IndentedWriter(buildFile)
        // remove any existing content from file
        buildFile.writeText("")
        val buildScriptGenerator = BuildScriptGenerator()
        writer.writeLine(buildScriptGenerator.generateGradlePlugins())
//        writer.writeLine("group = \"$packageName\"")
//        writer.writeLine("version = \"0.1.0-SNAPSHOT\"")

//        writer.writeLine("dependencies {\n")
        writer.writeLine(buildScriptGenerator.generateRepositories())

        writer.writeLine(buildScriptGenerator.generateDependencies())

        writer.writeLine(buildScriptGenerator.generateGradleTasks(projectName))

//        writer.writeLine(GradleConstants.GRADLE_COMPATIBILITY)
//        writer.writeLine(GradleConstants.GRADLE_TASK_JAR.format(projectName))
    }

    override fun getProjectType(): String {
        return this.buildType
    }
}
