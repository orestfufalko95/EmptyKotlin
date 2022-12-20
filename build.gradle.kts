import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.eclipse.jgit.api.Git

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        google()
    }

    dependencies {
        classpath("org.eclipse.jgit:org.eclipse.jgit:5.0.1.201806211838-r")
    }
}

plugins {
    kotlin("jvm") version "1.6.21"
//    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

//application {
//    mainClass.set("MainKt")
//}

tasks {

    register("generateGitRepo") {

        inputs.dir("src")
        inputs.files("build.gradle.kts", "settings.gradle.kts")

        val gitRepoDir = file("$buildDir/git-repo")
        outputs.dir(gitRepoDir)

        doLast {
            delete(gitRepoDir)
            delete(temporaryDir)

            val bareUri = Git.init().setDirectory(gitRepoDir).setBare(true).call().use { bare ->
                bare.repository.directory.toURI().toString()
            }

            Git.cloneRepository().setURI(bareUri).setDirectory(temporaryDir).call().use { clone ->
                copy {
                    from(".") {
                        include("src/**")
                        include("*.gradle.kts")
                    }
                    into(temporaryDir)
                }

                clone.add()
                    .addFilepattern("build.gradle.kts")
                    .addFilepattern("settings.gradle.kts")
                    .addFilepattern("src")
                    .call()
                clone.commit()
                    .setMessage("Initial import")
                    .setAuthor("name", "email")
                    .call()

                clone.push().call()
            }
        }
    }
}