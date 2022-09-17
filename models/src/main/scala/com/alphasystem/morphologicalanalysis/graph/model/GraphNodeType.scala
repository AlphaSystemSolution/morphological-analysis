package com.alphasystem.morphologicalanalysis.graph.model

enum GraphNodeType {

  /** A node that will always has part of speech tag.
    */
  case TERMINAL

  /** Represents part of speech.
    */
  case PART_OF_SPEECH

  /** Represents two or more terminal nodes to make a phrase.
    */
  case PHRASE

  /** Represents relationship between two nodes.
    */
  case RELATIONSHIP

  /** Represents a node from outside of current set of nodes.
    */
  case REFERENCE

  /** A implicit word with part of speech and Arabic text to fill grammatical
    * meaning / relationship.
    */
  case HIDDEN

  /** An implied node without any Arabic text and only part of speech to
    * complete grammatical meaning / relationship.
    */
  case IMPLIED

  /** Represents the root of each of above category in the tree. This is not
    * used in actual graph.
    */
  case ROOT
}
