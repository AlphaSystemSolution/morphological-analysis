package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import graph.model.DependencyGraph
import morphology.model.{ Location, Token }

package object cache {

  type DependencyGraphResponse = Map[(Int, String), Map[Int, Seq[DependencyGraph]]]

  extension (token: Token)
    def toLocationRequest: LocationRequest = LocationRequest(token.chapterNumber, token.verseNumber, token.tokenNumber)

  extension (location: Location)
    def toLocationRequest: LocationRequest =
      LocationRequest(location.chapterNumber, location.verseNumber, location.tokenNumber)
}
