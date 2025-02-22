package com.telekom.iaplatformcli.utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories

object FileUtil {

        fun copyResourceToDirectory(resourceName: String, projectPath: String, isExecutable: Boolean) {
            val classLoader = FileUtil::class.java.classLoader
            val inputStream = classLoader.getResourceAsStream(resourceName)
                ?: throw IOException("Resource not found: $resourceName")

            val destinationPath = Path.of(projectPath, resourceName)

            inputStream.use { input ->
                Files.newOutputStream(destinationPath).use { output ->
                    input.copyTo(output)
                }
            }

            if(isExecutable) {
                destinationPath.toFile().setExecutable(true)
            }
        }

        fun getMainApplicationName(mainProjectName: String): String {
            return "${mainProjectName.replaceFirstChar { it.uppercaseChar() }}Application"
        }

    fun createDirectories(path: Path) {
        path.createDirectories()
    }

    fun resolveSrcPath(projectDir: Path): Path = projectDir.resolve("src").resolve("main").resolve("kotlin")
}
