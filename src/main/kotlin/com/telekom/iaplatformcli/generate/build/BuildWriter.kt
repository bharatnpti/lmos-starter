package com.telekom.iaplatformcli.generate.build

import com.telekom.iaplatformcli.constants.GradleScriptGenerator
import com.telekom.iaplatformcli.constants.TemplateEngine
import com.telekom.iaplatformcli.utils.FileUtil
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface BuildWriter {
    fun createBuildFiles(projectDir: Path, packageName: String, projectName: String)
    fun getProjectType(): String
}

abstract class AbstractBuildWriter(private val buildType: String) : BuildWriter {
    override fun getProjectType(): String = buildType

    protected fun writeToFile(filePath: Path, content: String) {
        Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
            writer.write(content)
        }
    }
}

class GradleBuildWriter(templateEngine: TemplateEngine) : AbstractBuildWriter("GRADLE") {

    private val gradleScriptGenerator = GradleScriptGenerator(templateEngine)

    override fun createBuildFiles(projectDir: Path, packageName: String, projectName: String) {
        createBuildGradle(projectDir, packageName, projectName)
        createSettingsGradle(projectDir, projectName)
        createGradleWrappers(projectDir)
        println("Successfully generated build files in dir: $projectDir")
    }

    private fun createGradleWrappers(projectDir: Path) {
        val gradleWrapperDir = projectDir.resolve("gradle/wrapper")
        Files.createDirectories(gradleWrapperDir)

        val gradleWrapperPropertiesFile = gradleWrapperDir.resolve("gradle-wrapper.properties")
        writeToFile(gradleWrapperPropertiesFile, gradleScriptGenerator.generateGradleWrapperProperties())

        FileUtil.copyResourceToDirectory("gradle-wrapper.jar", gradleWrapperDir, true)
    }

    private fun createSettingsGradle(projectDir: Path, projectName: String) {
        val settingsGradleFile = projectDir.resolve("settings.gradle.kts")

        val settingsGradle = """
                rootProject.name = "$projectName"
                """.trimIndent()
        writeToFile(settingsGradleFile, settingsGradle)
    }

    private fun createBuildGradle(projectDir: Path, packageName: String, projectName: String) {
        val buildFile = projectDir.resolve("build.gradle.kts")
        writeToFile(buildFile, buildScriptContent(packageName, projectName))

    }

    private fun buildScriptContent(packageName: String, projectName: String): String {
        return buildString {
            append(gradleScriptGenerator.generateGradlePlugins(packageName))
            append(System.lineSeparator())
            append(gradleScriptGenerator.generateRepositories())
            append(System.lineSeparator())
            append(gradleScriptGenerator.generateDependencies())
            append(System.lineSeparator())
            append(gradleScriptGenerator.generateGradleTasks(projectName))
        }
    }

}
