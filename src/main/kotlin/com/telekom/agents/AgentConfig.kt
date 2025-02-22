package com.telekom.agents

import java.nio.file.Path

data class AgentConfig(
    val name: String,
    val model: String,
    val description: String,
    val prompt: String,
    val agentFolder: Path?
)