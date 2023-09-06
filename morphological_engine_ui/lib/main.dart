import 'package:flutter/material.dart';
import 'table.dart';
import 'models/arabic_letter.dart';
import 'models/model.dart';
import 'models/named_template.dart';

void main() {
  runApp(const MorphologicalEngine());
}

class MorphologicalEngine extends StatelessWidget {
  const MorphologicalEngine({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Morphological Engine',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.lightGreen),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Morphological engine'),
    );
  }
}

class MyWidget extends StatelessWidget {
  const MyWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}

class MyHomePage extends StatelessWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.lightGreen.shade300, title: Text(title)),
      body: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Center(
              child: MorphologicalEngineTableView(
            entries: [
              ConjugationEntry(
                  id: "1",
                  checked: true,
                  family: NamedTemplate.FormICategoryAGroupUTemplate,
                  rootLetters: const RootLetters(
                      firstRadical: ArabicLetter.Noon,
                      secondRadical: ArabicLetter.Sad,
                      thirdRadical: ArabicLetter.Ra),
                      translation: "To Help"),
              ConjugationEntry(
                  id: "2",
                  family: NamedTemplate.FormICategoryAGroupITemplate,
                  rootLetters: const RootLetters(
                      firstRadical: ArabicLetter.Ddad,
                      secondRadical: ArabicLetter.Ra,
                      thirdRadical: ArabicLetter.Ba),
                      translation: "To Strike")
            ],
            onChanged: (value) {},
          ))),
    );
  }
}
