package com.telekom.iaplatformcli.utils

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class FileUtil {

    companion object {
        fun copyResourceToDirectory(resourceName: String, projectPath: String) {
            val classLoader = FileUtil::class.java.classLoader
            val inputStream: InputStream? = classLoader.getResourceAsStream(resourceName)

            val destinationFile = File(projectPath, File(resourceName).name)
            val outputStream = FileOutputStream(destinationFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input?.copyTo(output)
                }
            }
            destinationFile.setExecutable(true)
        }

        fun getMainApplicationName(mainProjectName: String): String {
            return "${mainProjectName.replaceFirstChar { it.titlecase() }}Application"
        }

        fun createDirectoryStructure(basePath: Path, dirName: String): String {
            val newDirectoryPath = basePath.resolve(dirName)

            try {
                if (!Files.exists(newDirectoryPath)) {
                    Files.createDirectories(newDirectoryPath)
                }
            } catch (e: Exception) {
                println("Error occurred while creating folder:" + e.message)
                return basePath.toString()
            }

            return newDirectoryPath.toString()
        }
    }
}
