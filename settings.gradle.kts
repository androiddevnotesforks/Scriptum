pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Scriptum"

include(":app")

include(":libraries:safeDialog")
include(":libraries:iconAnim")
include(":libraries:textDotAnim")
include(":libraries:extensions")

include(":tests:common")
include(":tests:idling")
include(":tests:cappuccino")
include(":tests:prod")