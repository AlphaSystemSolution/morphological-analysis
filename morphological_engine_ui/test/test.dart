import 'dart:convert';

import 'package:morphological_engine_ui/models/arabic_letter.dart';
import 'package:morphological_engine_ui/models/chart_configuration.dart';
import 'package:morphological_engine_ui/models/conjugation_input.dart';
import 'package:morphological_engine_ui/models/conjugation_template.dart';
import 'package:morphological_engine_ui/models/model.dart';
import 'package:morphological_engine_ui/models/named_template.dart';
import 'package:morphological_engine_ui/models/verbal_noun.dart';
import 'package:test/test.dart';

void main() {
  group('Test json conversion', () {
    test('Convert json to RootLetters with no fourthRadical', () {
      const json =
          '{"firstRadical": "Ba", "secondRadical": "Ya", "thirdRadical": "Ain", "fourthRadical": null}';
      final parsedJson = jsonDecode(json);
      var actual = RootLetters.fromJson(parsedJson);

      var expected = const RootLetters(
          firstRadical: ArabicLetter.Ba,
          secondRadical: ArabicLetter.Ya,
          thirdRadical: ArabicLetter.Ain);

      expect(actual, equals(expected));
    });

    test('Convert json to RootLetters with fourthRadical', () {
      const json =
          '{"firstRadical": "Waw", "secondRadical": "Seen", "thirdRadical": "Waw", "fourthRadical": "Seen"}';
      final parsedJson = jsonDecode(json);
      var actual = RootLetters.fromJson(parsedJson);

      var expected = const RootLetters(
          firstRadical: ArabicLetter.Waw,
          secondRadical: ArabicLetter.Seen,
          thirdRadical: ArabicLetter.Waw,
          fourthRadical: ArabicLetter.Seen);

      expect(actual, equals(expected));
    });

    test('Convert RootLetters to json with no fourthRadical', () {
      var rootLetters = const RootLetters(
          firstRadical: ArabicLetter.Noon,
          secondRadical: ArabicLetter.Sad,
          thirdRadical: ArabicLetter.Ra);

      final actual = jsonEncode(rootLetters);
      const expected =
          '{"firstRadical":"Noon","secondRadical":"Sad","thirdRadical":"Ra","fourthRadical":null}';

      expect(actual, equals(expected));
    });

    test('Convert RootLetters to json with fourthRadical', () {
      var rootLetters = const RootLetters(
          firstRadical: ArabicLetter.Noon,
          secondRadical: ArabicLetter.Sad,
          thirdRadical: ArabicLetter.Ra,
          fourthRadical: ArabicLetter.Dal);

      final actual = jsonEncode(rootLetters);
      const expected =
          '{"firstRadical":"Noon","secondRadical":"Sad","thirdRadical":"Ra","fourthRadical":"Dal"}';

      expect(actual, equals(expected));
    });

    test('Convert json into ConjugationConfiguration', () {
      const json = '{"skipRuleProcessing": false, "removePassiveLine": true}';

      final parsedJson = jsonDecode(json);
      var actual = ConjugationConfiguration.fromJson(parsedJson);

      var expected = const ConjugationConfiguration(removePassiveLine: true);
      expect(actual, equals(expected));
    });

    test('Convert ConjugationInput to json', () {
      var conjugationConfiguration =
          const ConjugationConfiguration(skipRuleProcessing: true);

      var actual = jsonEncode(conjugationConfiguration);

      var expected = '{"skipRuleProcessing":true,"removePassiveLine":false}';
      expect(actual, equals(expected));
    });

    test('Convert json to ConjugationInput', () {
      const uuid = "b4357461-fb2e-42e5-bc88-d3fa7ac701a2";
      const json =
          '{"id": "$uuid","namedTemplate": "FormICategoryAGroupITemplate", "rootLetters": {"firstRadical":"Seen","secondRadical":"Jeem","thirdRadical":"Dal","fourthRadical":null}, "translation": "To Prostrate", "verbalNounCodes": []}';

      final parsedJson = jsonDecode(json);
      var actual = ConjugationInput.fromJson(parsedJson);

      var expected = ConjugationInput(
          id: uuid,
          namedTemplate: NamedTemplate.FormICategoryAGroupITemplate,
          rootLetters: const RootLetters(
              firstRadical: ArabicLetter.Seen,
              secondRadical: ArabicLetter.Jeem,
              thirdRadical: ArabicLetter.Dal),
          translation: "To Prostrate");

      expect(actual, equals(expected));
    });

    test('Convert ConjugationInput to json', () {
      const uuid = "b4357461-fb2e-42e5-bc88-d3fa7ac701a2";

      var input = ConjugationInput(
          id: uuid,
          conjugationConfiguration: const ConjugationConfiguration(
              skipRuleProcessing: true, removePassiveLine: false),
          namedTemplate: NamedTemplate.FormICategoryAGroupITemplate,
          rootLetters: const RootLetters(
              firstRadical: ArabicLetter.Seen,
              secondRadical: ArabicLetter.Jeem,
              thirdRadical: ArabicLetter.Dal),
          translation: "To Prostrate",
          verbalNouns: [VerbalNoun.FormIV1]);

      var actual = jsonEncode(input);

      const expected =
          '{"id":"$uuid","conjugationConfiguration":{"skipRuleProcessing":true,"removePassiveLine":false},"namedTemplate":"FormICategoryAGroupITemplate","rootLetters":{"firstRadical":"Seen","secondRadical":"Jeem","thirdRadical":"Dal","fourthRadical":null},"translation":"To Prostrate","verbalNounCodes":["FormIV1"]}';

      expect(actual, equals(expected));
    });

    test('Convert json into ConjugationTemplate', () {
      var json = '''{
    "chartConfiguration": {
      "pageOrientation": "Portrait",
      "sortDirection": "Ascending",
      "format": "AbbreviateConjugationSingleRow",
      "arabicFontFamily": "KFGQPC Uthman Taha Naskh",
      "translationFontFamily": "Candara",
      "arabicFontSize": 12,
      "translationFontSize": 10,
      "headingFontSize": 16,
      "showToc": true,
      "showTitle": true,
      "showLabels": true,
      "showAbbreviatedConjugation": true,
      "showDetailedConjugation": true,
      "showMorphologicalTermCaptionInAbbreviatedConjugation": true,
      "showMorphologicalTermCaptionInDetailConjugation": true
    },
    "inputs": [
      {
        "id": "59def14d-1510-446d-a4fa-26f110257538",
        "namedTemplate": "FormICategoryAGroupITemplate",
        "conjugationConfiguration": {
          "skipRuleProcessing": false,
          "removePassiveLine": false,
          "removeAdverbs": true
        },
        "rootLetters": {
          "firstRadical": "Ba",
          "secondRadical": "Ya",
          "thirdRadical": "Ain",
          "fourthRadical": null
        },
        "translation": "To Sell",
        "verbalNounCodes": ["FormIV1"]
      },
      {
        "id": "07f0483f-484d-45b4-a5fd-064e431b9915",
        "namedTemplate": "FormICategoryAGroupUTemplate",
        "conjugationConfiguration": {
          "skipRuleProcessing": false,
          "removePassiveLine": false,
          "removeAdverbs": true
        },
        "rootLetters": {
          "firstRadical": "Seen",
          "secondRadical": "Jeem",
          "thirdRadical": "Dal",
          "fourthRadical": null
        },
        "translation": "To Prostrate",
        "verbalNounCodes": ["FormIV1"]
      },
      {
        "id": "ef139d5b-36ef-4781-8151-086c5d3e2746",
        "namedTemplate": "FormVTemplate",
        "conjugationConfiguration": {
          "skipRuleProcessing": false,
          "removePassiveLine": false,
          "removeAdverbs": true
        },
        "rootLetters": {
          "firstRadical": "Ain",
          "secondRadical": "Ba",
          "thirdRadical": "Dal",
          "fourthRadical": null
        },
        "translation": "To Worship",
        "verbalNounCodes": []
      }
    ]
  }
  ''';
      final parsedJson = jsonDecode(json);
      var actual = ConjugationTemplate.fromJson(parsedJson);

      var expected = ConjugationTemplate(
          chartConfiguration: const ChartConfiguration(
              format: DocumentFormat.AbbreviateConjugationSingleRow),
          inputs: [
            ConjugationInput(
                id: "59def14d-1510-446d-a4fa-26f110257538",
                index: 0,
                namedTemplate: NamedTemplate.FormICategoryAGroupITemplate,
                rootLetters: const RootLetters(
                    firstRadical: ArabicLetter.Ba,
                    secondRadical: ArabicLetter.Ya,
                    thirdRadical: ArabicLetter.Ain),
                translation: "To Sell",
                verbalNouns: [VerbalNoun.FormIV1]),
            ConjugationInput(
                id: "07f0483f-484d-45b4-a5fd-064e431b9915",
                index: 1,
                namedTemplate: NamedTemplate.FormICategoryAGroupUTemplate,
                rootLetters: const RootLetters(
                    firstRadical: ArabicLetter.Seen,
                    secondRadical: ArabicLetter.Jeem,
                    thirdRadical: ArabicLetter.Dal),
                translation: "To Prostrate",
                verbalNouns: [VerbalNoun.FormIV1]),
            ConjugationInput(
                id: "ef139d5b-36ef-4781-8151-086c5d3e2746",
                index: 2,
                namedTemplate: NamedTemplate.FormVTemplate,
                rootLetters: const RootLetters(
                    firstRadical: ArabicLetter.Ain,
                    secondRadical: ArabicLetter.Ba,
                    thirdRadical: ArabicLetter.Dal),
                translation: "To Worship",
                verbalNouns: [])
          ]);

      expect(actual, equals(expected));
    });

    test('Convert ConjugationTemplate to json', () {
      var original = ConjugationTemplate(inputs: [
        ConjugationInput(
            id: "59def14d-1510-446d-a4fa-26f110257538",
            index: 0,
            namedTemplate: NamedTemplate.FormICategoryAGroupITemplate,
            rootLetters: const RootLetters(
                firstRadical: ArabicLetter.Ba,
                secondRadical: ArabicLetter.Ya,
                thirdRadical: ArabicLetter.Ain),
            translation: "To Sell",
            verbalNouns: [VerbalNoun.FormIV1]),
        ConjugationInput(
            id: "07f0483f-484d-45b4-a5fd-064e431b9915",
            index: 1,
            namedTemplate: NamedTemplate.FormICategoryAGroupUTemplate,
            rootLetters: const RootLetters(
                firstRadical: ArabicLetter.Seen,
                secondRadical: ArabicLetter.Jeem,
                thirdRadical: ArabicLetter.Dal),
            translation: "To Prostrate",
            verbalNouns: [VerbalNoun.FormIV1]),
        ConjugationInput(
            id: "ef139d5b-36ef-4781-8151-086c5d3e2746",
            index: 2,
            namedTemplate: NamedTemplate.FormVTemplate,
            rootLetters: const RootLetters(
                firstRadical: ArabicLetter.Ain,
                secondRadical: ArabicLetter.Ba,
                thirdRadical: ArabicLetter.Dal),
            translation: "To Worship",
            verbalNouns: [VerbalNoun.FormV])
      ]);

      var json = jsonEncode(original);
      var actual = ConjugationTemplate.fromJson(jsonDecode(json));
      expect(actual, equals(original));
    });
  });
}
