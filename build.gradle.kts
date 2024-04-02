plugins {
    id("java")
}

group = "com.github.ioloolo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("com.google.guava:guava:33.1.0-jre")
}
