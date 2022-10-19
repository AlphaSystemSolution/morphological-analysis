package com.alphasystem
package utils

import java.util.prefs.Preferences
import java.util.ServiceLoader
import scala.jdk.OptionConverters._

abstract class GenericPreferences protected (klass: Class[?]) {

  protected val root: Preferences = Preferences.userNodeForPackage(klass)

  protected def node(prefix: String, pathName: String): Preferences =
    root.node(s"$prefix.$pathName")

  def save(): Unit = root.flush()
}

object GenericPreferences {
  private var _instance: Option[GenericPreferences] = _

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
