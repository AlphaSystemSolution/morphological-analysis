import 'package:morphological_engine_ui/models/arabic_letter.dart';
import 'package:morphological_engine_ui/models/named_template.dart';

class RootLetters {
  const RootLetters(
      {this.firstRadical = ArabicLetter.Fa,
      this.secondRadical = ArabicLetter.Ain,
      this.thirdRadical = ArabicLetter.Lam,
      this.fourthRadical});

  final ArabicLetter firstRadical;
  final ArabicLetter secondRadical;
  final ArabicLetter thirdRadical;
  final ArabicLetter? fourthRadical;

  String displayValue() {
    return "$firstRadical $secondRadical $thirdRadical ${fourthRadical ?? ""}";
  }

  List<ArabicLetter> letters() {
    return [
      firstRadical,
      secondRadical,
      thirdRadical,
      fourthRadical ?? ArabicLetter.Space
    ];
  }

  updateFirstRadical(ArabicLetter value) => RootLetters(
      firstRadical: value,
      secondRadical: secondRadical,
      thirdRadical: thirdRadical,
      fourthRadical: fourthRadical);

  updateSecondRadical(ArabicLetter value) => RootLetters(
      firstRadical: firstRadical,
      secondRadical: value,
      thirdRadical: thirdRadical,
      fourthRadical: fourthRadical);

  updateThirdRadical(ArabicLetter value) => RootLetters(
      firstRadical: firstRadical,
      secondRadical: secondRadical,
      thirdRadical: value,
      fourthRadical: fourthRadical);

  updateFourthRadical(ArabicLetter? value) => RootLetters(
      firstRadical: firstRadical,
      secondRadical: secondRadical,
      thirdRadical: thirdRadical,
      fourthRadical: value);
}

class ConjugationInput {
  String id;
  bool checked;
  NamedTemplate namedTemplate;
  RootLetters rootLetters;
  String translation;

  ConjugationInput(
      {required this.id,
      this.checked = false,
      this.namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
      this.rootLetters = const RootLetters(),
      this.translation = ""});

  ConjugationInput copy(
      {bool? checked, NamedTemplate? family, RootLetters? rootLetters, String? translation}) {
    return ConjugationInput(
        id: id,
        checked: checked ?? this.checked,
        namedTemplate: family ?? this.namedTemplate,
        rootLetters: rootLetters ?? this.rootLetters,
        translation: translation ?? this.translation);
  }

  @override
  String toString() {
    return """ConjugationEntry(
       id: $id,
       checked: $checked,
       family: ${namedTemplate.displayValue()},
       rootLetters: ${rootLetters.displayValue()},
       translation: $translation
       )""";
  }
}
