import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.LinkOption.NOFOLLOW_LINKS

import NativePackagerKeys._
import com.typesafe.sbt.packager.archetypes.ServerLoader

packageArchetype.java_server

serverLoading in Debian := ServerLoader.Upstart

daemonUser in Debian := "root"

mainClass in Compile := Some("empty")

name := "debian-test"

version := "0.1.0"

maintainer := "Josh Suereth <joshua.suereth@typesafe.com>"

packageSummary := "Test debian package"

packageDescription := """A fun package description of our software,
  with multiple lines."""

defaultLinuxLogsLocation := "/non-standard/log"

InputKey[Unit]("check-softlink") <<= inputTask { (argTask: TaskKey[Seq[String]]) =>
  (argTask) map { (args: Seq[String]) =>
    assert(args.size >= 2, "Usage: check-softlink link to target")
    val link = Paths.get(args(0))
    val target = Paths.get(args(args.size - 1))
    assert(Files.exists(link, NOFOLLOW_LINKS),
        "Expected symbolic link '" + link + "' does not exist")
    assert(Files.isSymbolicLink(link), "'" + link + "' should be a symbolic link")
    assert(Files.readSymbolicLink(link) equals target,
        "Symbolic link '" + link + "' should point to '" + target + "'")
  }
}
