package org.eclipse.lmos.starter.config

import java.nio.file.Path

data class AgentConfig(
    val name: String,
    val model: String,
    val description: String,
    val prompt: String
)