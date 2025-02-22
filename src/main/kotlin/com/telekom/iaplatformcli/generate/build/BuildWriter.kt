package com.telekom.iaplatformcli.generate.build

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
        createGradleWrappers(projectDir)
        println("Successfully generated build files in dir: $projectDir")
        return this
    }

    private fun createGradleWrappers(projectDir: String) {
        val gradleWrapperDir = File(projectDir, "gradle/wrapper")
        gradleWrapperDir.mkdirs()
        val gradleWrapperPropertiesFile = File(gradleWrapperDir, "gradle-wrapper.properties")
        val buildScriptGenerator = BuildScriptGenerator()
        gradleWrapperPropertiesFile.writeText(buildScriptGenerator.generateGradleWrapperProperties())
        FileUtil.copyResourceToDirectory("gradle-wrapper.jar", gradleWrapperDir.absolutePath)
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
        buildFile.writeText("")
        val buildScriptGenerator = BuildScriptGenerator()
        writer.writeLine(buildScriptGenerator.generateGradlePlugins(packageName))
        writer.writeLine(buildScriptGenerator.generateRepositories())
        writer.writeLine(buildScriptGenerator.generateDependencies())
        writer.writeLine(buildScriptGenerator.generateGradleTasks(projectName))
    }

    override fun getProjectType(): String {
        return this.buildType
    }
}
