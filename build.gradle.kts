plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        compilations["main"].cinterops {
            val raylib by creating {
                defFile(project.file("src/nativeMain/cinterop/raylib.def"))
            }
        }
    }

    sourceSets {
        nativeMain.dependencies {
            implementation(libs.kotlinxSerializationJson)
        }
    }
}

tasks.register<Copy>("copyResourcesToOutput") {
    from("src/nativeMain/resources/sounds")
    into("build/bin/native/debugExecutable/sounds")
}
tasks.named("linkDebugExecutableNative") {
    finalizedBy("copyResourcesToOutput")
}

tasks.named<Exec>("runDebugExecutableNative") {
    workingDir = file("${layout.buildDirectory.get()}/bin/native/debugExecutable")
}
