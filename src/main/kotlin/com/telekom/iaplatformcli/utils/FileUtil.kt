package com.telekom.iaplatformcli.utils

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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
    }
}
