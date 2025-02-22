package com.telekom.iaplatformcli.generate.build

import com.telekom.iaplatformcli.constants.BuildScriptGenerator
import com.telekom.iaplatformcli.utils.FileUtil
import java.nio.file.*

interface BuildWriter {
    fun createBuildFiles(projectDir: Path, packageName: String, projectName: String): BuildWriter
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

class GradleBuildWriter : AbstractBuildWriter("GRADLE") {

    override fun createBuildFiles(projectDir: Path, packageName: String, projectName: String): BuildWriter {
        createBuildGradle(projectDir, packageName, projectName)
        createSettingsGradle(projectDir, projectName)
        createGradleWrappers(projectDir)
        println("Successfully generated build files in dir: $projectDir")
        return this
    }

    private fun createGradleWrappers(projectDir: Path) {
        val gradleWrapperDir = projectDir.resolve("gradle/wrapper")
        Files.createDirectories(gradleWrapperDir)

        val gradleWrapperPropertiesFile = gradleWrapperDir.resolve("gradle-wrapper.properties")
//        Files.newBufferedWriter(gradleWrapperPropertiesFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
//            writer.write(BuildScriptGenerator.generateGradleWrapperProperties())
//        }
        writeToFile(gradleWrapperPropertiesFile, BuildScriptGenerator.generateGradleWrapperProperties())

        FileUtil.copyResourceToDirectory("gradle-wrapper.jar", gradleWrapperDir.toString(), true)
    }

    private fun createSettingsGradle(projectDir: Path, projectName: String) {
        val settingsGradleFile = projectDir.resolve("settings.gradle.kts")

        val settingsGradle = """
                rootProject.name = "$projectName"
                """.trimIndent()
//        Files.newBufferedWriter(settingsGradleFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
//            writer.write(
//                settingsGradle
//            )
//        }

        writeToFile(settingsGradleFile, settingsGradle)
    }

    private fun createBuildGradle(projectDir: Path, packageName: String, projectName: String) {
        val buildFile = projectDir.resolve("build.gradle.kts")
//        Files.newBufferedWriter(buildFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
//            writer.write(BuildScriptGenerator.generateGradlePlugins(packageName))
//            writer.write(BuildScriptGenerator.generateRepositories())
//            writer.write(BuildScriptGenerator.generateDependencies())
//            writer.write(BuildScriptGenerator.generateGradleTasks(projectName))
//        }

        writeToFile(buildFile, buildScriptContent(packageName, projectName))

    }

    private fun buildScriptContent(packageName: String, projectName: String): String {
        return buildString {
            append(BuildScriptGenerator.generateGradlePlugins(packageName))
            append(System.lineSeparator())
            append(BuildScriptGenerator.generateRepositories())
            append(System.lineSeparator())
            append(BuildScriptGenerator.generateDependencies())
            append(System.lineSeparator())
            append(BuildScriptGenerator.generateGradleTasks(projectName))
        }
    }

}
