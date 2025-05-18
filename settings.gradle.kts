rootProject.name = ext["project.name"].toString()

fun includeModule(parentDirectory: String, name: String) {
    val modulePath = ":$name"
    include(modulePath)

    val projectDirectory = rootProject.projectDir
    project(modulePath).projectDir = projectDirectory.resolve(parentDirectory)
}

fun autoIncludeChild(
    parentDirectory: String,
    parentModuleName: String,
    classifier: String? = null
) {
    includeModule(parentDirectory, parentModuleName)

    val projectDirectory = rootProject.projectDir
    val childModuleDirectories = projectDirectory
        .resolve(parentDirectory)
        .listFiles()
        ?.filter { it.isDirectory && it.resolve("build.gradle.kts").exists() } ?: emptyList()
    childModuleDirectories.forEach { childModuleDirectory ->
        val modulePath = if (classifier == null) {
            ":$parentModuleName:${childModuleDirectory.name}-${parentModuleName.replace(":", "-")}"
        } else {
            ":$parentModuleName:${childModuleDirectory.name}-$classifier"
        }
        include(modulePath)
        project(modulePath).projectDir = childModuleDirectory
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")