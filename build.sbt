name := "email_spam"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
                      "org.apache.spark" %% "spark-core" % "1.2.1",
       		      "org.apache.spark" % "spark-mllib_2.11" % "1.2.1"
                    )
