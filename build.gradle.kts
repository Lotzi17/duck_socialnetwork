
plugins {
    id("java")
    id ("org.openjfx.javafxplugin").version( "0.1.0")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain{
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx{
    version = "21.0.4"
    modules = listOf("javafx.controls", "javafx.fxml")
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.postgresql:postgresql:42.7.3")

}

tasks.test {
    useJUnitPlatform()
}
application {
    mainClass.set("lab2_map.main.Main")
}
