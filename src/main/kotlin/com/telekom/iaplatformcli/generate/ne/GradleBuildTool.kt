package com.telekom.iaplatformcli.generate.ne

import com.telekom.agents.AgentConfig
import com.telekom.agents.ProjectConfig
import com.telekom.iaplatformcli.generate.build.GradleBuildWriter
import com.telekom.iaplatformcli.utils.FileUtil
import com.telekom.iaplatformcli.utils.FileUtil.resolveSrcPath
import java.nio.file.Path
import java.nio.file.Paths

class GradleBuildTool : BuildTool {
    override fun setupBuildTool(projectConfig: ProjectConfig, agentConfig: AgentConfig) {
        println("Setting up Gradle build tool.")

        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)
        val agentPackage = projectConfig.packageName

        FileUtil.createDirectories(projectDir)
        FileUtil.createDirectories(resolveSrcPath(projectDir))

        createBuildFiles(projectDir, agentPackage, projectConfig.projectName)

        FileUtil.copyResourceToDirectory("gradlew", projectDir.toString(), true)
    }

    private fun createBuildFiles(projectDir: Path, packageName: String, projectName: String) {
        GradleBuildWriter().createBuildFiles(projectDir, packageName, projectName)
    }
}