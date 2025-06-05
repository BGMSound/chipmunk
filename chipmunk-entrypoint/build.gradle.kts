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