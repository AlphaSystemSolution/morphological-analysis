package com.alphasystem
package arabic

import com.alphasystem.arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import munit.FunSuite

class JsonConversionSpec extends FunSuite {

  test("ConjugationTemplate conversion") {
    val chartConfiguration = """{
                               |        "pageOrientation": "Portrait",
                               |        "sortDirection": "Ascending",
                               |        "format": "AbbreviateConjugationSingleRow",
                               |        "arabicFontFamily": "KFGQPC Uthman Taha Naskh",
                               |        "translationFontFamily": "Candara",
                               |        "arabicFontSize": 12,
                               |        "translationFontSize": 10,
                               |        "headingFontSize": 16,
                               |        "showToc": true,
                               |        "showTitle": true,
                               |        "showLabels": true,
                               |        "removeAdverbs": true,
                               |        "showAbbreviatedConjugation": true,
                               |        "showDetailedConjugation": true,
                               |        "showMorphologicalTermCaptionInAbbreviatedConjugation": true,
                               |        "showMorphologicalTermCaptionInDetailConjugation": true
                               |    }""".stripMargin

    val json = """{
                 |    "id": "StoryOfIbrahim",
                 |    "chartConfiguration": {
                 |        "pageOrientation": "Portrait",
                 |        "sortDirection": "Ascending",
                 |        "format": "AbbreviateConjugationSingleRow",
                 |        "arabicFontFamily": "KFGQPC Uthman Taha Naskh",
                 |        "translationFontFamily": "Candara",
                 |        "arabicFontSize": 12,
                 |        "translationFontSize": 10,
                 |        "headingFontSize": 16,
                 |        "showToc": true,
                 |        "showTitle": true,
                 |        "showLabels": true,
                 |        "removeAdverbs": true,
                 |        "showAbbreviatedConjugation": true,
                 |        "showDetailedConjugation": true,
                 |        "showMorphologicalTermCaptionInAbbreviatedConjugation": true,
                 |        "showMorphologicalTermCaptionInDetailConjugation": true
                 |    },
                 |    "inputs": [
                 |        {
                 |            "id": "59def14d-1510-446d-a4fa-26f110257538",
                 |            "namedTemplate": "FormICategoryAGroupITemplate",
                 |            "conjugationConfiguration": {
                 |                "skipRuleProcessing": false,
                 |                "removePassiveLine": false
                 |            },
                 |            "rootLetters": {
                 |                "firstRadical": "Ba",
                 |                "secondRadical": "Ya",
                 |                "thirdRadical": "Ain",
                 |                "fourthRadical": null
                 |            },
                 |            "translation": "To sell",
                 |            "verbalNounCodes": [
                 |                "FormIV1"
                 |            ]
                 |        },
                 |        {
                 |            "id": "07f0483f-484d-45b4-a5fd-064e431b9915",
                 |            "namedTemplate": "FormICategoryAGroupUTemplate",
                 |            "conjugationConfiguration": {
                 |                "skipRuleProcessing": false,
                 |                "removePassiveLine": false
                 |            },
                 |            "rootLetters": {
                 |                "firstRadical": "Seen",
                 |                "secondRadical": "Jeem",
                 |                "thirdRadical": "Dal",
                 |                "fourthRadical": null
                 |            },
                 |            "translation": "To Prostrate",
                 |            "verbalNounCodes": [
                 |                "FormIV1"
                 |            ]
                 |        },
                 |        {
                 |            "id": "ef139d5b-36ef-4781-8151-086c5d3e2746",
                 |            "namedTemplate": "FormICategoryAGroupUTemplate",
                 |            "conjugationConfiguration": {
                 |                "skipRuleProcessing": false,
                 |                "removePassiveLine": false
                 |            },
                 |            "rootLetters": {
                 |                "firstRadical": "Ain",
                 |                "secondRadical": "Ba",
                 |                "thirdRadical": "Dal",
                 |                "fourthRadical": null
                 |            },
                 |            "translation": "To Worship",
                 |            "verbalNounCodes": []
                 |        }
                 |    ]
                 |}""".stripMargin

    println(decode[ChartConfiguration](chartConfiguration))
    println(decode[ConjugationTemplate](json))
  }
}
