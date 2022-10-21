package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package service

import morphology.model.{ Location, Token }

case class SaveRequest(token: Token, locations: List[Location])
