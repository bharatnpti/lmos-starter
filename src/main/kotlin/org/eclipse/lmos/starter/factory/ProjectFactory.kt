package org.eclipse.lmos.starter.factory

import org.eclipse.lmos.starter.config.AgentConfig
import org.eclipse.lmos.starter.config.ProjectConfig
import org.eclipse.lmos.starter.template.engine.TemplateEngine

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