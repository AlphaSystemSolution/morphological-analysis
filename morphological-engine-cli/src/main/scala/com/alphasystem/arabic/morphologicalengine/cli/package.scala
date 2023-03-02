package com.alphasystem
package arabic
package morphologicalengine

import org.rogach.scallop.{ ArgType, ValueConverter }

import java.nio.file.{ Path, Paths }

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

}
