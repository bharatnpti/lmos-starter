rootProject.name = "lmos-cli"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        repositories {
            fun String.findProperty() = System.getenv(this) ?: (if (extra.has(this)) extra[this].toString() else null)
            val mavenUser = "ONEAI_MAVEN_USER".findProperty()
            val mavenPassword = "ONEAI_MAVEN_TOKEN".findProperty()

//            maven {
//                name = "oneai"
//                url = java.net.URI("https://artifactory.devops.telekom.de/artifactory/oneai-maven/")
//                credentials {
//                    username = mavenUser
//                    password = mavenPassword
//                }
//            }
        }
    }
}
