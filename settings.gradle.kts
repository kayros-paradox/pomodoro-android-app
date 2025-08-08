pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
//        maven {
//            url = uri(path = "https://artifactory-external.vkpartner.ru/artifactory/maven")
//        }
    }
}

rootProject.name = "Pomodoro"
include(":app")
