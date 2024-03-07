package com.telekom.iaplatformcli.generate

import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories

class AgentGenerator(val sourceCode: KotlinSourceCode) {

    fun generateAgent(dirName: String, packageName: String, agentName: String, steps: List<String>) {
        //check dirname, if present , go into /src/main/kotlin
        val kotlinSourcePath = Path.of(dirName).resolve("src/main/kotlin").absolute()
        val slashedPackage = convertPackageToPath(packageName)
        var agentFolderPath = kotlinSourcePath.resolve(slashedPackage)


        //createMissingSubdirectories(agentFolderPath)
        if (!Files.exists(agentFolderPath)) {
            try {
                //first create a new folder,
                //val newPackageDirectory = //packageName.split(".").last()
                //create
                val newPath = Files.createDirectories(agentFolderPath)
                agentFolderPath = agentFolderPath.resolve(newPath)
            } catch (e: Exception) {
                println("Error occurred while creating folder:" + e.message)
                return
            }
        }

        //and then create the agent file inside it
        val agentFile: File = File("${agentFolderPath}/${agentName.capitalize()}.kt")

        this.sourceCode.createAgentCode(packageName, agentFile, agentName, steps)

    }

    private fun createMissingSubdirectories(path: Path) {
        val directory = File(path.toString())
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                println("Directories created successfully: $path")
            } else {
                println("Failed to create directories: $path")
            }
        } else {
            println("Directories already exist: $path")
        }
    }

    private fun convertPackageToPath(packageName: String): String {
        //package name is dot seperated, so convert to slash seperated
        return packageName.replace('.', '/');
    }
}