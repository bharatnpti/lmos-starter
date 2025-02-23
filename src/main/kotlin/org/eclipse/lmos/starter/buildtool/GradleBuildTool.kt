package org.eclipse.lmos.starter.buildtool

import org.eclipse.lmos.starter.buildtool.writer.GradleBuildWriter
import org.eclipse.lmos.starter.config.AgentConfig
import org.eclipse.lmos.starter.config.ProjectConfig
import org.eclipse.lmos.starter.template.engine.TemplateEngine
import org.eclipse.lmos.starter.factory.BuildTool
import org.eclipse.lmos.starter.utils.FileUtil
import org.eclipse.lmos.starter.utils.FileUtil.resolveSrcPath
import java.nio.file.Path
import java.nio.file.Paths

class GradleBuildTool : BuildTool {
    override fun setupBuildTool(
        projectConfig: ProjectConfig,
        agentConfig: AgentConfig,
        templateEngine: TemplateEngine
    ) {
        println("Setting up Gradle build tool.")

        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)
        val agentPackage = projectConfig.packageName

        FileUtil.createDirectories(projectDir)
        FileUtil.createDirectories(resolveSrcPath(projectDir))

        createBuildFiles(projectDir, agentPackage, projectConfig.projectName, templateEngine)

        FileUtil.copyResourceToDirectory("gradlew", projectDir, true)
        FileUtil.copyResourceToDirectory("gradlew.bat", projectDir, true)
    }

    private fun createBuildFiles(
        projectDir: Path,
        packageName: String,
        projectName: String,
        templateEngine: TemplateEngine
    ) {
        GradleBuildWriter(templateEngine).createBuildFiles(projectDir, packageName, projectName)
    }
}