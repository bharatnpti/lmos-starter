package com.telekom.iaplatformcli.service.writer

import java.io.File

class IndentedWriter(private val file: File) {
    private val indentSize = 4
    private var currentIndent = 0

    fun writeLine(line: String) {
        file.appendText("${" ".repeat(currentIndent)}$line\n")
    }

    fun increaseIndent() {
        currentIndent += indentSize
    }

    fun decreaseIndent() {
        currentIndent = (currentIndent - indentSize).coerceAtLeast(0)
    }
}
