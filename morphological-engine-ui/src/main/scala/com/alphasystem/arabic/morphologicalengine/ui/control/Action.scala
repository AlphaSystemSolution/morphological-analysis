package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

sealed trait Action

enum GlobalAction extends Action {

  case None extends GlobalAction
  case Open extends GlobalAction
  case New extends GlobalAction
  case Save extends GlobalAction
  case SaveAs extends GlobalAction
}

enum TableAction extends Action {

  case None extends TableAction
  case Add extends TableAction
  case Delete extends TableAction
  case Duplicate extends TableAction
}
