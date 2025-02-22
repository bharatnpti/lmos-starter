package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.constants.CliConstants
import com.telekom.iaplatformcli.constants.CliConstants.Companion.SRC_MAIN_KOTLIN
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute
class AgentGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateAgent(projectPath: String, basePackageName: String, classDir: String, agentName: String, steps: List<String>) {
        val packageName = basePackageName.plus(".$classDir")
        // check dirname, if present , go into /src/main/kotlin
        val kotlinSourcePath = Path.of(projectPath).resolve(SRC_MAIN_KOTLIN).absolute()
        val agentFolderPath = FileUtil.createDirectoryStructure(kotlinSourcePath, classDir)

        val agentConstantsFile = File("$agentFolderPath/AgentConstants.kt")
        if (!agentConstantsFile.exists()) {
            this.sourceCode.createAgentConstantsCode(packageName, agentConstantsFile, CliConstants.AGENT_CONSTANTS_CLASS_NAME)
        }

        // and then create the agent file inside it
        val agentFile = File("$agentFolderPath/${agentName.replaceFirstChar { it.titlecase() }}.kt")

        val customImports = mutableListOf("import ${basePackageName.plus(".step.*")}", "import $packageName.*")
        this.sourceCode.createAgentCode(packageName, agentFile, agentName, steps, customImports)

        println("Successfully generated code for agent: $agentName")
    }
}
