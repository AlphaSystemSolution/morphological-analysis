package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.GraphSettingsSkin
import morphology.graph.model.{ FontMetaInfo, GraphMetaInfo }
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.*
import scalafx.event.subscriptions.Subscription
import scalafx.scene.paint.Color

class GraphSettingsView extends Control {

  private var subscription: Subscription = _

  val graphMetaInfoProperty: ObjectProperty[GraphMetaInfo] =
    ObjectProperty[GraphMetaInfo](this, "graphMetaInfo", defaultGraphMetaInfo)

  private val graphWidthWrapperProperty: ReadOnlyDoubleWrapper = ReadOnlyDoubleWrapper(defaultGraphMetaInfo.width)

  private val graphHeightWrapperProperty: ReadOnlyDoubleWrapper = ReadOnlyDoubleWrapper(defaultGraphMetaInfo.height)

  private val tokenWidthWrapperProperty: ReadOnlyDoubleWrapper = ReadOnlyDoubleWrapper(defaultGraphMetaInfo.tokenWidth)

  private val tokenHeightWrapperProperty: ReadOnlyDoubleWrapper =
    ReadOnlyDoubleWrapper(defaultGraphMetaInfo.tokenHeight)

  private val gapBetweenTokensWrapperProperty: ReadOnlyDoubleWrapper =
    ReadOnlyDoubleWrapper(defaultGraphMetaInfo.gapBetweenTokens)

  private[control] val showGridLinesWrapperProperty: ReadOnlyBooleanWrapper =
    ReadOnlyBooleanWrapper(defaultGraphMetaInfo.showGridLines)

  private[control] val showOutLinesWrapperProperty: ReadOnlyBooleanWrapper =
    ReadOnlyBooleanWrapper(defaultGraphMetaInfo.showOutLines)

  private[control] val debugModeWrapperProperty: ReadOnlyBooleanWrapper =
    ReadOnlyBooleanWrapper(defaultGraphMetaInfo.debugMode)

  private val backgroundColorWrapperProperty: ReadOnlyObjectWrapper[Color] =
    ReadOnlyObjectWrapper[Color](this, "backgroundColor", defaultGraphMetaInfo.toColor)

  private val terminalFontWrapperProperty: ReadOnlyObjectWrapper[FontMetaInfo] =
    ReadOnlyObjectWrapper[FontMetaInfo](this, "terminalFont", defaultGraphMetaInfo.terminalFont)

  private val partOfSpeechFontWrapperProperty: ReadOnlyObjectWrapper[FontMetaInfo] =
    ReadOnlyObjectWrapper[FontMetaInfo](this, "partOfSpeechFont", defaultGraphMetaInfo.partOfSpeechFont)

  private val translationFontWrapperProperty: ReadOnlyObjectWrapper[FontMetaInfo] =
    ReadOnlyObjectWrapper[FontMetaInfo](this, "translationFont", defaultGraphMetaInfo.translationFont)

