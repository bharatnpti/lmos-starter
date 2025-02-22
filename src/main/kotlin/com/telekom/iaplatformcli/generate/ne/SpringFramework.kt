package com.telekom.iaplatformcli.generate.ne

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.BuildScriptGenerator
import com.telekom.iaplatformcli.utils.FileUtil
import com.telekom.iaplatformcli.utils.FileUtil.resolveSrcPath
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class SpringFramework : Framework {
    override fun setupFramework(projectConfig: ProjectConfig, agentConfig: AgentConfig) {
        println("Setting up Spring Framework.")
        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)
        createSpringBootApplicationClass(projectDir, projectConfig.packageName, projectConfig.projectName)
        createSpringBootResourceFolder(projectDir)

        createAgentKts(
            createAgentsFolder(projectDir), agentConfig
        )
    }

    private fun createAgentKts(projectDir: Path, agentConfig: AgentConfig) {
        val agentFile = projectDir.resolve("${agentConfig.name}.agent.kts")
        agentFile.writeText(BuildScriptGenerator.generateAgent(agentConfig))
        println("Successfully generated code for agent: $agentConfig")
    }

    private fun createSpringBootApplicationClass(projectDir: Path, packageName: String, projectName: String) {
        val className = FileUtil.getMainApplicationName(projectName)
        val mainFilePath = resolveSrcPath(projectDir).resolve("$className.kt")
        mainFilePath.writeText(BuildScriptGenerator.generateSpringBootApplication(packageName, className))
    }

    private fun createSpringBootResourceFolder(projectDir: Path) {
        val resourceFolderPath = projectDir.resolve("src").resolve("main").resolve("resources")
        resourceFolderPath.createDirectories()
        resourceFolderPath.resolve("application.yml").writeText(BuildScriptGenerator.generateApplicationYaml())
    }

    private fun createAgentsFolder(projectDir: Path) = projectDir.resolve("agents").apply { createDirectories() }


}