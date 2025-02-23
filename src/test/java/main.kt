//import com.telekom.iaplatformcli.constants.BuildScriptGenerator
//
//fun main() {
//    val generator = BuildScriptGenerator()
//
//    val gradlePlugins = generator.generateGradlePlugins()
//    val repositories = generator.generateRepositories()
//    val dependencies = generator.generateDependencies()
//    val gradleTasks = generator.generateGradleTasks("testJar")
//
//    val buildScript = """
//$gradlePlugins
//
//$repositories
//
//$dependencies
//
//$gradleTasks
//    """.trimIndent()
//
//    println(buildScript)
//}