  // initializations and bindings
  graphMetaInfo = defaultGraphMetaInfo
  setSkin(createDefaultSkin())
  initGraphMetaInfoPropertySubscription()
  graphWidthProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(width = nv.doubleValue())
    initGraphMetaInfoPropertySubscription()
  })
  graphHeightProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(height = nv.doubleValue())
    initGraphMetaInfoPropertySubscription()
  })
  tokenWidthProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(tokenWidth = nv.doubleValue())
    initGraphMetaInfoPropertySubscription()
  })
  tokenHeightProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(tokenHeight = nv.doubleValue())
    initGraphMetaInfoPropertySubscription()
  })
  gapBetweenTokensProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(gapBetweenTokens = nv.doubleValue())
    initGraphMetaInfoPropertySubscription()
  })
  showGridLinesProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(showGridLines = nv)
    initGraphMetaInfoPropertySubscription()
  })
  showOutLinesProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(showOutLines = nv)
    initGraphMetaInfoPropertySubscription()
  })
  debugModeProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(debugMode = nv)
    initGraphMetaInfoPropertySubscription()
  })
  backgroundColorProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(backgroundColor = nv.toHex)
    initGraphMetaInfoPropertySubscription()
  })
  terminalFontProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(terminalFont = nv)
    initGraphMetaInfoPropertySubscription()
  })
  partOfSpeechFontProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(partOfSpeechFont = nv)
    initGraphMetaInfoPropertySubscription()
  })
  translationFontProperty.onChange((_, _, nv) => {
    subscription.cancel()
    graphMetaInfo = graphMetaInfo.copy(translationFont = nv)
    initGraphMetaInfoPropertySubscription()
  })

  // getters & setters
  def graphMetaInfo: GraphMetaInfo = graphMetaInfoProperty.value
  def graphMetaInfo_=(value: GraphMetaInfo): Unit = graphMetaInfoProperty.value = graphMetaInfo

  def graphWidth: Double = graphWidthWrapperProperty.value
  private[control] def graphWidth_=(value: Double): Unit = graphWidthWrapperProperty.value = value
  def graphWidthProperty: ReadOnlyDoubleProperty = graphWidthWrapperProperty.readOnlyProperty

  def graphHeight: Double = graphHeightWrapperProperty.value
  private[control] def graphHeight_=(value: Double): Unit = graphHeightWrapperProperty.value = value
  def graphHeightProperty: ReadOnlyDoubleProperty = graphHeightWrapperProperty.readOnlyProperty

  def tokenWidth: Double = tokenWidthWrapperProperty.value
  private[control] def tokenWidth_=(value: Double): Unit = tokenWidthWrapperProperty.value = value
  def tokenWidthProperty: ReadOnlyDoubleProperty = tokenWidthWrapperProperty.readOnlyProperty

  def tokenHeight: Double = tokenHeightWrapperProperty.value
  private[control] def tokenHeight_=(value: Double): Unit = tokenHeightWrapperProperty.value = value
  def tokenHeightProperty: ReadOnlyDoubleProperty = tokenHeightWrapperProperty.readOnlyProperty

  def gapBetweenTokens: Double = gapBetweenTokensWrapperProperty.value
  private[control] def gapBetweenTokens_=(value: Double): Unit = gapBetweenTokensWrapperProperty.value = value
  def gapBetweenTokensProperty: ReadOnlyDoubleProperty = gapBetweenTokensWrapperProperty.readOnlyProperty

  def showGridLines: Boolean = showGridLinesWrapperProperty.value
  private[control] def showGridLines_=(value: Boolean): Unit = showGridLinesWrapperProperty.value = value
  def showGridLinesProperty: ReadOnlyBooleanProperty = showGridLinesWrapperProperty.readOnlyProperty

  def showOutLines: Boolean = showOutLinesWrapperProperty.value
  private[control] def showOutLines_=(value: Boolean): Unit = showOutLinesWrapperProperty.value = value
  def showOutLinesProperty: ReadOnlyBooleanProperty = showOutLinesWrapperProperty.readOnlyProperty

  def debugMode: Boolean = debugModeWrapperProperty.value
  private[control] def debugMode_=(value: Boolean): Unit = debugModeWrapperProperty.value = value
  def debugModeProperty: ReadOnlyBooleanProperty = debugModeWrapperProperty.readOnlyProperty

  def backgroundColor: Color = backgroundColorWrapperProperty.value
  private[control] def backgroundColor_=(value: Color): Unit = backgroundColorWrapperProperty.value = value
  def backgroundColorProperty: ReadOnlyObjectProperty[Color] = backgroundColorWrapperProperty.readOnlyProperty

  def terminalFont: FontMetaInfo = terminalFontWrapperProperty.value
  private[control] def terminalFont_=(value: FontMetaInfo): Unit = terminalFontWrapperProperty.value = value
  def terminalFontProperty: ReadOnlyObjectProperty[FontMetaInfo] = terminalFontWrapperProperty.readOnlyProperty

  def partOfSpeechFont: FontMetaInfo = partOfSpeechFontWrapperProperty.value
  private[control] def partOfSpeechFont_=(value: FontMetaInfo): Unit = partOfSpeechFontWrapperProperty.value = value
  def partOfSpeechFontProperty: ReadOnlyObjectProperty[FontMetaInfo] = partOfSpeechFontWrapperProperty.readOnlyProperty

  def translationFont: FontMetaInfo = translationFontWrapperProperty.value
  private[control] def translationFont_=(value: FontMetaInfo): Unit = translationFontWrapperProperty.value = value
  def translationFontProperty: ReadOnlyObjectProperty[FontMetaInfo] = translationFontWrapperProperty.readOnlyProperty

  override def createDefaultSkin(): Skin[_] = GraphSettingsSkin(this)

  private def initGraphMetaInfoPropertySubscription(): Unit = {
    subscription = graphMetaInfoProperty.onChange((_, _, nv) => {
      val value = if Option(nv).isDefined then nv else defaultGraphMetaInfo
      graphWidth = value.width
      graphHeight = value.height
      tokenWidth = value.tokenWidth
      tokenHeight = value.tokenHeight
      gapBetweenTokens = value.gapBetweenTokens
      showGridLines = value.showGridLines
      showOutLines = value.showOutLines
      debugMode = value.debugMode
      terminalFont = value.terminalFont
      partOfSpeechFont = value.partOfSpeechFont
      translationFont = value.translationFont
    })
  }
}

object GraphSettingsView {
  def apply(): GraphSettingsView = new GraphSettingsView()
}
