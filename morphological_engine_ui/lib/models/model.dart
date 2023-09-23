import 'package:quiver/core.dart';
import '../models/arabic_letter.dart';

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
    return "$firstRadical $secondRadical $thirdRadical ${fourthRadical ?? ""}"
        .trim();
  }

  String toBuckWalter() => "${firstRadical.code}${secondRadical.code}${thirdRadical.code}${fourthRadical?.code ?? ""}";

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

  Map toJson() => {
        "firstRadical": firstRadical.name,
        "secondRadical": secondRadical.name,
        "thirdRadical": thirdRadical.name,
        "fourthRadical": fourthRadical?.name
      };

  @override
  int get hashCode => hash4(firstRadical.hashCode, secondRadical.hashCode,
      thirdRadical.hashCode, fourthRadical?.hashCode);

  @override
  bool operator ==(Object other) =>
      other is RootLetters &&
      firstRadical == other.firstRadical &&
      secondRadical == other.secondRadical &&
      thirdRadical == other.thirdRadical &&
      fourthRadical == other.fourthRadical;

  factory RootLetters.fromJson(Map<String, dynamic> data) {
    var fourthRadicalStr = data['fourthRadical'];
    ArabicLetter? fourthRadical;
    if (fourthRadicalStr != null) {
      fourthRadical = ArabicLetter.values.byName(fourthRadicalStr);
    }
    return RootLetters(
        firstRadical: ArabicLetter.values.byName(data['firstRadical']),
        secondRadical: ArabicLetter.values.byName(data['secondRadical']),
        thirdRadical: ArabicLetter.values.byName(data['thirdRadical']),
        fourthRadical: fourthRadical);
  }
}

class ConjugationConfiguration {
  final bool skipRuleProcessing;
  final bool removePassiveLine;

  const ConjugationConfiguration(
      {this.skipRuleProcessing = false, this.removePassiveLine = false});

  @override
  int get hashCode => hash2(skipRuleProcessing, removePassiveLine);

  @override
  bool operator ==(Object other) =>
      other is ConjugationConfiguration &&
      skipRuleProcessing == other.skipRuleProcessing &&
      removePassiveLine == other.removePassiveLine;

  ConjugationConfiguration copy(
          {bool? skipRuleProcessing, bool? removePassiveLine}) =>
      ConjugationConfiguration(
          skipRuleProcessing: skipRuleProcessing ?? this.skipRuleProcessing,
          removePassiveLine: removePassiveLine ?? this.removePassiveLine);

  Map toJson() => {
        "skipRuleProcessing": skipRuleProcessing,
        "removePassiveLine": removePassiveLine
      };

  @override
  String toString() => """ConjugationConfiguration(
    skipRuleProcessing = $skipRuleProcessing,
    removePassiveLine = $removePassiveLine
  )""";

  factory ConjugationConfiguration.fromJson(Map<String, dynamic> data) =>
      ConjugationConfiguration(
          skipRuleProcessing: data['skipRuleProcessing'] ?? false,
          removePassiveLine: data['removePassiveLine'] ?? false);
}
