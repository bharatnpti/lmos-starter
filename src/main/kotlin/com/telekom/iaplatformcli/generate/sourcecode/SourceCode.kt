package com.telekom.iaplatformcli.generate.sourcecode

import java.io.File
import java.io.Writer

interface SourceCode {
    fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>)
    fun createAgentConstantsCode(packageName: String, agentConstantsFile: File, agentConstantsClass: String)
    fun createAgentControllerCode(agentName: String, controllerFile: File, packageName: String)
    fun createResponseStepCode(stepName: String, stepFile: File, packageName: String)
}

class DefaultSourceCode(val indentingWriter: Writer) : SourceCode {

    override fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>) {
        TODO("Not yet implemented")
    }

    override fun createAgentConstantsCode(packageName: String, agentConstantsFile: File, agentConstantsClass: String) {
        TODO("Not yet implemented")
    }

    override fun createAgentControllerCode(agentName: String, controllerFile: File, packageName: String) {
        TODO("Not yet implemented")
    }

    override fun createResponseStepCode(stepName: String, stepFile: File, packageName: String) {
        TODO("Not yet implemented")
    }
}
