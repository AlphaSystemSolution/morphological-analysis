// ignore_for_file: constant_identifier_names

import 'package:morphological_engine_ui/models/named_template.dart';

enum VerbalNoun implements Comparable<VerbalNoun> {
  FormIV1(label: "فَعْلٌ"),
  FormIV2(label: "فُعُلٌ"),
  FormIV3(label: "فُعْلٌ"),
  FormIV4(label: "فَعَلٌ"),
  FormIV5(label: "فَعِلٌ"),
  FormIV6(label: "فُعَلٌ"),
  FormIV7(label: "فِعْلٌ"),
  FormIV8(label: "فِعَلٌ"),
  FormIV9(label: "فَعْلَةٌ"),
  FormIV10(label: "فِعْلَةٌ"),
  FormIV11(label: "فُعْلَةٌ"),
  FormIV12(label: "فَعَلَةٌ"),
  FormIV13(label: "فَعِلَةٌ"),
  FormIV14(label: "فَعَالَةٌ"),
  FormIV15(label: "فِعَالَةٌ"),
  FormIV16(label: "فُعُوْلٌ"),
  FormIV17(label: "فَعُوْلٌ"),
  FormIV18(label: "فَعُوْلَةٌ"),
  FormIV19(label: "فَعَالٌ"),
  FormIV20(label: "فِعَالٌ"),
  FormIV21(label: "فُعَالٌ"),
  FormIV22(label: "فِعْلَى"),
  FormIV23(label: "فَعْلَى"),
  FormIV24(label: "فُعْلَى"),
  FormIV25(label: "فَعِيلٌ"),
  FormIV26(label: "فُعْلَانٌ"),
  FormIV27(label: "مَفْعَلَةٌ"),
  FormIV28(label: "مَفْعَلَةٌ"),
  FormII(label: "تَفْعِيلٌ"),
  FormIIDefectiveVerb(label: "تَفْعِيلَةٌ"),
  FormIIIV1(label: "فِعَالٌ"),
  FormIIIV2(label: "مُفَاعِلَةٌ"),
  FormIIIDefectiveVerb(label: "مُفَاعَلَةٌ"),
  FormIV(label: "إِفْعَالٌ"),
  FormV(label: "تَفْعُّلٌ"),
  FormVI(label: "تَفَاعُلٌ"),
  FormVII(label: "إِنْفِعَالٌ"),
  FormVIII(label: "إِفْتِعَالُ"),
  FormX(label: "إِسْتَفْعَالٌ");

  final String label;

  const VerbalNoun({
    required this.label,
  });

  @override
  int compareTo(VerbalNoun other) => label.compareTo(other.label);

  @override
  String toString() => label;

  static Map _byNamedTemplate() => {
    NamedTemplate.FormIITemplate: [VerbalNoun.FormII],
    NamedTemplate.FormIIITemplate: [VerbalNoun.FormIIIV1, VerbalNoun.FormIIIV2],
    NamedTemplate.FormIVTemplate: [VerbalNoun.FormIV],
    NamedTemplate.FormVTemplate: [VerbalNoun.FormV],
    NamedTemplate.FormVITemplate: [VerbalNoun.FormVI],
    NamedTemplate.FormVIITemplate: [VerbalNoun.FormVII],
    NamedTemplate.FormVIIITemplate: [VerbalNoun.FormVIII],
    NamedTemplate.FormIXTemplate: [],
    NamedTemplate.FormXTemplate: [VerbalNoun.FormX]
  };

  static List<VerbalNoun> byNamedTemplate(NamedTemplate template) {
    var values = VerbalNoun._byNamedTemplate()[template] as List<VerbalNoun>?;
    if (values == null) {
      return [];
    }
    return values;
  }
}
