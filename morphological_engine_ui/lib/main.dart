import 'dart:convert';
import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'utils/ui_utils.dart';
import 'package:provider/provider.dart';
import 'package:uuid/uuid.dart';
import 'widgets/table.dart';
import 'models/model.dart';

void main() {
  runApp(const MorphologicalEngine());
}

class MorphologicalEngine extends StatelessWidget {
  const MorphologicalEngine({super.key});

  final String _title = 'Morphological Engine';

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => ConjugationTemplate()),
        ChangeNotifierProxyProvider<ConjugationTemplate, ConjugationInput>(
            create: (context) => ConjugationInput(id: ""),
            update: (context, template, input) {
              if (input == null) {
                throw ArgumentError.notNull('input');
              }
              input.template = template;
              return input;
            })
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
                    icon: const Icon(Icons.remove), onPressed: _removeRows))
          ]),
      body: const Padding(
          padding: EdgeInsets.all(8.0),
          child: Center(child: MorphologicalEngineTableView())),
    );
  }

  void _addRow() {
    var template = context.read<ConjugationTemplate>();
    template.addOrUpdate(ConjugationInput(id: const Uuid().v4()));
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
        template.inputs = ConjugationTemplate.fromJson(json).inputs;
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
}
