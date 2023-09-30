import 'dart:convert';
import 'dart:io';

import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:provider/provider.dart';
import 'package:uuid/uuid.dart';
import 'package:window_manager/window_manager.dart';

import 'models/conjugation_input.dart';
import 'models/conjugation_template.dart';
import 'utils/service.dart';
import 'utils/ui_utils.dart';
import 'widgets/chart_configuration_dialog.dart';
import 'widgets/table.dart';

void main() async {
  await setupWindow();
  runApp(const MorphologicalEngine());
}

Future<void> setupWindow() async {
  if (!kIsWeb && (Platform.isWindows || Platform.isLinux || Platform.isMacOS)) {
    WidgetsFlutterBinding.ensureInitialized();
    await windowManager.ensureInitialized();

    WindowOptions windowOptions = const WindowOptions(
      size: Size(1200, 800),
      center: true,
      backgroundColor: Colors.transparent,
      skipTaskbar: false,
      title: "Morphological Engine",
      titleBarStyle: TitleBarStyle.normal,
    );

    windowManager.waitUntilReadyToShow(windowOptions, () async {
      await windowManager.show();
      await windowManager.focus();
    });
  }
}

class MorphologicalEngine extends StatelessWidget {
  const MorphologicalEngine({super.key});

  final String _title = 'Morphological Engine';

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
            create: (context) => ConjugationTemplate(id: const Uuid().v4()))
      ],
      child: MaterialApp(
        title: _title,
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.tealAccent),
          useMaterial3: true,
        ),
        home: MyHomePage(title: _title),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.tealAccent,
          title: Text(widget.title),
          actions: [
            Tooltip(
                preferBelow: true,
                message: "Open",
                child: IconButton(
                    icon: const Icon(Icons.file_open),
                    onPressed: () =>
                        _openFile(context.read<ConjugationTemplate>()))),
            Tooltip(
                preferBelow: true,
                message: "Save",
                child: IconButton(
                    icon: const Icon(Icons.save),
                    onPressed: () {
                      var template = context.read<ConjugationTemplate>();
                      _saveFile(template, jsonEncode(template), false);
                    })),
            Tooltip(
                preferBelow: true,
                message: "Save as",
                child: IconButton(
                    icon: const Icon(Icons.save_as),
                    onPressed: () {
                      var template = context.read<ConjugationTemplate>();
                      _saveFile(template, jsonEncode(template), true);
                    })),
            const VerticalDivider(
                color: Colors.black,
                width: 20,
                thickness: 1,
                indent: 8,
                endIndent: 8),
            Tooltip(
                preferBelow: true,
                message: "Add new row",
                child: IconButton(
                    icon: const Icon(Icons.add), onPressed: _addRow)),
            Tooltip(
                preferBelow: true,
                message: "Remove selected row(s)",
                child: IconButton(
                    icon: const Icon(Icons.remove), onPressed: _removeRows)),
            const VerticalDivider(
                color: Colors.black,
                width: 20,
                thickness: 1,
                indent: 8,
                endIndent: 8),
            Tooltip(
              preferBelow: true,
              message: "Export chart to MS Word document",
              child: IconButton(
                icon: const FaIcon(FontAwesomeIcons.fileWord),
                onPressed: _exportToWordDoc,
              ),
            ),
            const VerticalDivider(
                color: Colors.black,
                width: 20,
                thickness: 1,
                indent: 8,
                endIndent: 8),
            Tooltip(
                preferBelow: true,
                message: "Chart Setting",
                child: IconButton(
                    icon: const Icon(Icons.settings),
                    onPressed: () {
                      _updateChartConfiguration();
                    }))
          ]),
      body: const Padding(
          padding: EdgeInsets.all(8.0),
          child: Center(child: MorphologicalEngineTableView())),
    );
  }

  void _addRow() {
    var template = context.read<ConjugationTemplate>();
    template.addOrUpdate(ConjugationInput(id: const Uuid().v4(), index: -1));
  }

  void _removeRows() {
    var template = context.read<ConjugationTemplate>();
    if (template.hasSelectedRows) {
      Utils.showConfirmationDialog(
          context,
          true,
          "Remove Selected Row(s)",
          "Are you sure you want to remove selected row(s)!",
          () => template.removeSelectedRows());
    } else {
      Utils.showConfirmationDialog(context, false, "Remove Selected Row(s)",
          "Nothing to remove", () => {});
    }
  }

  void _openFile(ConjugationTemplate template) async {
    var result = await FilePicker.platform.pickFiles(
        type: FileType.custom,
        allowedExtensions: ["json"],
        allowMultiple: false);

    if (result != null) {
      var file = result.files.first;
      if (file.path != null) {
        var filePath = file.path!;
        template.filePath = filePath;
        template.fileName = file.name;
        var json = jsonDecode(await File(filePath).readAsString());
        var newTemplate = ConjugationTemplate.fromJson(json);
        template.update(
            newTemplate.id, newTemplate.chartConfiguration, newTemplate.inputs);
      }
    }
  }

  Future<void> _saveFile(
      ConjugationTemplate template, String json, bool saveAs) async {
    if (template.inputs.isNotEmpty) {
      String? outputFile;
      if (saveAs) {
        outputFile = await FilePicker.platform.saveFile(
            type: FileType.custom,
            initialDirectory: template.parentPath,
            fileName: template.fileName,
            allowedExtensions: ["json"]);
      } else {
        outputFile = template.filePath;
      }

      if (outputFile != null) {
        var file = File(outputFile);
        await file.writeAsString(json);
        if (saveAs) {
          template.updateFile(Utils.toPlatformFile(outputFile));
        }
      }
    }
  }

  void _updateChartConfiguration() => showDialog(
      context: context, builder: (context) => const ChartConfigurationDialog());

  Future<void> _exportToWordDoc() async {
    var template = context.read<ConjugationTemplate>();
    String fileName = "${template.id}.docx";
    String? outputFile = await FilePicker.platform.saveFile(
        type: FileType.custom,
        initialDirectory: template.parentPath,
        fileName: fileName,
        allowedExtensions: ["docx"]);

    if (outputFile != null) {
      var file = File(outputFile);
      var bytes = await MorphologicalEngineService.exportToWordDoc(template);
      if (bytes != null) {
        await file.writeAsBytes(bytes);
      }
    }
  }
}
