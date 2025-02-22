package com.telekom.iaplatformcli

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.MustacheTemplateEngine
import com.telekom.iaplatformcli.generate.ne.GradleSpringProjectFactory
import com.telekom.iaplatformcli.generate.ne.ProjectFactory


open class LmosAgentGeneratorService {

    fun generateAgentProject(projectFactory: ProjectFactory, projectConfig: ProjectConfig, agentConfig: AgentConfig) {

        // Use the factory to create build tool and framework objects
        val buildTool = projectFactory.createBuildTool()
        val framework = projectFactory.createFramework()
        val templateEngine = MustacheTemplateEngine()

        // Setup the build tool and framework
        buildTool.setupBuildTool(projectConfig, agentConfig, templateEngine)
        framework.setupFramework(projectConfig, agentConfig, templateEngine)

        println("Successfully generated project: ${projectConfig.projectName}")
    }

}

fun main() {
    val namedArgs = mapOf(
        "--d" to "/Users/bharatbhushan/IdeaProjects/Kinetiqx/starter-temp",
        "--p" to "com.tele",
        "--prj" to "project_nam_new",
        "--an" to "starter_agent",
        "--am" to "some_model2",
        "--ad" to "Agent description here2",
        "--ap" to "Agent prompt here2"
    )

    val projectDir = namedArgs.getValue("--d")
    val packageName = namedArgs.getValue("--p")
    val projectName = namedArgs.getValue("--prj")

    val agentName = namedArgs.getValue("--an")
    val agentModel = namedArgs.getValue("--am")
    val agentDescriptions = namedArgs.getValue("--ad")
    val agentPrompt = namedArgs.getValue("--ap")

    val projectConfig = ProjectConfig(projectDir, packageName, projectName)
    val agentConfig = AgentConfig(agentName, agentModel, agentDescriptions, agentPrompt, null)

    LmosAgentGeneratorService().generateAgentProject(GradleSpringProjectFactory(), projectConfig, agentConfig)
}

