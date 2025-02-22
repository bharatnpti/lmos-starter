package com.telekom.iaplatformcli

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.generate.ProjectGenerator
import kotlin.system.exitProcess


open class LmosStarter {

    fun agentStarter(projectConfig: ProjectConfig, agentConfig: AgentConfig) {
//        val validLists = mutableListOf<String>()
//        args.forEach { validLists.add(it.orEmpty()) }

//        val namedArgs: Map<String, List<String>> = parseNamedArguments(validLists.toTypedArray())


//        val projectDir = args.getFirstOrThrow("--dir", "--d")
//        val packageName = args.getFirstOrThrow("--pkg", "--p")
//        val projectName = args.getFirstOrThrow("--proj", "--prj")
//
//        val agentName = args.getFirstOrThrow("--agent", "--an")
//        val agentModel = args.getFirstOrThrow("--model", "--am")
//        val agentDescriptions = args.getFirstOrThrow("--desc", "--ad")
//        val agentPrompt = args.getFirstOrThrow("--prompt", "--ap")
//
//
//        val projectConfig = ProjectConfig(projectDir, packageName, projectName)
//        val agentConfig = AgentConfig(agentName, agentModel, agentDescriptions, agentPrompt)

        ProjectGenerator().generateProject(projectConfig, agentConfig)
        println("Successfully generated project: ${projectConfig.projectName}")
        exitProcess(0)
    }

//    fun parseNamedArguments(args: Array<String>): Map<String, List<String>> {
//        return args.fold(mutableMapOf<String, MutableList<String>>() to null as String?) { (map, currentOption), arg ->
//            when {
//                arg.startsWith("--") -> map.apply { this[arg] = mutableListOf() } to arg
//                currentOption != null -> map.apply { this[currentOption]?.add(arg) } to currentOption
//                else -> map to currentOption
//            }
//        }.first
//    }


    private fun Map<String, String>.getFirstOrThrow(vararg keys: String): String {
        for (key in keys) {
            this[key]?.let { return it }
        }
        throw IllegalArgumentException("Missing value for keys: ${keys.joinToString(", ")}")
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

