package com.alphasystem
package arabic
package morphologicalengine

import org.rogach.scallop.{ ArgType, ValueConverter }

import java.nio.file.{ Path, Paths }
import arabic.morphologicalengine.generator.model.ConjugationTemplate
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*

import scala.io.Source
import scala.util.{ Failure, Success, Try }

package object cli {

  given PathConverter: ValueConverter[Path] = new ValueConverter[Path] {
    override def parse(s: List[(String, List[String])]): Either[String, Option[Path]] =
      s match
        case (_, p :: _) :: _ =>
          if p.isBlank then Right(None)
          else Right(Some(Paths.get(p)))
        case _ => Right(None)

    override val argType: ArgType.V = org.rogach.scallop.ArgType.SINGLE
  }

  def toConjugationTemplate(path: Path): ConjugationTemplate = {
    val source = Source.fromFile(path.toFile)
    val json = source.mkString
    Try(source.close())
    decode[ConjugationTemplate](json) match
      case Left(ex)     => throw ex
      case Right(value) => value
  }

}
