organization := "nserfontein.hello-event-modeling"
name := "kvstore"
version := "1.0"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"     % akkaVersion,
  "com.typesafe.akka"        %% "akka-testkit"   % akkaVersion % Test,
  "com.novocode"             % "junit-interface" % "0.11"      % Test
)
