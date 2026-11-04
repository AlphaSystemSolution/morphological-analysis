package com.alphasystem
package arabic
package examples_generator
package ui
package model

import examples_generator.morphologicalchart.{ ConjugationRequest, Conjugations, PairedConjugation, SingleConjugation }
import arabic.morphologicalengine.generator.model.ConjugationTemplate
import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationInput
import scalafx.scene.control.TreeItem

enum ConjugationsTreeNode {
  case RootNode extends ConjugationsTreeNode
  case ConjugationTypeNode(conjugationType: ConjugationType) extends ConjugationsTreeNode
  case SingleConjugationNode(singleConjugation: SingleConjugation) extends ConjugationsTreeNode
  case PairedConjugationsNode(pairedConjugation: PairedConjugation) extends ConjugationsTreeNode
  case FullConjugationsNode(conjugationTemplate: ConjugationTemplate) extends ConjugationsTreeNode
  case ConjugationRequestNode(conjugationRequest: ConjugationRequest) extends ConjugationsTreeNode
  case ConjugationInputNode(conjugationInput: ConjugationInput) extends ConjugationsTreeNode
}

object ConjugationsTreeNode {
  def buildTree(conjugations: Conjugations): TreeItem[ConjugationsTreeNode] = {
    val root = new TreeItem[ConjugationsTreeNode](RootNode) {
      expanded = true
    }

    ConjugationType.values.foreach { conjugationType =>
      val node = new TreeItem[ConjugationsTreeNode](ConjugationTypeNode(conjugationType)) {
        expanded = false
      }

      conjugationType match {
        case ConjugationType.Single => conjugations.single.foreach(addSingleConjugationsNodes(node, _))
        case ConjugationType.Paired => conjugations.paired.foreach(addPairedConjugationsNodes(node, _))
        case ConjugationType.Full   => conjugations.full.foreach(addFullConjugationsNodes(node, _))
      }
      root.children += node
    }

    root
  }

  private def addSingleConjugationsNodes(
    root: TreeItem[ConjugationsTreeNode],
    singleConjugations: Seq[SingleConjugation]
  ): Unit = singleConjugations.foreach { singleConjugation =>
    val node = new TreeItem[ConjugationsTreeNode](SingleConjugationNode(singleConjugation)) {
      expanded = false
    }
    addConjugationRequest(node, singleConjugation.request)
    root.children += node
  }

  private def addPairedConjugationsNodes(
    root: TreeItem[ConjugationsTreeNode],
    pairedConjugations: Seq[PairedConjugation]
  ): Unit = {
    pairedConjugations.foreach { pairedConjugation =>
      val node = new TreeItem[ConjugationsTreeNode](PairedConjugationsNode(pairedConjugation)) {
        expanded = false
      }
      pairedConjugation.left.foreach(addConjugationRequest(root, _))
      pairedConjugation.right.foreach(addConjugationRequest(root, _))
      root.children += node
    }
  }

  private def addFullConjugationsNodes(
    root: TreeItem[ConjugationsTreeNode],
    conjugationTemplates: Seq[ConjugationTemplate]
  ): Unit = {
    conjugationTemplates.foreach { conjugationTemplate =>
      val node = new TreeItem[ConjugationsTreeNode](FullConjugationsNode(conjugationTemplate)) {
        expanded = false
      }
      root.children += node
    }
  }

  private def addConjugationRequest(
    root: TreeItem[ConjugationsTreeNode],
    conjugationRequest: ConjugationRequest
  ): Unit = {
    val node = new TreeItem[ConjugationsTreeNode](ConjugationRequestNode(conjugationRequest))
    addConjugationInput(node, conjugationRequest.conjugationInput)
    root.children += node
  }

  private def addConjugationInput(root: TreeItem[ConjugationsTreeNode], conjugationInput: ConjugationInput) = {
    val node = new TreeItem[ConjugationsTreeNode](ConjugationInputNode(conjugationInput))
    root.children += node
  }
}
