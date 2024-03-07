package com.telekom.iaplatformcli

import com.telekom.iaplatformcli.generate.AgentGenerator
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.generate.sourcecode.LmosImports
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IaPlatformCliApplication : CommandLineRunner {

    fun parseNamedArguments(args: Array<String>): Map<String, String> {
        val namedArgs = mutableMapOf<String, String>()
        var i = 0
        while (i < args.size - 1) {
            if (args[i].startsWith("--") && i + 1 < args.size) {
                namedArgs[args[i]] = args[i + 1]
            }
            i += 2
        }
        return namedArgs
    }

    fun parseNamedArgumentsWithArray(args: Array<String>): Map<String, List<String>> {
        val namedArgs = mutableMapOf<String, MutableList<String>>()
        var i = 0
        var currentOption = "";
        while (i < args.size) {
            if (args[i].startsWith("--") && i + 1 < args.size) {
                currentOption = args[i]
                namedArgs.put(currentOption, mutableListOf())
            } else if (currentOption != "") {
                if (namedArgs.contains(currentOption)) {
                    namedArgs[currentOption]?.add(args[i])
                } else namedArgs.put(currentOption, mutableListOf(args[i]))
            }
            ++i;
        }
        return namedArgs
    }

    override fun run(vararg args: String?) {

        println("inside the run of command line runner:")
        val validLists = mutableListOf<String>()
        println("printing args:"+args.forEach { validLists.add(it.orEmpty()) })

//        val namedArgs = parseNamedArguments(validLists.toTypedArray())
        val namedArgs = parseNamedArgumentsWithArray(validLists.toTypedArray())

        var projectDir = namedArgs["--dirName"].orEmpty()
        val packageName = namedArgs["--packageName"].orEmpty()
        val agentName = namedArgs["--agentName"].orEmpty()

        val steps = namedArgs["--steps"].orEmpty()
        //steps too, comma seperated

        println("Project Directory: $projectDir")
        println("Package Name: $packageName")
        println("Agent Name: $agentName")

        AgentGenerator(KotlinSourceCode(KotlinLmosImports())).generateAgent( projectDir[0], packageName[0], agentName[0], steps )
    }
}
    fun main(args: Array<String>) {
        runApplication<IaPlatformCliApplication>(*args)
    }