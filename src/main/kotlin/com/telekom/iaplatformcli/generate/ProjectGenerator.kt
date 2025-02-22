package com.telekom.iaplatformcli.generate

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.BuildScriptGenerator
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.utils.FileUtil
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class ProjectGenerator {

    fun generateProject(
        projectConfig: ProjectConfig,
        agentConfig: AgentConfig
    ) {
        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)
        val agentPackage = projectConfig.packageName

        createDirectories(projectDir)
        createDirectories(resolveKotlinSrcPath(projectDir))

        createBuildFiles(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootApplicationClass(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootResourceFolder(projectDir)

        createAgentKts(
            createAgentsFolder(projectDir), agentConfig
        )

        FileUtil.copyResourceToDirectory("gradlew", projectDir.toString(), true)
    }

    private fun createAgentKts(projectDir: Path, agentConfig: AgentConfig) {
        val agentFile = projectDir.resolve("${agentConfig.name}.agent.kts")
        agentFile.writeText(BuildScriptGenerator.generateAgent(agentConfig))
            println("Successfully generated code for agent: $agentConfig")
    }

    private fun createSpringBootApplicationClass(projectDir: Path, packageName: String, projectName: String) {
        val className = FileUtil.getMainApplicationName(projectName)
        val mainFilePath = resolveKotlinSrcPath(projectDir).resolve("$className.kt")
        mainFilePath.writeText(BuildScriptGenerator.generateSpringBootApplication(packageName, className))
    }

    private fun resolveKotlinSrcPath(projectDir: Path) = projectDir.resolve("src").resolve("main").resolve("kotlin")

    private fun createSpringBootResourceFolder(projectDir: Path) {
        val resourceFolderPath = projectDir.resolve("src").resolve("main").resolve("resources")
        resourceFolderPath.createDirectories()
            resourceFolderPath.resolve("application.yml").writeText(BuildScriptGenerator.generateApplicationYaml())
    }

    private fun createBuildFiles(projectDir: Path, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(projectDir, packageName, projectName)
    }

    private fun createAgentsFolder(projectDir: Path) = projectDir.resolve("agents").apply { createDirectories() }

    private fun createDirectories(path: Path) {
        runCatching {
            path.createDirectories()
        }.onFailure {
            println("Error creating directory: ${path.toAbsolutePath()}, ${it.message}")
        }
    }
}
