package com.alphasystem
package morphologicalanalysis
package morphology
package model

import java.lang.Enum
enum LocationType extends Enum[LocationType] {

  case PREFIX extends LocationType
  case STEM extends LocationType
  case SUFFIX extends LocationType
}
