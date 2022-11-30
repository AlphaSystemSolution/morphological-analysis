package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.typesafe.config.Config

package object nitrite {

  extension (src: Config) {
    def getOptionalString(path: String): Option[String] = if src.hasPath(path) then Some(src.getString(path)) else None
  }
}
