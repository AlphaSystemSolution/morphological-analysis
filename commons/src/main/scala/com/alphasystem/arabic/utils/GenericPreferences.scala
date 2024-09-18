package com.alphasystem
package arabic
package utils

import java.util.prefs.Preferences
import java.util.ServiceLoader
import scala.compiletime.uninitialized
import scala.jdk.OptionConverters.*

abstract class GenericPreferences protected (klass: Class[?]) {

  protected val root: Preferences = Preferences.userNodeForPackage(klass)

  protected def node(prefix: String, pathName: String): Preferences =
    root.node(s"$prefix.$pathName")

  def save(): Unit = root.flush()
}

object GenericPreferences {
  private var _instance: Option[GenericPreferences] = uninitialized

  def instance: GenericPreferences = {
    _instance match {
      case Some(gf) => gf
      case None =>
        val loader = ServiceLoader.load(classOf[GenericPreferences])
        loader.findFirst().toScala match {
          case Some(gf) =>
            _instance = Some(gf)
            gf
          case None =>
            throw new IllegalArgumentException("Unable to find any preferences")
        }
    }

  }
}
