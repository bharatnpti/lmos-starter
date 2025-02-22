package com.telekom.iaplatformcli

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.generate.ProjectGenerator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
open class LmosCliApplication : CommandLineRunner {

    private fun parseNamedArgumentsWithArray(args: Array<String>): Map<String, List<String>> {
        val namedArgs = mutableMapOf<String, MutableList<String>>()
        var i = 0
        var currentOption = ""
        while (i < args.size) {
            if (args[i].startsWith("--") && i + 1 < args.size) {
                currentOption = args[i]
                namedArgs[currentOption] = mutableListOf()
            } else if (currentOption != "") {
                if (namedArgs.contains(currentOption)) {
                    namedArgs[currentOption]?.add(args[i])
                } else {
                    namedArgs[currentOption] = mutableListOf(args[i])
                }
            }
            ++i
        }
        return namedArgs
    }

    override fun run(vararg args: String?) {
        val validLists = mutableListOf<String>()
        args.forEach { validLists.add(it.orEmpty()) }

        val namedArgs: Map<String, List<String>> = parseNamedArgumentsWithArray(validLists.toTypedArray())

        if (namedArgs.size < 2) {
            println("Insufficient arguments. Exiting... ${namedArgs.keys}")
            exitProcess(0)
        }

        val projectDir = namedArgs.getFirstOrThrow("--dir", "--d")
        val packageName = namedArgs.getFirstOrThrow("--pkg", "--p")
        val projectName = namedArgs.getFirstOrThrow("--proj", "--prj")

        val agentName = namedArgs.getFirstOrThrow("--agent", "--an")
        val agentModel = namedArgs.getFirstOrThrow("--model", "--am")
        val agentDescriptions = namedArgs.getFirstOrThrow("--desc", "--ad")
        val agentPrompt = namedArgs.getFirstOrThrow("--prompt", "--ap")


        val projectConfig = ProjectConfig(projectDir, packageName, projectName)
        val agentConfig = AgentConfig(agentName, agentModel, agentDescriptions, agentPrompt)

        ProjectGenerator().generateProject(projectConfig, agentConfig)
        println("Successfully generated project: $projectName")
        exitProcess(0)
    }

    private fun Map<String, List<String>>.getFirstOrThrow(vararg keys: String): String {
        for (key in keys) {
            this[key]?.firstOrNull()?.let { return it }
        }
        throw IllegalArgumentException("Missing value for keys: ${keys.joinToString(", ")}")
    }


}

fun main(args: Array<String>) {
    runApplication<LmosCliApplication>(*args)
}
