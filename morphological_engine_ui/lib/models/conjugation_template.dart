import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';
import 'package:uuid/uuid.dart';

import '../utils/ui_utils.dart';
import 'chart_configuration.dart';
import 'conjugation_input.dart';

class ConjugationTemplate extends ChangeNotifier {
  String id;
  String _filePath = "";
  String _parentPath = "";
  String fileName = "";
  ChartConfiguration _chartConfiguration = const ChartConfiguration();
  List<ConjugationInput> _inputs = [];
  List<ConjugationInput> _selectedRows = [];
  int selectedIndex = 0;

  ConjugationTemplate(
      {required this.id,
      ChartConfiguration chartConfiguration = const ChartConfiguration(),
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

  void update(String id, ChartConfiguration chartConfiguration,
      List<ConjugationInput> inputs) {
    this.id = id;
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

  void removeSelectedRows() {
    _selectedRows.map((e) => e.id).toList().forEach((id) {
      _inputs.removeWhere((e) => id == e.id);
    });
    _inputs = _populateIndex(inputs);
    notifyListeners();
  }

  void updateFile(PlatformFile file) {
    filePath = file.path!;
    fileName = file.name;
    notifyListeners();
  }

  get hasSelectedRows => _selectedRows.isNotEmpty;

  @override
  int get hashCode =>
      hash3(id.hashCode, chartConfiguration.hashCode, hashObjects(_inputs));

  @override
  bool operator ==(Object other) =>
      other is ConjugationTemplate &&
      id == other.id &&
      chartConfiguration == other._chartConfiguration &&
      listEquals(_inputs, other._inputs);

  Map toJson() => {
        "id": id,
        "chartConfiguration": chartConfiguration.toJson(),
        "inputs": _inputs.map((e) => e.toJson()).toList()
      };

  @override
  String toString() => '''ConjugationTemplate(
    id: $id,
    chartConfiguration: $chartConfiguration,
    inputs: ${_inputs.map((e) => e.toString())}
)''';

  factory ConjugationTemplate.fromJson(Map<String, dynamic> data) {
    var id = data['id'] ?? const Uuid().v4();
    var inputs = List.from(data['inputs'] as List)
        .map((e) => ConjugationInput.fromJson(e))
        .toList();
    inputs = _populateIndex(inputs);
    var chartConfiguration =
        ChartConfiguration.fromJson(data['chartConfiguration'] ?? {});
    return ConjugationTemplate(
        id: id, chartConfiguration: chartConfiguration, inputs: inputs);
  }

  static List<ConjugationInput> _populateIndex(List<ConjugationInput> src) =>
      src
          .asMap()
          .map((index, input) => MapEntry(index, input.copy(index: index)))
          .values
          .toList();
}
