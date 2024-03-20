package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.constants.CliConstants
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolute
class AgentGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateAgent(dirName: String, packageName: String, agentName: String, steps: List<String>) {
        // check dirname, if present , go into /src/main/kotlin
        val kotlinSourcePath = Path.of(dirName).resolve("src/main/kotlin").absolute()
        var agentFolderPath = kotlinSourcePath

        if (Files.exists(kotlinSourcePath)) {
            try {
                // first create a new folder
                val newPackageDirectory = packageName.split(".").last()
                val newPath = Files.createDirectories(kotlinSourcePath.resolve(newPackageDirectory))
                agentFolderPath = agentFolderPath.resolve(newPath)
            } catch (e: Exception) {
                println("Error occurred while creating folder:" + e.message)
                return
            }
        } else {
            return
        }

        // first optionally create a AgentConstants.kt for storing constants
        // move constants to different class
        val agentConstantsFile = File("$agentFolderPath/AgentConstants.kt")
        if (!agentConstantsFile.exists()) {
            this.sourceCode.createAgentConstantsCode(packageName, agentConstantsFile, CliConstants.AGENT_CONSTANTS_CLASS_NAME)
        }

        // and then create the agent file inside it
        val agentFile = File("$agentFolderPath/${agentName.replaceFirstChar { it.titlecase() }}.kt")

        this.sourceCode.createAgentCode(packageName, agentFile, agentName, steps)

        println("Successfully generated code for agent: $agentName")
    }
}
