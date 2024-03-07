package com.telekom.iaplatformcli

import com.telekom.iaplatformcli.generate.AgentGenerator
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

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
                } else namedArgs[currentOption] = mutableListOf(args[i])
            }
            ++i
        }
        return namedArgs
    }

    override fun run(vararg args: String?) {

        println("inside the run of command line runner:")
        val validLists = mutableListOf<String>()
        println("printing args:" + args.forEach { validLists.add(it.orEmpty()) })

        val namedArgs = parseNamedArgumentsWithArray(validLists.toTypedArray())

        var projectDir = namedArgs["--dirName"].orEmpty()
        val packageName = namedArgs["--packageName"].orEmpty()
        val agentName = namedArgs["--agentName"].orEmpty()

        val steps = namedArgs["--steps"].orEmpty()

        println("Project Directory: $projectDir")
        println("Package Name: $packageName")
        println("Agent Name: $agentName")

        AgentGenerator(KotlinSourceCode(KotlinLmosImports())).generateAgent(
            projectDir[0],
            packageName[0],
            agentName[0],
            steps
        )
    }
}

fun main(args: Array<String>) {
    runApplication<LmosCliApplication>(*args)
}