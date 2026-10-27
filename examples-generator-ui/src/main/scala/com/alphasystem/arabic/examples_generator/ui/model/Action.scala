package com.alphasystem
package arabic
package examples_generator
package ui
package model

import examples_generator.model.{ ColumnData, TableProperties }

import java.util.UUID

sealed trait Action

object Action {
  case object NoAction extends Action
  case class AddRow(insertPosition: InsertPosition) extends Action
  case class AddColumn(columnData: ColumnData, insertPosition: InsertPosition) extends Action
  case class UpdateColumn(columnData: ColumnData) extends Action
  case class UpdateTable(tableProperties: TableProperties) extends Action
  case class RemoveRow(rowId: UUID) extends Action
  case class RemoveColumn(columnId: UUID) extends Action
  case class SelectTable(tableProperties: TableProperties) extends Action
  case class SelectColumn(columnData: ColumnData) extends Action
}

enum InsertPosition:
  case Before, After

  def offset: Int = this match
    case Before => 0
    case After  => 1
