package com.telekom.iaplatformcli.constants
  
import com.github.mustachejava.DefaultMustacheFactory
import com.telekom.agents.AgentConfig
import java.io.Reader
import java.io.StringWriter

object BuildScriptGenerator {
  
    private val mustacheFactory = DefaultMustacheFactory()
  
    fun generateGradlePlugins(packageName: String): String {
        val context = mapOf(
            "kotlinVersion" to Versions.KOTLIN_VERSION,
            "springBootVersion" to Versions.SPRING_BOOT_VERSION,
            "dependencyManagementVersion" to Versions.DEPENDENCY_MANAGEMENT_VERSION,
            "graalvmVersion" to Versions.GRAALVM_VERSION,
            "groupId" to packageName,
            "version" to Versions.STARTER_VERSION,
            "javaVersion" to Versions.JAVA_VERSION
        )  
        return renderTemplate("templates/gradle/plugins.mustache", context)
    }  
  
    fun generateRepositories(): String {
        val context = mapOf(
            "snapshotRepo" to "https://oss.sonatype.org/content/repositories/snapshots/",
            "weeklyRepo" to "https://oss.sonatype.org/service/local/repositories/orgeclipselmos-1003/content/"
        )
        return renderTemplate("templates/gradle/repositories.mustache", context)
    }
  
    fun generateDependencies(): String {  
        val context = mapOf(  
            "arcVersion" to Versions.ARC_VERSION,
            "azureIdentityVersion" to Versions.AZURE_IDENTITY_VERSION,
            "graphqlJavaVersion" to Versions.GRAPHQL_JAVA_VERSION,
            "testcontainersVersion" to Versions.TESTCONTAINERS_VERSION,
            "micrometerVersion" to Versions.MICROMETER_VERSION
        )  
        return renderTemplate("templates/gradle/dependencies.mustache", context)
    }

    fun generateGradleTasks(jarFileName: String): String {
        val context = mapOf(
            "jarFileName" to jarFileName
        )
        return renderTemplate("templates/gradle/tasks.mustache", context)
    }

    private fun renderTemplate(templatePath: String, context: Map<String, Any>): String {
        getResourceReader(templatePath).use { reader ->
            val mustache = mustacheFactory.compile(reader, templatePath)
            return StringWriter().use { writer ->
                mustache.execute(writer, context).flush()
                writer.toString().trimIndent()
            }
        }
    }

    private fun getResourceReader(resourcePath: String): Reader {
        return Thread.currentThread().contextClassLoader.getResourceAsStream(resourcePath)?.reader()
            ?: throw IllegalArgumentException("Resource $resourcePath not found.")
    }


    fun generateGradleWrapperProperties(): String {
        val context = mapOf(
            "distributionBase" to "GRADLE_USER_HOME",
            "distributionPath" to "wrapper/dists",
            "distributionUrl" to "https://services.gradle.org/distributions/gradle-8.5-bin.zip",
            "networkTimeout" to 10000,
            "validateDistributionUrl" to true,
            "zipStoreBase" to "GRADLE_USER_HOME",
            "zipStorePath" to "wrapper/dists"
        )

        return renderTemplate("templates/gradle/wrapper.mustache", context)
    }

    fun generateAgent(agentConfig: AgentConfig): String {
        val context = mapOf(
            "agentName" to agentConfig.name,
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
}  