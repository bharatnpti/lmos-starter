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
        agentPackageName: String,
        agentName: String,
        steps: List<String>,
    ) {
        createProjectStructure(dirName) // create directory upto project if not exist
        val projectPath = "$dirName/$projectName"
        createDirectory(projectPath)
        createPackageStructure(projectPath) // create src directory if not exist

        // we got agent package, remove the last directory and use the rest as base package
        val packageParts = agentPackageName.split(".")
        val basePackage = packageParts.subList(0, packageParts.size - 1).joinToString(separator = ".")

        createBuildFiles(projectPath, basePackage, projectName)
        createSpringBootApplicationClass(projectPath, basePackage, projectName)
        createSpringBootResourceFolder(projectPath)
        createGenerateResponseStep(projectPath, basePackage, "step", "GenerateResponse")

        val collectedSteps = mutableListOf<String>()
        collectedSteps.addAll(steps)

        createAgent(projectPath, basePackage, packageParts.last(), agentName, collectedSteps.toList())
        createAgentRestController(projectPath, basePackage, "controller", agentName, basePackage.plus(".${packageParts.last()}"))
        FileUtil.copyResourceToDirectory("gradlew", projectPath)
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
                language-models:
                    - id: gpt-3.5-4k
                      provider: azure-openai
                      url: https://ateam-gpt4.openai.azure.com
                      api-key: ae132cf4eae14452b5d1a94340284537
                      model-name: dt-gpt-35-turbo
                      temperature: 0.0
                      maxRetries: 3
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

    private fun createDirectory(directoryPath: String) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }
}
