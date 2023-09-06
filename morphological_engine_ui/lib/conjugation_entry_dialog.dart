// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'arabic_keyboard_dialog.dart';
import 'models/model.dart';
import 'models/named_template.dart';

class ConjugationEntryDialog extends StatefulWidget {
  ConjugationEntryDialog(
      {super.key,
      required this.entry,
      required this.width,
      required this.height,
      required this.onChanged});

  ConjugationEntry entry;
  double width;
  double height;
  final ValueChanged<ConjugationEntry> onChanged;

  @override
  State<ConjugationEntryDialog> createState() => _ConjugationEntryDialogState();
}

class _ConjugationEntryDialogState extends State<ConjugationEntryDialog> {
  final labelStyle = const TextStyle(fontWeight: FontWeight.bold);
  final arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  final _formKey = GlobalKey<_ConjugationEntryDialogState>();
  final TextEditingController _rootLettersController = TextEditingController();
  final TextEditingController _translationController = TextEditingController();

  ConjugationEntry _entry = ConjugationEntry(id: "1");
  RootLetters rootLetters = const RootLetters();
  NamedTemplate namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate;
  List<NamedTemplate> namedTemplates = NamedTemplate.values;
  String translation = "";

  @override
  void initState() {
    super.initState();
    _translationController.addListener(() => setState(() {
        translation = _translationController.text;
        _entry = _entry.copy(translation: translation);
      })
    );
    setState(() {
      _entry = widget.entry;
      rootLetters = _entry.rootLetters;
      namedTemplate = _entry.family;
      translation = _entry.translation;
      _rootLettersController.text = rootLetters.displayValue();
      _translationController.text = translation;
    });
  }

  @override
  void dispose() {
    super.dispose();
    _rootLettersController.dispose();
    _translationController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      scrollable: true,
      title: const Text("Edit Conjugation", textAlign: TextAlign.center),
      content: buildForm,
      actions: <Widget>[
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        TextButton(
            onPressed: () {
              widget.onChanged(_entry);
              Navigator.pop(context, 'OK');
            },
            child: const Text("OK"))
      ],
    );
  }

  get buildForm => SizedBox(
        width: widget.width,
        height: widget.height,
        child: Form(
            key: _formKey,
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text("Root Letters:", style: labelStyle),
                  const SizedBox(height: 16.0),
                  buildRootLettersWidget,
                  const SizedBox(height: 16.0),
                  Text("Family:", style: labelStyle),
                  const SizedBox(height: 16.0),
                  buildFamilyWidget,
                  const SizedBox(height: 16.0),
                  Text("Translation:", style: labelStyle),
                  const SizedBox(height: 16.0),
                  buildTranslationWidget
                ])),
      );

  get buildRootLettersWidget =>
      Row(mainAxisAlignment: MainAxisAlignment.start, children: [
        Expanded(
            child: TextFormField(
          controller: _rootLettersController,
          style: arabicRegularStyle,
          textDirection: TextDirection.rtl,
          readOnly: true,
        )),
        TextButton(
            onPressed: () => showKeyboard(),
            child: const Icon(Icons.keyboard_alt_outlined))
      ]);

  Future<dynamic> showKeyboard() {
    return showDialog(
        context: context,
        builder: (BuildContext context) => LayoutBuilder(
            builder: (_, constrains) => ArabicKeyboardDialog(
                rootLetters: rootLetters,
                width: constrains.minWidth * 0.4,
                height: constrains.minHeight * 0.25,
                onChanged: (rl) => {
                      setState(() {
                        rootLetters = rl;
                        _rootLettersController.text = rootLetters.displayValue();
                        _entry = _entry.copy(rootLetters: rootLetters);
                      })
                    })));
  }

  get buildFamilyWidget => Directionality(
      textDirection: TextDirection.rtl,
      child: DropdownButton<NamedTemplate>(
          value: namedTemplate,
          alignment: AlignmentDirectional.centerEnd,
          items: namedTemplates
              .map<DropdownMenuItem<NamedTemplate>>((e) => DropdownMenuItem(
                  value: e,
                  child: Text(e.displayValue(),
                      textDirection: TextDirection.rtl,
                      style: arabicRegularStyle)))
              .toList(),
          onChanged: (NamedTemplate? value) {
            setState(() {
              namedTemplate = value!;
              _entry = _entry.copy(family: namedTemplate);
            });
          }));

     get buildTranslationWidget =>
      Expanded(child: TextFormField(controller: _translationController));     
}
