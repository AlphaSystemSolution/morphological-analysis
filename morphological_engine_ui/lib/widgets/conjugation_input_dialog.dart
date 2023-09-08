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
  final _arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  final _formKey = GlobalKey<_ConjugationEntryDialogState>();
  final _rootLettersController = TextEditingController();
  final _translationController = TextEditingController();
  final List<NamedTemplate> _namedTemplates = NamedTemplate.values;
  late final _keyboardLayoutBuilder = LayoutBuilder(
      builder: (_, constrains) => ArabicKeyboardDialog(
          rootLetters: _rootLetters,
          width: constrains.minWidth * 0.6,
          height: constrains.minHeight * 0.35,
          onChanged: (rl) {
            var input = context.read<ConjugationInput>();
            input.updateOnly(input.copy(rootLetters: rl));
            setState(() {
              _rootLettersController.text = rl.displayValue();
              _rootLetters = rl;
            });
          }));

  RootLetters _rootLetters = const RootLetters();
  NamedTemplate _namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate;

  @override
  void dispose() {
    super.dispose();
    _rootLettersController.dispose();
    _translationController.dispose();
  }

  @override
  void initState() {
    super.initState();
    _translationController.addListener(() => setState(() {
          var input = context.read<ConjugationInput>();
          input.updateOnly(input.copy(translation: _translationController.text));
        }));
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      scrollable: true,
      title: const Text("Edit Conjugation", textAlign: TextAlign.center),
      content: _buildForm,
      actions: <Widget>[
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        TextButton(
            onPressed: () {
              var input = context.read<ConjugationInput>();
              input.updateParent();
              Navigator.pop(context, 'OK');
            },
            child: const Text("OK"))
      ],
    );
  }

  get _buildForm => SizedBox(
      width: widget.width,
      height: widget.height,
      child: Consumer<ConjugationInput>(
          builder: (context, input, child) {
            _rootLettersController.text = input.rootLetters.displayValue();
            _translationController.text = input.translation;
            _namedTemplate = input.namedTemplate;
            _rootLetters = input.rootLetters;

            return child!;
          },
          child: Form(
              key: _formKey,
              child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Text("Root Letters:", style: _labelStyle),
                    const SizedBox(height: 16.0),
                    _buildRootLettersWidget,
                    const SizedBox(height: 16.0),
                    Text("Family:", style: _labelStyle),
                    const SizedBox(height: 16.0),
                    _buildFamilyWidget,
                    const SizedBox(height: 16.0),
                    Text("Translation:", style: _labelStyle),
                    const SizedBox(height: 16.0),
                    _buildTranslationWidget
                  ]))));

  get _buildRootLettersWidget =>
      Row(mainAxisAlignment: MainAxisAlignment.start, children: [
        Expanded(
            child: TextFormField(
          controller: _rootLettersController,
          style: _arabicRegularStyle,
          textDirection: TextDirection.rtl,
          readOnly: true,
        )),
        TextButton(
            onPressed: () => _showKeyboard,
            child: const Icon(Icons.keyboard_alt_outlined))
      ]);

  get _showKeyboard => showDialog(
      context: context,
      builder: (BuildContext context) => _keyboardLayoutBuilder);

  get _buildFamilyWidget => Directionality(
      textDirection: TextDirection.rtl,
      child: DropdownButton<NamedTemplate>(
          value: _namedTemplate,
          alignment: AlignmentDirectional.centerEnd,
          items: _namedTemplates
              .map<DropdownMenuItem<NamedTemplate>>((e) => DropdownMenuItem(
                  value: e,
                  child: Text(e.displayValue(),
                      textDirection: TextDirection.rtl,
                      style: _arabicRegularStyle)))
              .toList(),
          onChanged: (NamedTemplate? value) {
            var input = context.read<ConjugationInput>();
            input.updateOnly(input.copy(namedTemplate: value!));
            setState(() => _namedTemplate = value);
          }));

  get _buildTranslationWidget =>
      Expanded(child: TextFormField(controller: _translationController));
}
