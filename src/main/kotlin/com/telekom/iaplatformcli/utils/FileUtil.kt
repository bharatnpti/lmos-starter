package com.telekom.iaplatformcli.utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

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

        fun createDirectoryStructure(basePath: Path, dirName: String): Path {
            val newDirectoryPath = basePath.resolve(dirName)

            return try {
                if (Files.notExists(newDirectoryPath)) {
                    Files.createDirectories(newDirectoryPath)
                }
                newDirectoryPath
            } catch (e: IOException) {
                println("Error occurred while creating folder: ${e.message}")
                basePath
            }
        }
}
