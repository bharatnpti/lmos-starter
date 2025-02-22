package com.telekom.iaplatformcli.generate

import com.telekom.iaplatformcli.constants.CliConstants
import com.telekom.iaplatformcli.generate.agent.AgentControllerGenerator
import com.telekom.iaplatformcli.generate.agent.AgentGenerator
import com.telekom.iaplatformcli.generate.agent.StepGenerator
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute

class ProjectGenerator {

    fun generateProject(
        projectName: String,
        dirName: String,
        agentPackageName: String,
        agentName: String,
        steps: List<String>,
    ) {
        createProjectStructure(dirName) // create directory upto project if not exist
        val projectPath = "$dirName/$projectName"
        createDirectory(projectPath)
        createPackageStructure(projectPath) // create src directory if not exist

        // we got agent package, remove the last directory and use the rest as base package
//        val packageParts = agentPackageName.split(".")
//        val basePackage = packageParts.subList(0, packageParts.size - 1).joinToString(separator = ".")
//        println("packageParts: $packageParts, basePackage: $basePackage")

        createBuildFiles(projectPath, agentPackageName, projectName)
        createSpringBootApplicationClass(projectPath, agentPackageName, projectName)
        createSpringBootResourceFolder(projectPath)

        val collectedSteps = mutableListOf<String>()
        collectedSteps.addAll(steps)

        createAgentKts(createAgentsFolder(projectPath), agentName, "description", "model", "prompt")

//        createAgent(projectPath, basePackage, packageParts.last(), agentName, collectedSteps.toList())
//        createAgentRestController(projectPath, basePackage, "controller", agentName, basePackage.plus(".${packageParts.last()}"))
        FileUtil.copyResourceToDirectory("gradlew", projectPath)
    }

    private fun createAgentKts(projectPath: String, agentName: String, description: String, model: String, prompt: String) {

        // and then create the agent file inside it
        val agentFile = File("$projectPath/$agentName.agent.kts")

        agentFile.writeText(
            """
                // SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
                //
                // SPDX-License-Identifier: Apache-2.0

                agent {
                    name = "$agentName"
                    description = "$description"
                    model { "$model" }
                    tools = AllTools
                    prompt {
                        \"$prompt\"
                    }
                }
            """.trimIndent()
        )
        println("Successfully generated code for agent: $agentName")

    }

    private fun createGenerateResponseStep(projectPath: String, packageName: String, stepDir: String, stepName: String) {
        StepGenerator(KotlinSourceCode(KotlinLmosImports())).generateStep(projectPath, packageName, stepDir, stepName)
    }

    private fun createAgentRestController(projectPath: String, packageName: String, dirName: String, agentName: String, agentPackage: String) {
        AgentControllerGenerator(KotlinSourceCode(KotlinLmosImports())).generateController(projectPath, packageName, dirName, agentName, agentPackage)
    }

    private fun createSpringBootApplicationClass(projectPath: String, packageName: String, mainProjectName: String) {
        val className = FileUtil.getMainApplicationName(mainProjectName)

        val fileContent = """
package $packageName

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Simple Spring Boot application that demonstrates how to use the Arc Agents.
 */
@SpringBootApplication
class ArcAIApplication

fun main(args: Array<String>) {
    runApplication<ArcAIApplication>(*args)
}
        """.trimIndent()

        val filePath = "$projectPath/src/main/kotlin/$className.kt"

        val mainFile = File(filePath)
        mainFile.createNewFile()
        mainFile.writeText(fileContent)
    }

    private fun createSpringBootResourceFolder(path: String) {
        val resourceFolderPath = "$path/src/main/resources"
        val applicationYmlContent = """
# SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
# SPDX-License-Identifier: Apache-2.0
spring:
  jackson:
    default-property-inclusion: NON_NULL
  main:
    banner-mode: off
    web-application-type: reactive
  reactor:
    context-propagation: auto
  graphql:
    graphiql:
      enabled: true

server:
  port: 8080

arc:
  scripts:
    enabled: true
    folder: agents
    hotReload:
      enable: true
      delay: PT1S
  chat:
    ui:
      enabled: true
  subscriptions:
    events:
      enable: true

logging:
  level:
    root: WARN
    org.eclipse.lmos.arc: DEBUG
    org.eclipse.lmos.arc.app: WARN
    ArcDSL: DEBUG

management:
  server:
    port: 9090
  endpoints:
    web:
      base-path: /
      exposure:
        include: prometheus,metrics,info,health
  endpoint:
    metrics:
      enabled: true
    health:
      probes:
        enabled: true
  prometheus:
    metrics:
      export:
        enabled: true
        """.trimIndent()

        File(resourceFolderPath).mkdirs()
        File("$resourceFolderPath/application.yml").writeText(applicationYmlContent)
    }

    private fun createBuildFiles(dirName: String, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(dirName, packageName, projectName)
    }

    private fun createAgent(projectPath: String, packageName: String, dirName: String, agentName: String, steps: List<String>) {
        // hardcoded instantiation should be changed to injection
        AgentGenerator(KotlinSourceCode(KotlinLmosImports())).generateAgent(projectPath, packageName, dirName, agentName, steps)
    }

    private fun createProjectStructure(projectName: String) {
        createDirectory(projectName)
    }

    private fun createPackageStructure(projectName: String) {
        createDirectory("$projectName/src/main/kotlin/")
    }

    private fun createAgentsFolder(projectName: String): String {
        val agentDirectory = "$projectName/agents"
        createDirectory(agentDirectory)
        return agentDirectory
    }

    private fun createDirectory(directoryPath: String) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }
}
