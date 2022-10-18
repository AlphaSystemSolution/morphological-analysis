package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token

package object cache {

  extension (token: Token)
    def toLocationRequest: LocationRequest = LocationRequest(token.chapterNumber, token.verseNumber, token.tokenNumber)
}
