package com.alphasystem
package arabic
package morphologicalengine
package server

import cats.effect.*
import morphologicalengine.generator.model.ConjugationTemplate
import io.circe.*
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.circe.*

package object routes {

  given ConjugationTemplateDecoder: EntityDecoder[IO, ConjugationTemplate] = jsonOf[IO, ConjugationTemplate]
}
