plugins {
    id("java-library")
    id("io.freefair.lombok") version "8.4"
    `maven-publish`
    checkstyle

}

repositories {
    mavenCentral()
}

allprojects {
    buildDir = File("${rootProject.buildDir}/${name}")
    group = "io.github.contube-org"
    version = "1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "maven-publish")
    repositories {
        mavenCentral()
    }

    checkstyle {
        toolVersion = "10.12.4"
        configFile = file("${project.rootDir}/checkstyle/checkstyle.xml")
        isShowViolations = true
    }

    dependencies {
        constraints {
            api("org.apache.logging.log4j:log4j-api:2.14.1")
            implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
            implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
            implementation("com.github.rholder:guava-retrying:2.0.0")
            implementation("org.apache.logging.log4j:log4j-core:2.14.1")
        }
        api("org.apache.logging.log4j:log4j-api")

        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }

    publishing {
        publications {
            create<MavenPublication>("ConTube") {
                from(components["java"])
                artifactId = "contube-" + project.name
                version = project.version.toString()
            }
        }
    }
}

project(":api") {

}

project(":common") {
    dependencies {
        api(project(":api"))
        api("com.fasterxml.jackson.core:jackson-databind")
        api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    }
}

project(":runtime") {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        implementation(project(":api"))
        implementation(project(":common"))

        implementation("com.github.rholder:guava-retrying")
        runtimeOnly("org.apache.logging.log4j:log4j-core")

        runtimeOnly(project(":file"))
    }
    tasks.register<Copy>("copyDependencies") {
        from(configurations.runtimeClasspath)
        into("${project.rootDir}/libs")
    }
    tasks.named("jar") {
        dependsOn("copyDependencies")
    }
}

project(":file") {
    dependencies {
        implementation(project(":common"))

        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    }
}
