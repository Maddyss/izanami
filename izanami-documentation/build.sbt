import sbt.project

lazy val `izanami-documentation` = (project in file("."))
  .enablePlugins(ParadoxPlugin, DitaaPlugin)
  .settings(
    name := "Izanami",
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    paradoxProperties in Compile ++= Map(
      "download_zip.base_url" -> s"https://github.com/maif/izanami/release/download/v-${version}/izanami.zip",
      "download_jar.base_url" -> s"https://github.com/maif/izanami/release/download/v-${version}/izanami.jar",
      "extref.diagram.base_url" -> s"${(target.value / "ditaa").getAbsolutePath}/%s"
    ),
    ditaaDestination := (sourceDirectory.value / "main" / "paradox" / "img" / "diagrams"),
    watchSources ++= Seq(
      sourceDirectory.value / "main" / "ditaa",
      sourceDirectory.value / "main" / "paradox"
    )
  )


lazy val generateDoc = taskKey[Unit]("Copy doc")

generateDoc := {
  val p = project
  //IO.delete(ditaaDestination.value)
  (ditaa in Compile).value
  (paradox in Compile).value
  val paradoxFile = target.value / "paradox" / "site" / "main"
  val targetDocs = p.base.getParentFile / "docs"
  IO.delete(targetDocs)
  IO.copyDirectory(paradoxFile, targetDocs)
}

