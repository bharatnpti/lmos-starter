package com.telekom.iaplatformcli

import com.telekom.iaplatformcli.generate.ProjectGenerator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class LmosCliApplication : CommandLineRunner {

    fun parseNamedArgumentsWithArray(args: Array<String>): Map<String, List<String>> {
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

        val namedArgs = parseNamedArgumentsWithArray(validLists.toTypedArray())

        if (namedArgs.size < 2) {
            println("Insufficient arguments. Exiting...")
            exitProcess(0)
        }

        var projectDir = namedArgs["--dirName"].orEmpty()
        val packageName = namedArgs["--packageName"].orEmpty()
        val agentName = namedArgs["--agentName"].orEmpty()
        val projectName = namedArgs["--projectName"].orEmpty()

        val steps = namedArgs["--steps"].orEmpty()

        ProjectGenerator().generateProject(projectName[0], projectDir[0], packageName[0], agentName[0], steps)
        println("Successfully generated project: ${projectName[0]}")
        exitProcess(0)
    }
}

fun main(args: Array<String>) {
    runApplication<LmosCliApplication>(*args)
}
