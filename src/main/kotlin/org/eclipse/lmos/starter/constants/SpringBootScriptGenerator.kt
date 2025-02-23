package org.eclipse.lmos.starter.constants

import org.eclipse.lmos.starter.config.AgentConfig
import org.eclipse.lmos.starter.template.engine.TemplateEngine
import java.io.Reader

class SpringBootScriptGenerator(private val templateEngine: TemplateEngine) {

    fun generateAgent(agentConfig: AgentConfig): String {
        val context = mapOf(
            "agentName" to agentConfig.name.replace(Regex("[^a-zA-Z]"), ""),
            "description" to agentConfig.description,
            "model" to agentConfig.model,
            "prompt" to agentConfig.prompt
        )
        return renderTemplate("templates/spring/agent.mustache", context)
    }

    fun generateSpringBootApplication(packageName: String, className: String): String {
        val context = mapOf(
            "packageName" to packageName,
            "className" to className
        )
        return renderTemplate("templates/spring/application_class.mustache", context)
    }

    fun generateApplicationYaml(): String {
        return renderTemplate("templates/spring/yaml.mustache", emptyMap())
    }

    private fun renderTemplate(templatePath: String, context: Map<String, Any>): String {
        getResourceReader(templatePath).use { reader ->
            return templateEngine.render(reader, templatePath, context)
        }
    }

    private fun getResourceReader(resourcePath: String): Reader {
        return Thread.currentThread().contextClassLoader.getResourceAsStream(resourcePath)?.reader()
            ?: throw IllegalArgumentException("Resource $resourcePath not found.")
    }
}
