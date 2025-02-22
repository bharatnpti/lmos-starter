package com.telekom.iaplatformcli.generate.ne

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.TemplateEngine
import com.telekom.iaplatformcli.generate.build.SpringFrameworkWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories

class SpringFramework : Framework {
    override fun setupFramework(
        projectConfig: ProjectConfig,
        agentConfig: AgentConfig,
        templateEngine: TemplateEngine
    ) {
        println("Setting up Spring Framework.")

        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)
        val agentsFolder = createAgentsFolder(projectDir, agentConfig.agentFolder)
        createSpringBootResourceFolder(projectDir)

        createSpringBootFiles(projectConfig, agentConfig.copy(agentFolder = agentsFolder), templateEngine)
    }

    private fun createSpringBootFiles(projectConfig: ProjectConfig, agentConfig: AgentConfig, templateEngine: TemplateEngine) {
        SpringFrameworkWriter(templateEngine).createFrameworkFiles(projectConfig, agentConfig)
    }

    private fun createSpringBootResourceFolder(projectDir: Path) {
        val resourceFolderPath = projectDir.resolve("src").resolve("main").resolve("resources")
        resourceFolderPath.createDirectories()
    }

    private fun createAgentsFolder(projectDir: Path, agentFolder: Path?) = agentFolder ?: projectDir.resolve("agents").apply { createDirectories() }


}