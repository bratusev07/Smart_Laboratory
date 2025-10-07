rootProject.name = "SmartLaboratory"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":ui:core")
include(":data:core")
include(":domain:core")


include(":feature:navigation-api")
include(":feature:navigation")
include(":feature:home")
include(":feature:settings")
include(":feature:login")
include(":feature:logcat")
include(":feature:customScreen")
include(":feature:addWidgetScreen")
include(":feature:allAreas")
include(":feature:area")
include(":shared")
