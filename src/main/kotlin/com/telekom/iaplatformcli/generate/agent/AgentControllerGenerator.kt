package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute

class AgentControllerGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateController(projectPath: String, basePackageName: String, classDir: String, agentName: String, agentsPackage: String) {
        val packageName = basePackageName.plus(".$classDir")
        val kotlinSourcePath = Path.of(projectPath).resolve("src/main/kotlin").absolute()
        val controllerFolderPath = FileUtil.createDirectoryStructure(kotlinSourcePath, classDir)

        val controllerFile = File("$controllerFolderPath/${agentName.replaceFirstChar { it.titlecase() }}Controller.kt")

        val customImports = mutableListOf("import $agentsPackage.*")
        this.sourceCode.createAgentControllerCode(agentName, controllerFile, packageName, customImports)
    }
}
