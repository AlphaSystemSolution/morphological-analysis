package com.alphasystem
package arabic
package cli

import org.rogach.scallop.{ ScallopOption, Subcommand }

import java.nio.file.Path

abstract class BaseCommand(commandNameAndAliases: String*) extends Subcommand(commandNameAndAliases*) {

  val srcPath: ScallopOption[Path] = opt[Path](
    descr = "Path to source yaml file",
    required = true
  )

  val destPath: ScallopOption[Path] = opt[Path](
    descr = "Path to dest adoc file",
    required = true
  )

  val attributesPath: ScallopOption[Path] = opt[Path](
    descr = "Path to header attributes file",
    default = None,
    required = false
  )

  def buildDocument(): Unit
}
