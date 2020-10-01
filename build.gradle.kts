plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    application
}

group = "team.false_"
version = "1.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
//    testImplementation("junit", "junit", "4.12")
    implementation("net.dv8tion", "JDA", "4.2.0_198") { exclude(module = "opus-java") }
    implementation("club.minnced", "jda-reactor", "1.2.0")
    implementation("org.apache.logging.log4j", "log4j-api", "2.13.3")
    runtimeOnly("org.apache.logging.log4j", "log4j-core", "2.13.3")
    implementation("org.springframework.data", "spring-data-jpa", "2.3.3.RELEASE")
    implementation("org.apache.deltaspike.modules", "deltaspike-data-module-api", "1.9.4")
    runtimeOnly("org.apache.deltaspike.modules", "deltaspike-data-module-impl", "1.9.4")
    implementation("org.hibernate", "hibernate-core", "5.4.21.Final")
    implementation("org.hibernate", "hibernate-c3p0", "5.4.21.Final")
    runtimeOnly("com.h2database", "h2", "1.4.200")
}

kapt.includeCompileClasspath = false

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
    compileTestKotlin { kotlinOptions.jvmTarget = "1.8" }
    withType<JavaCompile> {
        options.isFork = true
        options.isIncremental = true
    }
    register("fatJar", Jar::class) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes("Main-Class" to application.mainClassName) }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(sourceSets.main.get().output)
    }
}

application {
    applicationName = rootProject.name.toLowerCase()
    mainClassName = "${project.group}.$applicationName.Main"
}
