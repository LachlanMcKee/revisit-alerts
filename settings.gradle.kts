enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "revisit-alerts-root"
include(
  ":revisit-alerts-sample",
  ":revisit-alerts-lint",
  ":revisit-alerts"
)
