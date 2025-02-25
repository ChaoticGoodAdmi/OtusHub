plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    application
}

group = "ru.ushakov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.vavr:vavr:0.10.4")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("net.datafaker:datafaker:1.5.0")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.3.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    archiveFileName.set("OtusHub.jar")
}

tasks.named("bootDistZip") {
    dependsOn(tasks.named("jar"))
}

tasks.named("bootDistTar") {
    dependsOn(tasks.named("jar"))
}

tasks.named("bootStartScripts") {
    dependsOn(tasks.named("jar"))
}

tasks.named("bootJar") {
    dependsOn(tasks.named("jar"))
}

tasks.named("startScripts") {
    dependsOn(tasks.named("bootJar"))
}

application {
    mainClass.set("ru.ushakov.otushub.OtusHubApplicationKt")
}
