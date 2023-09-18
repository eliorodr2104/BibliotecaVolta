plugins {
    kotlin("multiplatform") version "1.8.10"
    application
    kotlin("plugin.serialization") version "1.8.10"
}

group = "me.elio0"
version = "1.0-SNAPSHOT"
var ktorversion = "2.3.0"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    google()
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting{
            dependencies {
                // Works as common dependency as well as the platform one
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
                implementation("com.googlecode.json-simple:json-simple:1.1.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-html-builder-jvm:2.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation("mysql:mysql-connector-java:5.1.6")
                implementation("io.ktor:ktor-server-netty:$ktorversion")
                implementation("io.ktor:ktor-server-core:$ktorversion")
                implementation("io.ktor:ktor-server-cio:$ktorversion")
                implementation("ch.qos.logback:logback-classic:1.3.6")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
                implementation("io.ktor:ktor-server-call-logging:$ktorversion")
                implementation("io.ktor:ktor-network-tls-certificates:$ktorversion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorversion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorversion")
                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("com.google.code.gson:gson:2.8.9")

            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("me.elio0.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

dependencies {
    implementation ("com.sun.mail:javax.mail:1.6.2")
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}
