import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';
import 'package:uuid/uuid.dart';

import 'arabic_letter.dart';
import 'model.dart';
import 'named_template.dart';
import 'verbal_noun.dart';

class ConjugationInput {
  String id;
  int index;
  bool checked;
  ConjugationConfiguration conjugationConfiguration;
  NamedTemplate namedTemplate;
  RootLetters rootLetters;
  String translation;
  List<VerbalNoun> verbalNouns;

  ConjugationInput(
      {required this.id,
      this.index = 0,
      this.checked = false,
      this.conjugationConfiguration = const ConjugationConfiguration(),
      this.namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
      this.rootLetters = const RootLetters(),
      this.translation = "To Do",
      this.verbalNouns = const []});

  ConjugationInput copy(
      {String? id,
      int? index,
      bool? checked,
      ConjugationConfiguration? conjugationConfiguration,
      NamedTemplate? namedTemplate,
      RootLetters? rootLetters,
      String? translation,
      List<VerbalNoun>? verbalNouns}) {
    return ConjugationInput(
        id: id ?? this.id,
        index: index ?? this.index,
        checked: checked ?? this.checked,
        conjugationConfiguration:
            conjugationConfiguration ?? this.conjugationConfiguration,
        namedTemplate: namedTemplate ?? this.namedTemplate,
        rootLetters: rootLetters ?? this.rootLetters,
        translation: translation ?? this.translation,
        verbalNouns: verbalNouns ?? this.verbalNouns);
  }

  String displayVerbalNouns() {
    return verbalNouns.map((e) => e.label).join(" ${ArabicLetter.Waw.label} ");
  }

  @override
  int get hashCode => hashObjects([
        id,
        index,
        checked,
        conjugationConfiguration,
        namedTemplate,
        rootLetters,
        translation
      ]);

  @override
  bool operator ==(Object other) =>
      other is ConjugationInput &&
      id == other.id &&
      index == other.index &&
      checked == other.checked &&
      conjugationConfiguration == other.conjugationConfiguration &&
      namedTemplate == other.namedTemplate &&
      rootLetters == other.rootLetters &&
      translation == other.translation &&
      listEquals(verbalNouns, other.verbalNouns);

  Map toJson() => {
        "id": id,
        "conjugationConfiguration": conjugationConfiguration.toJson(),
        "namedTemplate": namedTemplate.name,
        "rootLetters": rootLetters.toJson(),
        "translation": translation,
        "verbalNounCodes": verbalNouns.map((e) => e.name).toList()
      };

  @override
  String toString() {
    return """ConjugationInput(
  id: $id,
  index: $index,
  checked: $checked,
  conjugationConfiguration: $conjugationConfiguration,
  namedTemplate: ${namedTemplate.displayValue()},
  rootLetters: ${rootLetters.displayValue()},
  translation: $translation,
  "verbalNounCodes": ${verbalNouns.map((e) => e.name).toList()}
)""";
  }

  factory ConjugationInput.fromJson(Map<String, dynamic> data) =>
      ConjugationInput(
          id: data['id'] ?? const Uuid().v4(),
          checked: false,
          conjugationConfiguration: ConjugationConfiguration.fromJson(
              data['conjugationConfiguration'] ?? {}),
          namedTemplate: NamedTemplate.values.byName(data['namedTemplate']),
          rootLetters: RootLetters.fromJson(data['rootLetters']),
          translation: data['translation'],
          verbalNouns: List.from(data['verbalNounCodes'] ?? [])
              .map((e) => VerbalNoun.values.byName(e as String))
              .toList());
}
