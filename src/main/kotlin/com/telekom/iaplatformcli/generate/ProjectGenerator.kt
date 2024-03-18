package com.telekom.iaplatformcli.generate

import com.telekom.iaplatformcli.generate.agent.AgentGenerator
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import java.io.File

class ProjectGenerator {

    fun generateProject(
        projectName: String,
        dirName: String,
        packageName: String,
        agentName: String,
        steps: List<String>,
    ) {
        createProjectStructure(dirName) // create directory upto project if not exist
        val projectPath = "$dirName/$projectName"
        createDirectory(projectPath)
        createPackageStructure(projectPath) // create src directory if not exist
        // addDependencies(dirName, dependencies) // to be implemented

        createBuildFiles(projectPath, packageName)
        createSpringBootApplicationClass(projectPath, projectName, packageName)
        createSpringBootResourceFolder(projectPath)
        createAgent(projectPath, packageName, agentName, steps)
    }

    private fun createSpringBootApplicationClass(projectPath: String, mainProjectName: String, packageName: String) {
        val className = "${mainProjectName.replaceFirstChar { it.titlecase() }}Application"

        val fileContent = """
        package $packageName

        import org.springframework.boot.autoconfigure.SpringBootApplication
        import org.springframework.boot.runApplication

        @SpringBootApplication
        class $className

        fun main(args: Array<String>) {
            runApplication<$className>(*args)
        }
        """.trimIndent()

        val filePath = "$projectPath/src/main/kotlin/$className.kt"

        val mainFile = File(filePath)
        mainFile.createNewFile()
        mainFile.writeText(fileContent)

        println("Spring Boot main application class created successfully at $filePath")
    }

    private fun createSpringBootResourceFolder(path: String) {
        val resourceFolderPath = "$path/src/main/resources"
        val applicationYmlContent = """
            kernel:
                repositories:
                    conversation: memory
        """.trimIndent()

        File(resourceFolderPath).mkdirs()
        File("$resourceFolderPath/application.yml").writeText(applicationYmlContent)

        println("Spring Boot resource folder created successfully at $resourceFolderPath")
    }

    private fun createBuildFiles(dirName: String, packageName: String) {
        GradleBuildWriter().createBuildFiles(dirName, packageName)
    }

    private fun createAgent(dirName: String, packageName: String, agentName: String, steps: List<String>) {
        // hardcoded instantiation should be changed to injection
        AgentGenerator(KotlinSourceCode(KotlinLmosImports())).generateAgent(dirName, packageName, agentName, steps)
    }

    private fun createProjectStructure(projectName: String) {
        createDirectory(projectName)
    }

    private fun createPackageStructure(projectName: String) {
        createDirectory("$projectName/src/main/kotlin/")
    }

    private fun addDependencies(projectName: String, dependencies: List<String>) {
        // Logic to add dependencies to the project (e.g., update build.gradle.kts file)
        println("Added dependencies to $projectName: $dependencies")
    }

    private fun createDirectory(directoryPath: String) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
            println("Directory '$directoryPath' created successfully.")
        } else {
            println("Directory '$directoryPath' already exists.")
        }
    }
}
