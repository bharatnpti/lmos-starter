package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolute

class AgentControllerGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateController(dirName: String, packageName: String, agentName: String) {
        val kotlinSourcePath = Path.of(dirName).resolve("src/main/kotlin").absolute()
        var controllerFolderPath = kotlinSourcePath

        if (Files.exists(controllerFolderPath)) {
            try {
                val newPath = Files.createDirectories(kotlinSourcePath.resolve("controller"))
                controllerFolderPath = controllerFolderPath.resolve(newPath)
            } catch (e: Exception) {
                println("Error occurred while creating folder:" + e.message)
                return
            }
        } else {
            return
        }

        val controllerFile = File("$controllerFolderPath/${agentName.replaceFirstChar { it.titlecase() }}Controller.kt")
        this.sourceCode.createAgentControllerCode(agentName, controllerFile, packageName)
    }
}
