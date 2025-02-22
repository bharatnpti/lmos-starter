package com.telekom.iaplatformcli.generate.old

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.LmosStarterConstants.Companion.SRC_MAIN_KOTLIN
import com.telekom.iaplatformcli.generate.agent.AgentControllerGenerator
import com.telekom.iaplatformcli.generate.agent.AgentGenerator
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class ProjectGenerator {

    fun generateProject(
        projectConfig: ProjectConfig,
        agentConfig: AgentConfig
    ) {
        val projectDir = Paths.get(projectConfig.projectDir, projectConfig.projectName)
        val agentPackage = projectConfig.packageName

        createDirectories(projectDir)
        createDirectories(projectDir.resolve(SRC_MAIN_KOTLIN))

        createBuildFiles(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootApplicationClass(projectDir, agentPackage, projectConfig.projectName)
        createSpringBootResourceFolder(projectDir)

        createAgentKts(
            createAgentsFolder(projectDir), 
            agentConfig.name, agentConfig.description, agentConfig.model, agentConfig.prompt
        )

        FileUtil.copyResourceToDirectory("gradlew", projectDir.toString())
    }

    private fun createAgentKts(projectPath: Path, agentName: String, description: String, model: String, prompt: String) {
        val agentFile = projectPath.resolve("$agentName.agent.kts")
        runCatching {
            agentFile.writeText(agentTemplate(agentName, description, model, prompt))
            println("Successfully generated code for agent: $agentName")
        }.onFailure {
            println("Error generating agent file: ${it.message}")
        }
    }

    private fun createSpringBootApplicationClass(projectPath: Path, packageName: String, mainProjectName: String) {
        val className = FileUtil.getMainApplicationName(mainProjectName)
        val mainFilePath = projectPath.resolve("$SRC_MAIN_KOTLIN/$className.kt")

        runCatching {
            mainFilePath.writeText(springBootApplicationTemplate(packageName, className))
        }.onFailure {
            println("Error generating Spring Boot application class: ${it.message}")
        }
    }

    private fun createSpringBootResourceFolder(projectPath: Path) {
        val resourceFolderPath = projectPath.resolve("src/main/resources")
        runCatching {
            resourceFolderPath.createDirectories()
            resourceFolderPath.resolve("application.yml").writeText(applicationYamlTemplate())
        }.onFailure {
            println("Error creating resource folder: ${it.message}")
        }
    }

    private fun createBuildFiles(projectPath: Path, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(projectPath.toString(), packageName, projectName)
    }

    private fun createAgent(projectPath: Path, packageName: String, dirName: String, agentName: String, steps: List<String>) {
        AgentGenerator(KotlinSourceCode(KotlinLmosImports()))
            .generateAgent(projectPath.toString(), packageName, dirName, agentName, steps)
    }

    private fun createAgentsFolder(projectPath: Path): Path {
        val agentDirectory = projectPath.resolve("agents")
        agentDirectory.createDirectories()
        return agentDirectory
    }

    private fun createDirectories(path: Path) {
        runCatching {
            path.createDirectories()
        }.onFailure {
            println("Error creating directory: ${path.toAbsolutePath()}, ${it.message}")
        }
    }

    // Templates for content generation
    private fun agentTemplate(agentName: String, description: String, model: String, prompt: String) = """
        // SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
        //
        // SPDX-License-Identifier: Apache-2.0

        agent {
            name = "$agentName"
            description = "$description"
            model { "$model" }
            tools = AllTools
            prompt {
            ${"\"\"\""}$prompt${"\"\"\""}
            }
        }
    """.trimIndent()

    private fun springBootApplicationTemplate(packageName: String, className: String) = """
        package $packageName

        import org.springframework.boot.autoconfigure.SpringBootApplication
        import org.springframework.boot.runApplication

        @SpringBootApplication
        class $className

        fun main(args: Array<String>) {
            runApplication<$className>(*args)
        }
    """.trimIndent()

    private fun applicationYamlTemplate() = """
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
}
