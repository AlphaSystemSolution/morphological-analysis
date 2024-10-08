package com.alphasystem
package arabic
package cli

import arabic.morphologicalanalysis.morphology.persistence.DatabaseInit
import com.alphasystem.arabic.cli.asciidoc.GenerateDocumentCommand
import org.rogach.scallop.ScallopConf
import org.slf4j.bridge.SLF4JBridgeHandler

object ToolMain extends DatabaseInit {

  def main(args: Array[String]): Unit = {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()

    object Conf extends ScallopConf(args) {
      version(
        s"${BuildInfo.normalizedName} ${BuildInfo.version}"
      )

      addSubcommand(GenerateDocumentCommand(cacheFactory))
      verify()
    }

    Conf.subcommand match
      case Some(command: GenerateDocumentCommand) => command.buildDocument()
      case Some(command) =>
        Console.err.println(s"Unknown command: ${command.printedName}")
        Conf.printHelp()
      case None =>
        Console.err.println(s"No sub-command given")
        Conf.printHelp()
  }
}
