package com.telekom.iaplatformcli.generate

import com.telekom.iaplatformcli.generate.agent.AgentControllerGenerator
import com.telekom.iaplatformcli.generate.agent.AgentGenerator
import com.telekom.iaplatformcli.generate.agent.StepGenerator
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.generate.sourcecode.KotlinLmosImports
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
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

        createBuildFiles(projectPath, packageName, projectName)
        createSpringBootApplicationClass(projectPath, projectName, packageName)
        createSpringBootResourceFolder(projectPath)
        createGenerateResponseStep(projectPath, packageName, "GenerateResponse")

        val collectedSteps = mutableListOf("GenerateResponse")
        collectedSteps.addAll(steps)

        createAgent(projectPath, packageName, agentName, collectedSteps)
        createAgentRestController(projectPath, packageName, agentName)
        FileUtil.copyResourceToDirectory("gradlew", projectPath)
    }

    private fun createGenerateResponseStep(projectPath: String, packageName: String, step: String) {
        StepGenerator(KotlinSourceCode(KotlinLmosImports())).generateStep(projectPath, packageName, step)
    }

    private fun createAgentRestController(projectPath: String, packageName: String, agentName: String) {
        AgentControllerGenerator(KotlinSourceCode(KotlinLmosImports())).generateController(projectPath, packageName, agentName)
    }

    private fun createSpringBootApplicationClass(projectPath: String, mainProjectName: String, packageName: String) {
        val className = FileUtil.getMainApplicationName(mainProjectName)

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
    }

    private fun createBuildFiles(dirName: String, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(dirName, packageName, projectName)
    }

    private fun createAgent(dirName: String, packageName: String, agentName: String, steps: MutableList<String>) {
        // hardcoded instantiation should be changed to injection
        AgentGenerator(KotlinSourceCode(KotlinLmosImports())).generateAgent(dirName, packageName, agentName, steps.toList())
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
        }
    }
}
