import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:uuid/uuid.dart';
import 'widgets/table.dart';
import 'models/arabic_letter.dart';
import 'models/model.dart';
import 'models/named_template.dart';

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
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
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
    var templateProvider = context.read<ConjugationTemplate>();
    templateProvider.inputs = [
      ConjugationInput(
          id: const Uuid().v4(),
          namedTemplate: NamedTemplate.FormICategoryAGroupUTemplate,
          rootLetters: const RootLetters(
              firstRadical: ArabicLetter.Noon,
              secondRadical: ArabicLetter.Sad,
              thirdRadical: ArabicLetter.Ra),
          translation: "To Help"),
      ConjugationInput(
          id: const Uuid().v4(),
          namedTemplate: NamedTemplate.FormICategoryAGroupITemplate,
          rootLetters: const RootLetters(
              firstRadical: ArabicLetter.Ddad,
              secondRadical: ArabicLetter.Ra,
              thirdRadical: ArabicLetter.Ba),
          translation: "To Strike")
    ];
    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.teal,
          title: Text(widget.title),
          actions: [
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
      showDialog(
          context: context,
          builder: (BuildContext context) {
            return AlertDialog(
                title: const Text("Remove Selected Row(s)"),
                content: const Text(
                    "Are you sure you want to remove selected row(s)!"),
                actions: <Widget>[
                  TextButton(
                      onPressed: () => Navigator.pop(context, 'Cancel'),
                      child: const Text("Cancel")),
                  TextButton(
                      onPressed: () {
                        Navigator.pop(context, 'OK');
                        template.removeSelectedRows();
                      },
                      child: const Text("OK"))
                ]);
          });
    } else {
      showDialog(
          context: context,
          builder: (BuildContext context) {
            return AlertDialog(
              title: const Text("Remove Selected Row(s)"),
              content: const Text("Nothing to remove"),
              actions: <Widget>[
                TextButton(
                    onPressed: () => Navigator.pop(context, 'OK'),
                    child: const Text("OK"))
              ],
            );
          });
    }
  }
}
