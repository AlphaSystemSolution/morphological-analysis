// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:morphological_engine_ui/models/verbal_noun.dart';
import 'package:multi_select_flutter/multi_select_flutter.dart';
import 'package:provider/provider.dart';
import 'arabic_keyboard_dialog.dart';
import '../models/model.dart';
import '../models/named_template.dart';

class ConjugationInputDialog extends StatefulWidget {
  ConjugationInputDialog(
      {super.key, required this.width, required this.height});

  double width;
  double height;

  @override
  State<ConjugationInputDialog> createState() => _ConjugationInputDialogState();
}

class _ConjugationInputDialogState extends State<ConjugationInputDialog> {
  final _labelStyle = const TextStyle(fontWeight: FontWeight.bold);
  final _arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  final _formKey = GlobalKey<_ConjugationInputDialogState>();
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

  ConjugationInput _previousValue = ConjugationInput(id: "1");
  RootLetters _rootLetters = const RootLetters();
  NamedTemplate _namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate;
  List<VerbalNoun> _verbalNouns = [];

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
      content: _buildForm,
      actions: <Widget>[
        TextButton(
            onPressed: () {
               var input = context.read<ConjugationInput>();
               input.updateOnly(_previousValue);
               Navigator.pop(context, 'Cancel');
            },
            child: const Text("Cancel")),
        TextButton(
            onPressed: () {
              var input = context.read<ConjugationInput>();
              input.updateOnly(
                  input.copy(translation: _translationController.text));
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
            Future.delayed(Duration.zero, () async {
              setState(() {
                _previousValue = input;
                _rootLettersController.text = input.rootLetters.displayValue();
                _translationController.text = input.translation;
                _namedTemplate = input.namedTemplate;
                _rootLetters = input.rootLetters;
                _verbalNouns = input.verbalNouns;
              });
            });

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
                    _buildTranslationWidget,
                    const SizedBox(height: 16.0),
                    Text("Verbal Nouns:", style: _labelStyle),
                    const SizedBox(height: 16.0),
                    _buildVerbalNounsWidget
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

  get _buildTranslationWidget => Expanded(
      child: TextFormField(
          controller: _translationController,
          textAlignVertical: TextAlignVertical.center));

  get _buildVerbalNounsWidget => Expanded(
        child: MultiSelectBottomSheetField(
            initialValue: _verbalNouns,
            decoration: BoxDecoration(
              color: Colors.tealAccent.withOpacity(0.1),
              borderRadius: const BorderRadius.all(Radius.circular(40)),
              border: Border.all(
                color: Colors.tealAccent,
                width: 2,
              ),
            ),
            initialChildSize: 0.4,
            listType: MultiSelectListType.CHIP,
            itemsTextStyle: _arabicRegularStyle,
            searchTextStyle: _arabicRegularStyle,
            searchHintStyle: _arabicRegularStyle,
            items: VerbalNoun.values
                .map((e) => MultiSelectItem<VerbalNoun?>(e, e.label))
                .toList(),
            onConfirm: (results) {
              var input = context.read<ConjugationInput>();
              var nonNulls = results.nonNulls.toList();
              input.updateOnly(input.copy(verbalNouns: nonNulls));
              setState(() => _verbalNouns = nonNulls);
            }),
      );
}
