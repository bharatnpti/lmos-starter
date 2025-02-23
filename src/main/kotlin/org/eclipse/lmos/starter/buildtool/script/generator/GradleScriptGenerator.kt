package org.eclipse.lmos.starter.buildtool.script.generator

import org.eclipse.lmos.starter.config.Versions
import org.eclipse.lmos.starter.template.engine.TemplateEngine
import java.io.Reader

class GradleScriptGenerator(private val templateEngine: TemplateEngine) {

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
        val context = mapOf("jarFileName" to jarFileName)
        return renderTemplate("templates/gradle/tasks.mustache", context)
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
