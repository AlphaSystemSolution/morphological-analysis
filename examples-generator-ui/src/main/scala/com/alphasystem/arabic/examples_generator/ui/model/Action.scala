package com.alphasystem
package arabic
package examples_generator
package ui
package model

sealed trait Action

object Action {
  case object NoAction extends Action
  case class AddRow(row: Row) extends Action
  case class AddColumn(columnData: ColumnData) extends Action
  case class UpdateColumn(columnData: ColumnData) extends Action
  case class SelectColumn(columnData: ColumnData) extends Action
}
