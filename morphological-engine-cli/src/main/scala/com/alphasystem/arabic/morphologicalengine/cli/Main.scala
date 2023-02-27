package com.alphasystem
package arabic
package morphologicalengine
package cli

import org.rogach.scallop.ScallopConf
import org.slf4j.bridge.SLF4JBridgeHandler

object Main {

  def main(args: Array[String]): Unit = {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()

    object Conf extends ScallopConf(args) {
      version(
        s"${BuildInfo.normalizedName} ${BuildInfo.version}"
      )

      verify()
    }
  }
}
