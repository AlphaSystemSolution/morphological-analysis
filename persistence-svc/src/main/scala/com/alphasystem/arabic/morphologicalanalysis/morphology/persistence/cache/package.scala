package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.model.Token

package object cache {

  extension (token: Token)
    def toLocationRequest: LocationRequest = LocationRequest(token.chapterNumber, token.verseNumber, token.tokenNumber)
}
