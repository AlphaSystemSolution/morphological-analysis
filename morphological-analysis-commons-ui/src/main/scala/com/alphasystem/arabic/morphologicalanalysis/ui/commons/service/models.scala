package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package commons
package service

import morphology.graph.model.{ DependencyGraph, GraphNode }
import morphology.model.{ Location, Token }

case class SaveRequest(token: Token, locations: List[Location])
