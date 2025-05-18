import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    java
}

subprojects {
    group = property("project.group").toString()
    version = property("project.version").toString()
    with(pluginManager) {
        apply(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        apply(rootProject.libs.plugins.kotlin.plugin.spring.get().pluginId)
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    configure<KotlinJvmProjectExtension> {
        jvmToolchain(21)
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }
    dependencies {
        implementation(rootProject.libs.kotlin.reflect)
    }
    tasks.test {
        useJUnitPlatform()
    }
}