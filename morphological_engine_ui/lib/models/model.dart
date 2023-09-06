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

class ConjugationEntry {
  String id;
  bool checked;
  NamedTemplate family;
  RootLetters rootLetters;

  ConjugationEntry(
      {required this.id,
      this.checked = false,
      this.family = NamedTemplate.FormICategoryAGroupUTemplate,
      this.rootLetters = const RootLetters()});

  ConjugationEntry copy(
      {bool? checked, NamedTemplate? family, RootLetters? rootLetters}) {
    return ConjugationEntry(
        id: id,
        checked: checked ?? this.checked,
        family: family ?? this.family,
        rootLetters: rootLetters ?? this.rootLetters);
  }

  @override
  String toString() {
    return "ConjugationEntry(id: $id, checked: $checked, family: ${family.displayValue()}, rootLetters: ${rootLetters.displayValue()})";
  }
}
