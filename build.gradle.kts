import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("org.springframework.boot") version "3.4.0"
//    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.0.21" apply false
}

//val springBootVersion by extra { "3.4.0" }

group = "com.telekom"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//    this.archiveFileName.set("lmos-cli.jar")
//}

