package com.telekom.iaplatformcli.generate.ne

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.constants.TemplateEngine

interface BuildTool {
    fun setupBuildTool(projectConfig: ProjectConfig, agentConfig: AgentConfig, templateEngine: TemplateEngine)
}

interface Framework {
    fun setupFramework(projectConfig: ProjectConfig, agentConfig: AgentConfig, templateEngine: TemplateEngine)
}

interface ProjectFactory {
    fun createBuildTool(): BuildTool
    fun createFramework(): Framework
}