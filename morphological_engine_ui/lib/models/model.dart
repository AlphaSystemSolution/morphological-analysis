import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:morphological_engine_ui/models/chart_configuration.dart';
import 'package:morphological_engine_ui/models/verbal_noun.dart';
import 'package:morphological_engine_ui/utils/ui_utils.dart';
import 'package:quiver/core.dart';
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

class ConjugationInput extends ChangeNotifier {
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

  void updateOnly(ConjugationInput other) {
    id = other.id;
    index = other.index;
    checked = other.checked;
    conjugationConfiguration = other.conjugationConfiguration;
    namedTemplate = other.namedTemplate;
    rootLetters = other.rootLetters;
    translation = other.translation;
    verbalNouns = other.verbalNouns;
    notifyListeners();
  }

  void update(
      {String? id,
      int? index,
      bool? checked,
      ConjugationConfiguration? conjugationConfiguration,
      NamedTemplate? namedTemplate,
      RootLetters? rootLetters,
      String? translation,
      List<VerbalNoun>? verbalNouns}) {
    this.id = id ?? this.id;
    this.index = index ?? this.index;
    this.checked = checked ?? this.checked;
    this.conjugationConfiguration =
        conjugationConfiguration ?? this.conjugationConfiguration;
    this.namedTemplate = namedTemplate ?? this.namedTemplate;
    this.rootLetters = rootLetters ?? this.rootLetters;
    this.translation = translation ?? this.translation;
    this.verbalNouns = verbalNouns ?? this.verbalNouns;
    notifyListeners();
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

class ConjugationTemplate extends ChangeNotifier {
  String _filePath = "";
  String _parentPath = "";
  String fileName = "";
  ChartConfiguration _chartConfiguration = const ChartConfiguration();
  List<ConjugationInput> _inputs = [];
  List<ConjugationInput> _selectedRows = [];
  int selectedIndex = 0;
  

  ConjugationTemplate(
      {ChartConfiguration chartConfiguration = const ChartConfiguration(),
      List<ConjugationInput> inputs = const []}) {
    _chartConfiguration = chartConfiguration;
    _inputs = inputs;
  }

  String get filePath => _filePath;

  set filePath(String filePath) {
    _filePath = filePath;
    _parentPath = Utils.getParentPath(_filePath);
  }

  String get parentPath => _parentPath;

  ChartConfiguration get chartConfiguration => _chartConfiguration;

  set chartConfiguration(ChartConfiguration chartConfiguration) {
    _chartConfiguration = chartConfiguration;
    notifyListeners();
  }

  List<ConjugationInput> get inputs => _inputs;

  set inputs(List<ConjugationInput> inputs) {
    _inputs = inputs;
    notifyListeners();
  }

  void update(
      ChartConfiguration chartConfiguration, List<ConjugationInput> inputs) {
    _chartConfiguration = chartConfiguration;
    _inputs = inputs;
    notifyListeners();
  }

  ConjugationInput get currentConjugationInput => _inputs[selectedIndex];

  List<ConjugationInput> get selectedRows => _selectedRows;

  void addOrUpdate(ConjugationInput input) {
    int index = input.index;
    if (index < -1 || (index > -1 && index != selectedIndex)) {
      return;
    }
    if (index == -1) {
      var v = input.copy(index: _inputs.length);
      _inputs.add(v);
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

  void updateFile(PlatformFile file) {
    filePath = file.path!;
    fileName = file.name;
    notifyListeners();
  }

  get hasSelectedRows => _selectedRows.isNotEmpty;

  @override
  int get hashCode => hash2(chartConfiguration.hashCode, hashObjects(_inputs));

  @override
  bool operator ==(Object other) =>
      other is ConjugationTemplate &&
      chartConfiguration == other._chartConfiguration &&
      listEquals(_inputs, other._inputs);

  Map toJson() => {
        "chartConfiguration": chartConfiguration.toJson(),
        "inputs": _inputs.map((e) => e.toJson()).toList()
      };

  @override
  String toString() => '''ConjugationTemplate(
    chartConfiguration: $chartConfiguration,
    inputs: ${_inputs.map((e) => e.toString())}
)''';

  factory ConjugationTemplate.fromJson(Map<String, dynamic> data) {
    var inputs = List.from(data['inputs'] as List)
        .map((e) => ConjugationInput.fromJson(e))
        .toList()
        .asMap()
        .map((index, input) => MapEntry(index, input.copy(index: index)))
        .values
        .toList();
    var chartConfiguration =
        ChartConfiguration.fromJson(data['chartConfiguration'] ?? {});
    return ConjugationTemplate(
        chartConfiguration: chartConfiguration, inputs: inputs);
  }
}
