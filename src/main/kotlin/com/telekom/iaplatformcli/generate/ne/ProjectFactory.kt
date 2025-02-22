package com.telekom.iaplatformcli.generate.ne

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig

interface BuildTool {
    fun setupBuildTool(projectConfig: ProjectConfig, agentConfig: AgentConfig)
}

interface Framework {
    fun setupFramework(projectConfig: ProjectConfig, agentConfig: AgentConfig)
}

interface ProjectFactory {
    fun createBuildTool(): BuildTool
    fun createFramework(): Framework
}