package com.telekom.iaplatformcli

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.generate.ProjectGenerator
import kotlin.system.exitProcess


open class LmosStarter {

    fun agentStarter(projectConfig: ProjectConfig, agentConfig: AgentConfig) {
        ProjectGenerator().generateProject(projectConfig, agentConfig)
        println("Successfully generated project: ${projectConfig.projectName}")
        exitProcess(0)
    }

}

fun main() {
    val namedArgs = mapOf(
        "--d" to "/Users/bharatbhushan/IdeaProjects/Kinetiqx/starter-temp",
        "--p" to "com.tele",
        "--prj" to "project_nam2",
        "--an" to "starter_agent",
        "--am" to "some_model",
        "--ad" to "Agent description here",
        "--ap" to "Agent prompt here"
    )

    val projectDir = namedArgs.getValue("--d")
    val packageName = namedArgs.getValue("--p")
    val projectName = namedArgs.getValue("--prj")

    val agentName = namedArgs.getValue("--an")
    val agentModel = namedArgs.getValue("--am")
    val agentDescriptions = namedArgs.getValue("--ad")
    val agentPrompt = namedArgs.getValue("--ap")

    val projectConfig = ProjectConfig(projectDir, packageName, projectName)
    val agentConfig = AgentConfig(agentName, agentModel, agentDescriptions, agentPrompt)

    LmosStarter().agentStarter(projectConfig, agentConfig)
}

