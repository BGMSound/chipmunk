dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))
    implementation("org.springframework.ai:spring-ai-starter-model-mistral-ai")
    implementation(libs.github.api)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.jackson.kotlin)
    runtimeOnly(libs.mysql)
}