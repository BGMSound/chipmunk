plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

allprojects {
    dependencies {
        implementation(rootProject.projects.core)
        runtimeOnly(rootProject.projects.advice)
    }
}

dependencies {
    rootProject.subprojects.filter { it.path != project.path }.forEach {
        implementation(it)
    }
}

tasks.bootJar {
    archiveBaseName.set(project.rootProject.name)
    archiveVersion.set(rootProject.version.toString())
    archiveClassifier.set("")
    destinationDirectory.set(rootProject.layout.buildDirectory.asFile.get().resolve("libs"))
}

jib {
    val imageTag = property("project.version").toString()
    val imageName = property("project.image").toString()
    from {
        image = "openjdk:21-jdk"
    }
    to {
        image = "$imageName:$imageTag"
        tags = setOf("latest", imageTag)
        auth {
            username = System.getenv("DOCKERHUB_USERNAME")
            password = System.getenv("DOCKERHUB_TOKEN")
        }
    }
    container {
        jvmFlags = listOf(
            "-Xms512m",
            "-Xmx512m",
        )
    }
}