package com.telekom.iaplatformcli.generate.sourcecode

import java.io.File
import java.io.Writer
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path

interface SourceCode {
    fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>)
}

class DefaultSourceCode(val indentingWriter: Writer) : SourceCode {

    override fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>) {
        TODO("Not yet implemented")
    }

}
