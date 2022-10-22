package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import java.lang.Enum
enum LocationType extends Enum[LocationType] {

  case Prefix extends LocationType
  case Stem extends LocationType
  case Suffix extends LocationType
}
