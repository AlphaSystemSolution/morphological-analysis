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
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.lightGreen),
          useMaterial3: true,
        ),
        home: MyHomePage(title: _title),
      ),
    );
  }
}

class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  Widget build(BuildContext context) {
    var templateProvider = context.read<ConjugationTemplate>();
    templateProvider.inputs = [
      ConjugationInput(
          id: const Uuid().v4(),
          checked: true,
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
          backgroundColor: Colors.lightGreen.shade300, title: Text(title)),
      body: const Padding(
          padding: EdgeInsets.all(8.0),
          child: Center(
              child: MorphologicalEngineTableView())),
    );
  }
}
