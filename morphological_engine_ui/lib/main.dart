import 'package:flutter/material.dart';
import 'package:morphological_engine_ui/table.dart';

void main() {
  runApp(const MorphologicalEngine());
}

class MorphologicalEngine extends StatelessWidget {
  const MorphologicalEngine({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
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
        backgroundColor: Colors.lightGreen.shade300,
        title: Text(title)
      ),
      body: const Padding(
        padding: EdgeInsets.all(8.0),
        child: Center(
            child: MorphologicalEngineTableView())
      ),
    );
  }
}
