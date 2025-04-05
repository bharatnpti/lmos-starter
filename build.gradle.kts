plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.0.21" apply false
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "org.eclipse.lmos"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

