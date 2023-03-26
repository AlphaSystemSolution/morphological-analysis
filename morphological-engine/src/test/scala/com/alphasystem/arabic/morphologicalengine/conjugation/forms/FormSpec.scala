package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms

import munit.FunSuite

@munit.IgnoreSuite
class FormSpec extends FunSuite {

  test("Form: FormICategoryAGroupUTemplate") {
    val form = Form.FormICategoryAGroupUTemplate
    assertEquals(form.pastTense.label, "فَعَلَ")
    assertEquals(form.presentTense.label, "يَفْعُلُ")
    assertEquals(form.imperative.label, "أُفْعُلْ")
    assertEquals(form.forbidden.label, "تَفْعُلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَاعِلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَاعِلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفْعَلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مَفْعُوْلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مَفْعُوْلَةٌ")
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormICategoryAGroupITemplate") {
    val form = Form.FormICategoryAGroupITemplate
    assertEquals(form.pastTense.label, "فَعَلَ")
    assertEquals(form.presentTense.label, "يَفْعِلُ")
    assertEquals(form.imperative.label, "إِفْعِلْ")
    assertEquals(form.forbidden.label, "تَفْعِلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَاعِلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَاعِلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفْعَلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مَفْعُوْلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مَفْعُوْلَةٌ")
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormICategoryAGroupATemplate") {
    val form = Form.FormICategoryAGroupATemplate
    assertEquals(form.pastTense.label, "فَعَلَ")
    assertEquals(form.presentTense.label, "يَفْعَلُ")
    assertEquals(form.imperative.label, "إِفْعَلْ")
    assertEquals(form.forbidden.label, "تَفْعَلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَاعِلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَاعِلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفْعَلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مَفْعُوْلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مَفْعُوْلَةٌ")
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormICategoryIGroupATemplate") {
    val form = Form.FormICategoryIGroupATemplate
    assertEquals(form.pastTense.label, "فَعِلَ")
    assertEquals(form.presentTense.label, "يَفْعَلُ")
    assertEquals(form.imperative.label, "إِفْعَلْ")
    assertEquals(form.forbidden.label, "تَفْعَلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَاعِلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَاعِلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفْعَلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مَفْعُوْلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مَفْعُوْلَةٌ")
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormICategoryIGroupITemplate") {
    val form = Form.FormICategoryIGroupITemplate
    assertEquals(form.pastTense.label, "فَعِلَ")
    assertEquals(form.presentTense.label, "يَفْعِلُ")
    assertEquals(form.imperative.label, "إِفْعِلْ")
    assertEquals(form.forbidden.label, "تَفْعِلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَاعِلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَاعِلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفْعَلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مَفْعُوْلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مَفْعُوْلَةٌ")
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormICategoryUTemplate") {
    val form = Form.FormICategoryUTemplate
    assertEquals(form.pastTense.label, "فَعُلَ")
    assertEquals(form.presentTense.label, "يَفْعُلُ")
    assertEquals(form.imperative.label, "أُفْعُلْ")
    assertEquals(form.forbidden.label, "تَفْعُلْ")
    assertEquals(form.activeParticipleMasculine.label, "فَعِيْلٌ")
    assertEquals(form.activeParticipleFeminine.label, "فَعِيْلَةٌ")
    assertEquals(form.pastPassiveTense.isEmpty, true)
    assertEquals(form.presentPassiveTense.isEmpty, true)
    assertEquals(form.passiveParticipleMasculine.isEmpty, true)
    assertEquals(form.passiveParticipleFeminine.isEmpty, true)
    assertEquals(form.verbalNouns.isEmpty, true)
    assertEquals(form.adverbs.map(_.label), Seq("مَفْعِلٌ", "مَفْعَلٌ", "مَفْعَلَةٌ"))
  }

  test("Form: FormIITemplate") {
    val form = Form.FormIITemplate
    assertEquals(form.pastTense.label, "فَعَّلَ")
    assertEquals(form.presentTense.label, "يُفَعِّلُ")
    assertEquals(form.imperative.label, "فَعِّلْ")
    assertEquals(form.forbidden.label, "تُفَعِّلْ")
    assertEquals(form.activeParticipleMasculine.label, "مُفَعِّلٌ")
    assertEquals(form.activeParticipleFeminine.label, "مُفَعِّلَةٌ")
    assertEquals(form.pastPassiveTense.get.label, "فُعِّلَ")
    assertEquals(form.presentPassiveTense.get.label, "يُفَعَّلُ")
    assertEquals(form.passiveParticipleMasculine.get.label, "مُفَعَّلٌ")
    assertEquals(form.passiveParticipleFeminine.get.label, "مُفَعَّلَةٌ")
    assertEquals(form.verbalNouns.map(_.label).head, "تَفْعِيْلٌ")
    assertEquals(form.adverbs.map(_.label), Seq("مُفَعَّلَةٌ"))
  }
}
