plugins {
    id("java-library")
    id("io.freefair.lombok") version "8.4"
    checkstyle

}

group = "com.zikeyang.contube"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
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
            implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
            implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.3")
            implementation("com.github.rholder:guava-retrying:2.0.0")
        }
        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("org.slf4j:slf4j-simple:2.0.9")

        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
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

        runtimeOnly(project(":file"))
    }
}

project(":file") {
    dependencies {
        implementation(project(":common"))

        implementation("com.fasterxml.jackson.core:jackson-databind")
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    }
}
