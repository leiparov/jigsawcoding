name := "jigsawcoding"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

play.Project.playJavaSettings

play.Keys.lessEntryPoints <<= baseDirectory { base =>
    (base / "app" / "assets" / "css" ** "main.less") +++
    (base / "app" / "assets" / "css" ** "login.less")
}

templatesImport += "_root_.utils._"

val appDependencies = Seq(
    // Add your project dependencies here,
    "mysql" % "mysql-connector-java" % "5.1.19"
    //"postgresql" % "postgresql" % "9.3-1100.jdbc4"    
)

  