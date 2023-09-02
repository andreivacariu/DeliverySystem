
plugins {
    id("java")
}

group = "dev.vacariu.DeliverySystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://redempt.dev")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://mvnrepository.com/artifact/")
    maven {
        name = "citizens"
        url = uri("https://maven.citizensnpcs.co/repo")
    }

}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.github.Redempt:RedLib:6.5.8")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly("net.citizensnpcs:citizensapi:2.0.32-SNAPSHOT")

}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    tasks.withType<Jar> {
        outputs.dir(file("/home/andreivacariu/Downloads/test/plugins"))
        destinationDirectory.set(file("/home/andreivacariu/Downloads/test/plugins"))
    }
}
