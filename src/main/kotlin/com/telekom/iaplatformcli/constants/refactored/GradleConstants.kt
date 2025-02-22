package com.telekom.iaplatformcli.constants.refactored
  
class GradleConstants {  
    companion object {  
        const val GRADLE_TASKS = """  
            java {  
                sourceCompatibility = JavaVersion.VERSION_21  
            }  
  
            tasks.withType<KotlinCompile> {  
                kotlinOptions {  
                    freeCompilerArgs += "-Xjsr305=strict"  
                    jvmTarget = "21"  
                }  
            }  
  
            tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {  
                this.archiveFileName.set("%s.jar")  
            }  
        """  
    }  
}  