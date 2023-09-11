import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';
import 'package:flutter/material.dart';
import 'package:uuid/uuid.dart';
import '../models/arabic_letter.dart';
import '../models/named_template.dart';

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

class ConjugationInput extends ChangeNotifier {
  String id;
  bool checked;
  NamedTemplate namedTemplate;
  RootLetters rootLetters;
  String translation;

  late ConjugationTemplate _template;

  ConjugationInput(
      {required this.id,
      this.checked = false,
      this.namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
      this.rootLetters = const RootLetters(),
      this.translation = "To Do"});

  ConjugationTemplate get template => _template;

  set template(ConjugationTemplate template) {
    _template = template;
    notifyListeners();
  }

  ConjugationInput copy(
      {String? id,
      bool? checked,
      NamedTemplate? namedTemplate,
      RootLetters? rootLetters,
      String? translation}) {
    return ConjugationInput(
        id: id ?? this.id,
        checked: checked ?? this.checked,
        namedTemplate: namedTemplate ?? this.namedTemplate,
        rootLetters: rootLetters ?? this.rootLetters,
        translation: translation ?? this.translation);
  }

  void updateOnly(ConjugationInput other) {
    id = other.id;
    checked = other.checked;
    namedTemplate = other.namedTemplate;
    rootLetters = other.rootLetters;
    translation = other.translation;
    notifyListeners();
  }

  void update(
      {String? id,
      bool? checked,
      NamedTemplate? namedTemplate,
      RootLetters? rootLetters,
      String? translation}) {
    this.id = id ?? this.id;
    this.checked = checked ?? this.checked;
    this.namedTemplate = namedTemplate ?? this.namedTemplate;
    this.rootLetters = rootLetters ?? this.rootLetters;
    this.translation = translation ?? this.translation;
    _template.addOrUpdate(this);
    notifyListeners();
  }

  void updateParent() {
    _template.addOrUpdate(this);
  }

  @override
  int get hashCode => hash4(id.hashCode, namedTemplate.hashCode,
      rootLetters.hashCode, translation.hashCode);

  @override
  bool operator ==(Object other) =>
      other is ConjugationInput &&
      id == other.id &&
      namedTemplate == other.namedTemplate &&
      rootLetters == other.rootLetters &&
      translation == other.translation;

  Map toJson() => {
        "id": id,
        "namedTemplate": namedTemplate.name,
        "rootLetters": rootLetters.toJson(),
        "translation": translation
      };

  @override
  String toString() {
    return """ConjugationInput(
  id: $id,
  checked: $checked,
  namedTemplate: ${namedTemplate.displayValue()},
  rootLetters: ${rootLetters.displayValue()},
  translation: $translation
)""";
  }

  factory ConjugationInput.fromJson(Map<String, dynamic> data) {
    var id = data['id'];
    id ??= const Uuid().v4();
    return ConjugationInput(
        id: id,
        checked: false,
        namedTemplate: NamedTemplate.values.byName(data['namedTemplate']),
        rootLetters: RootLetters.fromJson(data['rootLetters']),
        translation: data['translation']);
  }
}

class ConjugationTemplate extends ChangeNotifier {
  String filePath = "";
  String fileName = "";
  List<ConjugationInput> _inputs = [];
  List<ConjugationInput> _selectedRows = [];

  ConjugationTemplate({List<ConjugationInput> inputs = const []})
      : _inputs = inputs;

  List<ConjugationInput> get inputs => _inputs;

  set inputs(List<ConjugationInput> inputs) {
    _inputs = inputs;
    notifyListeners();
  }

  List<ConjugationInput> get selectedRows => _selectedRows;

  ConjugationInput? getById(String id) =>
      _inputs.where((e) => e.id == id).firstOrNull;

  int getIndex(String id) => _inputs.indexWhere((e) => e.id == id);

  void addOrUpdate(ConjugationInput input) {
    int index = getIndex(input.id);
    if (index <= -1) {
      _inputs.add(input);
    } else {
      _inputs[index] = input;
    }
    _selectedRows = _inputs.where((e) => e.checked).toList();
    notifyListeners();
  }

  void remove(ConjugationInput input) {
    _inputs.removeWhere((e) => e.id == input.id);
    notifyListeners();
  }

  void removeSelectedRows() {
    _selectedRows.map((e) => e.id).toList().forEach((id) {
      _inputs.removeWhere((e) => id == e.id);
    });
    notifyListeners();
  }

  get hasSelectedRows => _selectedRows.isNotEmpty;

  @override
  int get hashCode => hashObjects(_inputs);

  @override
  bool operator ==(Object other) =>
      other is ConjugationTemplate && listEquals(_inputs, other._inputs);

  Map toJson() => {
        "inputs": _inputs.map((e) => e.toJson()).toList()
      };

  @override
  String toString() {
    return '''ConjugationTemplate
  ${_inputs.map((e) => e.toString())}
''';
  }

  factory ConjugationTemplate.fromJson(Map<String, dynamic> data) {
    var inputs = List.from(data['inputs'] as List)
        .map((e) => ConjugationInput.fromJson(e))
        .toList();
    return ConjugationTemplate(inputs: inputs);
  }
}
