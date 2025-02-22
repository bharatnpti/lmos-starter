package com.telekom.iaplatformcli.generate

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.LmosStarterConstants.Companion.SRC_MAIN_KOTLIN
import com.telekom.iaplatformcli.constants.refactored.BuildScriptGenerator
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
        val projectDir = Paths.get(projectConfig.projectDir, projectConfig.projectName)
        val agentPackage = projectConfig.packageName

        createDirectories(projectDir)
        createDirectories(projectDir.resolve(SRC_MAIN_KOTLIN))

        createBuildFiles(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootApplicationClass(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootResourceFolder(projectDir)

        createAgentKts(
            createAgentsFolder(projectDir), agentConfig
        )

        FileUtil.copyResourceToDirectory("gradlew", projectDir.toString())
    }

    private fun createAgentKts(projectPath: Path, agentConfig: AgentConfig) {
        val agentFile = projectPath.resolve("${agentConfig.name}.agent.kts")
        val buildScriptGenerator = BuildScriptGenerator()

        agentFile.writeText(buildScriptGenerator.generateAgent(agentConfig))
            println("Successfully generated code for agent: $agentConfig")
    }

    private fun createSpringBootApplicationClass(projectPath: Path, packageName: String, projectName: String) {
        val className = FileUtil.getMainApplicationName(projectName)
        val mainFilePath = projectPath.resolve("$SRC_MAIN_KOTLIN/$className.kt")
        val buildScriptGenerator = BuildScriptGenerator()
        mainFilePath.writeText(buildScriptGenerator.generateSpringBootApplication(packageName, className))
    }

    private fun createSpringBootResourceFolder(projectPath: Path) {
        val resourceFolderPath = projectPath.resolve("src/main/resources")
            resourceFolderPath.createDirectories()
        val buildScriptGenerator = BuildScriptGenerator()
            resourceFolderPath.resolve("application.yml").writeText(buildScriptGenerator.generateApplicationYaml())
    }

    private fun createBuildFiles(projectPath: Path, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(projectPath.toString(), packageName, projectName)
    }

    private fun createAgentsFolder(projectPath: Path): Path {
        val agentDirectory = projectPath.resolve("agents")
        agentDirectory.createDirectories()
        return agentDirectory
    }

    private fun createDirectories(path: Path) {
        runCatching {
            path.createDirectories()
        }.onFailure {
            println("Error creating directory: ${path.toAbsolutePath()}, ${it.message}")
        }
    }

}
