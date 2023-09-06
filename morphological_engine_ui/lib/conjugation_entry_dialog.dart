// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:morphological_engine_ui/arabic_keyboard_dialog.dart';
import 'package:morphological_engine_ui/models/model.dart';
import 'package:morphological_engine_ui/models/named_template.dart';

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
  final TextEditingController _controller = TextEditingController();
  ConjugationEntry _entry = ConjugationEntry(id: "1");
  RootLetters _rootLetters = const RootLetters();
  NamedTemplate namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate;
  List<NamedTemplate> namedTemplates = NamedTemplate.values;

  @override
  void initState() {
    super.initState();
    setState(() {
      _entry = widget.entry;
      _rootLetters = _entry.rootLetters;
      namedTemplate = _entry.family;
      _controller.text = _rootLetters.displayValue();
    });
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
                  const SizedBox(height: 8.0),
                  buildRootLettersWidget,
                  const SizedBox(height: 16.0),
                  Text("Family:", style: labelStyle),
                  const SizedBox(height: 8.0),
                  buildFamilyWidget
                ])),
      );

  get buildRootLettersWidget =>
      Row(mainAxisAlignment: MainAxisAlignment.start, children: [
        Expanded(
            child: TextFormField(
          controller: _controller,
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
                rootLetters: _rootLetters,
                width: constrains.minWidth * 0.4,
                height: constrains.minHeight * 0.25,
                onChanged: (rl) => {
                      setState(() {
                        _rootLetters = rl;
                        _controller.text = _rootLetters.displayValue();
                        _entry = _entry.copy(rootLetters: _rootLetters);
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
}
