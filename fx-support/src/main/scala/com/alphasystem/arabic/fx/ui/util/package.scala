package com.alphasystem
package arabic
package fx
package ui

import java.nio.file.{ Path, Paths }

package object util {

  private val UserDirName: String = System.getProperty("user.dir", ".")
  val UserHome: String = System.getProperty("user.home", UserDirName)
  val UserDir: Path = Paths.get(UserDirName)
  val UserHomeDir: Path = Paths.get(UserHome)
}
