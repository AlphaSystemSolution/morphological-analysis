// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'arabic_keyboard_dialog.dart';
import '../models/model.dart';
import '../models/named_template.dart';

class ConjugationEntryDialog extends StatefulWidget {
  ConjugationEntryDialog(
      {super.key, required this.width, required this.height});

  double width;
  double height;

  @override
  State<ConjugationEntryDialog> createState() => _ConjugationEntryDialogState();
}

class _ConjugationEntryDialogState extends State<ConjugationEntryDialog> {
  final _labelStyle = const TextStyle(fontWeight: FontWeight.bold);
  final arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  final _formKey = GlobalKey<_ConjugationEntryDialogState>();
  final TextEditingController _rootLettersController = TextEditingController();
  final TextEditingController _translationController = TextEditingController();

  ConjugationInput _entry = ConjugationInput(id: "1");
  final List<NamedTemplate> _namedTemplates = NamedTemplate.values;

  @override
  void initState() {
    super.initState();
    _translationController.addListener(() => setState(() {
          _entry = _entry.copy(translation: _translationController.text);
        }));
  }

  @override
  void dispose() {
    super.dispose();
    _rootLettersController.dispose();
    _translationController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var input = context.watch<ConjugationInput>();
    return AlertDialog(
      scrollable: true,
      title: const Text("Edit Conjugation", textAlign: TextAlign.center),
      content: buildForm(input),
      actions: <Widget>[
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        TextButton(
            onPressed: () {
              var input = context.read<ConjugationInput>();
              input.update(
                  id: input.id,
                  checked: input.checked,
                  namedTemplate: input.namedTemplate,
                  rootLetters: input.rootLetters,
                  translation: input.translation);
              Navigator.pop(context, 'OK');
            },
            child: const Text("OK"))
      ],
    );
  }

  Widget buildForm(ConjugationInput input) {
    return SizedBox(
      width: widget.width,
      height: widget.height,
      child: Form(
          key: _formKey,
          child:
              Column(crossAxisAlignment: CrossAxisAlignment.stretch, children: [
            Text("Root Letters:", style: _labelStyle),
            const SizedBox(height: 16.0),
            buildRootLettersWidget(input.rootLetters),
            const SizedBox(height: 16.0),
            Text("Family:", style: _labelStyle),
            const SizedBox(height: 16.0),
            buildFamilyWidget(input.namedTemplate),
            const SizedBox(height: 16.0),
            Text("Translation:", style: _labelStyle),
            const SizedBox(height: 16.0),
            buildTranslationWidget(input.translation)
          ])),
    );
  }

  Widget buildRootLettersWidget(RootLetters rootLetters) {
    _rootLettersController.text = rootLetters.displayValue();
    return Row(mainAxisAlignment: MainAxisAlignment.start, children: [
      Expanded(
          child: TextFormField(
        controller: _rootLettersController,
        style: arabicRegularStyle,
        textDirection: TextDirection.rtl,
        readOnly: true,
      )),
      TextButton(
          onPressed: () => showKeyboard(rootLetters),
          child: const Icon(Icons.keyboard_alt_outlined))
    ]);
  }

  Future<dynamic> showKeyboard(RootLetters rootLetters) {
    return showDialog(
        context: context,
        builder: (BuildContext context) => LayoutBuilder(
            builder: (_, constrains) => ArabicKeyboardDialog(
                rootLetters: rootLetters,
                width: constrains.minWidth * 0.4,
                height: constrains.minHeight * 0.25,
                onChanged: (rl) => {
                      setState(() {
                        _rootLettersController.text =
                            rootLetters.displayValue();
                        _entry = _entry.copy(rootLetters: rootLetters);
                      })
                    })));
  }

  Widget buildFamilyWidget(NamedTemplate namedTemplate) {
    return Directionality(
        textDirection: TextDirection.rtl,
        child: DropdownButton<NamedTemplate>(
            value: namedTemplate,
            alignment: AlignmentDirectional.centerEnd,
            items: _namedTemplates
                .map<DropdownMenuItem<NamedTemplate>>((e) => DropdownMenuItem(
                    value: e,
                    child: Text(e.displayValue(),
                        textDirection: TextDirection.rtl,
                        style: arabicRegularStyle)))
                .toList(),
            onChanged: (NamedTemplate? value) {
              setState(() {
                namedTemplate = value!;
                _entry = _entry.copy(namedTemplate: namedTemplate);
              });
            }));
  }

  Widget buildTranslationWidget(String translation) {
    _translationController.text = translation;
    return Expanded(child: TextFormField(controller: _translationController));
  }
}
