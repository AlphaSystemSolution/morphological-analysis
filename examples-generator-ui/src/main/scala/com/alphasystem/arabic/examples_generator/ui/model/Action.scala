package com.alphasystem
package arabic
package examples_generator
package ui
package model

import arabic.morphologicalengine.generator.model.ChartConfiguration
import examples_generator.morphologicalchart.{ Conjugations, PairedConjugation, SingleConjugation }
import examples_generator.model.{ ColumnData, ExampleData, TableData, TableProperties }

import java.nio.file.Path
import java.util.UUID

enum Action {
  case NoAction extends Action
  case OpenExampleFile(filePath: Path, exampleData: ExampleData) extends Action
  case SaveExampleFile(exampleRequest: ExampleRequest) extends Action
  case NewExampleFile(tableData: TableData) extends Action
  case AddTable(tableData: TableData) extends Action
  case AddRow(insertPosition: InsertPosition) extends Action
  case AddColumn(columnData: ColumnData, insertPosition: InsertPosition) extends Action
  case UpdateColumn(columnData: ColumnData) extends Action
  case UpdateTable(tableProperties: TableProperties) extends Action
  case RemoveRow(rowId: UUID) extends Action
  case RemoveColumn(columnId: UUID) extends Action
  case SelectTable(tableProperties: TableProperties) extends Action
  case SelectColumn(columnData: ColumnData) extends Action
  case OpenConjugationsFile(filePath: Path, conjugations: Conjugations) extends Action
  case NewConjugationFile extends Action
  case SaveConjugationFile(conjugationsRequest: ConjugationsRequest) extends Action
  case AddSingleConjugation(singleConjugation: SingleConjugation) extends Action
  case SelectSingleConjugation(singleConjugation: SingleConjugation) extends Action
  case UpdateSingleConjugation(singleConjugation: SingleConjugation) extends Action
  case AddPairedConjugation(pairedConjugation: PairedConjugation) extends Action
  case SelectPairedConjugation(pairedConjugation: PairedConjugation) extends Action
  case UpdatePairedConjugation(pairedConjugation: PairedConjugation) extends Action
  case RemoveConjugation(conjugationId: UUID) extends Action
  case EditCharConfiguration(chartConfiguration: ChartConfiguration) extends Action
}

enum InsertPosition:
  case Before, After

  def offset: Int = this match
    case Before => 0
    case After  => 1
