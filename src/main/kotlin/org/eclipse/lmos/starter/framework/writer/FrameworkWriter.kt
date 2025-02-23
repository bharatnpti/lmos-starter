package org.eclipse.lmos.starter.framework.writer

import org.eclipse.lmos.starter.config.AgentConfig
import org.eclipse.lmos.starter.config.ProjectConfig
import org.eclipse.lmos.starter.constants.SpringBootScriptGenerator
import org.eclipse.lmos.starter.template.engine.TemplateEngine
import org.eclipse.lmos.starter.utils.FileUtil
import org.eclipse.lmos.starter.utils.FileUtil.resolveSrcPath
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

interface FrameworkWriter {
    fun createFrameworkFiles(projectConfig: ProjectConfig, agentConfig: AgentConfig)
    fun getProjectType(): String
}

abstract class AbstractFrameworkWriter(private val buildType: String) : FrameworkWriter {

    override fun getProjectType(): String = buildType

    protected fun writeToFile(filePath: Path, content: String) {
        Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).use { writer ->
            writer.write(content)
        }
    }
}

class SpringFrameworkWriter(templateEngine: TemplateEngine) : AbstractFrameworkWriter("SPRING") {

    private val springBootScriptGenerator = SpringBootScriptGenerator(templateEngine)

    override fun createFrameworkFiles(projectConfig: ProjectConfig, agentConfig: AgentConfig) {

        val projectDir = Paths.get(projectConfig.projectDir).resolve(projectConfig.projectName)

        val className = FileUtil.getMainApplicationName(projectConfig.projectName)
        val mainFilePath = resolveSrcPath(projectDir).resolve("$className.kt")
        writeToFile(mainFilePath, springBootScriptGenerator.generateSpringBootApplication(projectConfig.packageName, className))

        val resourceFolderPath = projectDir.resolve("src").resolve("main").resolve("resources")
        writeToFile(resourceFolderPath.resolve("application.yml"), springBootScriptGenerator.generateApplicationYaml())

        val agentDir = agentConfig.agentFolder ?: projectDir
        val agentFile = agentDir.resolve("${agentConfig.name}.agent.kts")
        writeToFile(agentFile, springBootScriptGenerator.generateAgent(agentConfig))
        println("Successfully generated code for agent: $agentConfig")
    }

}
