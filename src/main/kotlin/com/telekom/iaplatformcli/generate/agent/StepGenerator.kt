package com.telekom.iaplatformcli.generate.agent

import com.telekom.iaplatformcli.generate.sourcecode.KotlinSourceCode
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolute

class StepGenerator(private val sourceCode: KotlinSourceCode) {

    fun generateStep(dirName: String, packageName: String, stepName: String) {
        val kotlinSourcePath = Path.of(dirName).resolve("src/main/kotlin").absolute()
        var stepPath = kotlinSourcePath

        if (Files.exists(stepPath)) {
            try {
                val newPath = Files.createDirectories(kotlinSourcePath.resolve("step"))
                stepPath = stepPath.resolve(newPath)
            } catch (e: Exception) {
                println("Error occurred while creating folder:" + e.message)
                return
            }
        } else {
            return
        }

        val stepFile = File("$stepPath/${stepName.replaceFirstChar { it.titlecase() }}.kt")
        this.sourceCode.createResponseStepCode(stepName, stepFile, packageName)
    }
}
