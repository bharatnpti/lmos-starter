package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.constants.CliConstants
import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import com.telekom.iaplatformcli.utils.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute

class StepGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateStep(projectPath: String, basePackageName: String, classDir: String, stepName: String) {
        val packageName = basePackageName.plus(".$classDir")
        // make sure there is a kotlin src dir
        val kotlinSourcePath = Path.of(projectPath).resolve(CliConstants.SRC_MAIN_KOTLIN).absolute()
        val stepPath = FileUtil.createDirectoryStructure(kotlinSourcePath, classDir)

        val stepFile = File("$stepPath/${stepName.replaceFirstChar { it.titlecase() }}.kt")
        this.sourceCode.createResponseStepCode(stepName, stepFile, packageName)
    }
}
