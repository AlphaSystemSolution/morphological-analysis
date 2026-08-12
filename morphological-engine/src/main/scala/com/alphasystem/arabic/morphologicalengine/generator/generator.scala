package com.alphasystem
package arabic
package morphologicalengine
package generator

import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax.*
import io.circe.yaml.v12.parser
import io.circe.yaml.v12.syntax.*
import io.circe.yaml.common.Printer.StringStyle

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*
import com.alphasystem.arabic.model.JussiveParticle

import java.nio.file.{ Files, Path }
import scala.io.Source
import scala.util.{ Failure, Success, Using }

given Decoder[SingleConjugation] = deriveDecoder
given Encoder[SingleConjugation] = deriveEncoder
given Decoder[SingleConjugationRequest] = deriveDecoder
given Encoder[SingleConjugationRequest] = deriveEncoder
given Decoder[PairedConjugationRequest] = deriveDecoder
given Encoder[PairedConjugationRequest] = deriveEncoder
given Decoder[PairedConjugation] = deriveDecoder
given Encoder[PairedConjugation] = deriveEncoder
given Decoder[ConjugationRequest] = deriveDecoder
given Encoder[ConjugationRequest] = deriveEncoder
given Decoder[RootLetters] = deriveDecoder
given Encoder[RootLetters] = deriveEncoder
given Decoder[ChartConfiguration] = deriveDecoder
given Encoder[ChartConfiguration] = deriveEncoder
given Decoder[ConjugationConfiguration] = deriveDecoder
given Encoder[ConjugationConfiguration] = deriveEncoder
given Decoder[ConjugationInput] = deriveDecoder
given Encoder[ConjugationInput] = deriveEncoder
given Decoder[ConjugationTemplate] = deriveDecoder
given Encoder[ConjugationTemplate] = deriveEncoder
given Decoder[Settings] = deriveDecoder
given Encoder[Settings] = deriveEncoder
given Decoder[Conjugations] = deriveDecoder
given Encoder[Conjugations] = deriveEncoder

def toSingleConjugationRequest(path: Path): SingleConjugationRequest =
  fromFile(path, fromString[SingleConjugationRequest])

def toPairedConjugationRequest(path: Path): PairedConjugationRequest =
  fromFile(path, fromString[PairedConjugationRequest])

def toConjugationTemplate(path: Path): ConjugationTemplate =
  fromFile(path, fromString[ConjugationTemplate])

def toConjugations(path: Path): Conjugations =
  fromFile(path, fromString[Conjugations])

private val yamlPrinter =
  io.circe.yaml.v12.Printer
    .builder
    .withStringStyle(StringStyle.DoubleQuoted)
    .build()

def toYaml(conjugationTemplate: ConjugationTemplate): String = yamlPrinter.pretty(conjugationTemplate.asJson)

def saveData(conjugationTemplate: ConjugationTemplate, path: Path): Path = {
  Files.writeString(path, toYaml(conjugationTemplate))
  path
}

private def fromFile[T](path: Path, fromString: String => T): T =
  Using(Source.fromFile(path.toFile))(source => fromString(source.mkString)) match
    case Failure(ex)    => throw ex
    case Success(value) => value

private def fromString[T](ymlString: String)(using dec: Decoder[T]): T =
  parser.parse(ymlString) match {
    case Left(ex) => throw ex
    case Right(value) =>
      value.as[T] match {
        case Left(ex)     => throw ex
        case Right(value) => value
      }
  }

case class SingleConjugationRequest(conjugations: Seq[SingleConjugation])

case class SingleConjugation(tag: String, settings: Settings, request: ConjugationRequest)

case class PairedConjugationRequest(conjugations: Seq[PairedConjugation])

case class Conjugations(
  single: Option[Set[SingleConjugation]] = None,
  paired: Option[Set[PairedConjugation]] = None,
  full: Option[Set[ConjugationTemplate]] = None)

case class PairedConjugation(
  tag: String,
  settings: Settings,
  right: Option[ConjugationRequest],
  left: Option[ConjugationRequest]) {
  validate()

  import PairedConjugation.*

  private def validate(): Unit = {
    require(right.isDefined || left.isDefined, "Either right or left request must be defined")
    (left, right) match
      case (Some(l), Some(r)) =>
        require(
          hasSimilarTypes(l.morphologicalTermType, r.morphologicalTermType),
          "Both right and left term should be of similar types"
        )
      case _ => // all other cases are validated
  }
}

object PairedConjugation {
  private val tenseTerms = Seq(PastTense, PresentTense, PastPassiveTense, PresentPassiveTense)
  private val imperativeAndForbiddenTerms = Seq(Imperative, Forbidden)
  private val nounTerms = Seq(
    VerbalNoun,
    ActiveParticipleMasculine,
    ActiveParticipleFeminine,
    PassiveParticipleMasculine,
    PassiveParticipleFeminine,
    NounOfPlaceAndTime
  )

  def hasSimilarTypes(leftTerm: MorphologicalTermType, rightTerm: MorphologicalTermType): Boolean = {
    (tenseTerms.contains(rightTerm) && tenseTerms.contains(leftTerm)) ||
    (imperativeAndForbiddenTerms.contains(rightTerm) && imperativeAndForbiddenTerms.contains(leftTerm)) ||
    (nounTerms.contains(rightTerm) && nounTerms.contains(leftTerm))
  }

  def isTenseTerm(term: MorphologicalTermType): Boolean = tenseTerms.contains(term)
  def isImperativeOrForbidden(term: MorphologicalTermType): Boolean = imperativeAndForbiddenTerms.contains(term)
  def isNounTerm(term: MorphologicalTermType): Boolean = nounTerms.contains(term)
}

case class ConjugationRequest(
  morphologicalTermType: MorphologicalTermType,
  namedTemplate: NamedTemplate,
  rootLetters: RootLetters,
  verbalNouns: Option[Seq[String]] = None,
  translations: Option[Map[ProNoun, Seq[String]]] = None // so far only valid for verbs
) {

  def toConjugationInput(jussiveParticle: Option[JussiveParticle] = None): ConjugationInput =
    ConjugationInput(
      namedTemplate = namedTemplate,
      conjugationConfiguration = ConjugationConfiguration(jussiveParticle = jussiveParticle),
      rootLetters = rootLetters,
      translation = None,
      verbalNounCodes = verbalNouns.getOrElse(Seq.empty)
    )
}

case class Settings(
  showPronouns: Option[Boolean] = None, // valid for verbs only
  showNumbers: Option[Boolean] = None, // valid for both verbs and nouns, numbers header
  showGenders: Option[Boolean] = None, // valid for verbs only
  showConversationTypes: Option[Boolean] = None, // valid for verbs only
  showNounStatus: Option[Boolean] = None, // valid for nouns only, nominative, accusative, and genitive
  showTermTypeCaption: Option[Boolean] = None, // valid only for detailed chart
  tableWidth: Option[Int] = None, // valid only for single conjugation tables
  jussiveParticle: Option[JussiveParticle] = None // valid for jussive present tense verbs only
)
