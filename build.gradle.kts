plugins {
    id("java")
    id("io.freefair.lombok") version "6.2.0"
}

group = "com.ooov.burp"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.portswigger.burp.extensions:montoya-api:2023.9")
    //lombok
    implementation("org.projectlombok:lombok:1.18.22")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Title"] = "Burp Extension"
        attributes["Implementation-Version"] = version
    }
}