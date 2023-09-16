import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:morphological_engine_ui/models/model.dart';
import 'package:morphological_engine_ui/models/named_template.dart';
import 'package:multi_select_flutter/multi_select_flutter.dart';
import 'package:provider/provider.dart';

import '../models/verbal_noun.dart';
import 'arabic_keyboard_dialog.dart';

class ConjugationInputDialog extends StatefulWidget {
  const ConjugationInputDialog({super.key});

  @override
  State<ConjugationInputDialog> createState() => _ConjugationInputDialogState();
}

class _ConjugationInputDialogState extends State<ConjugationInputDialog> {
  final _formKey = GlobalKey<FormBuilderState>();
  RootLetters? _rootLetters;
  List<VerbalNoun?>? _verbalNouns;
  static final _arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  static const _labelStyle = TextStyle(fontWeight: FontWeight.bold);
  static final _namedTemplateValues = NamedTemplate.values
      .map((e) => DropdownMenuItem(
            alignment: AlignmentDirectional.centerStart,
            value: e,
            child: Text(
              e.displayValue(),
              style: _arabicRegularStyle,
            ),
          ))
      .toList();

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      scrollable: true,
      title: const Text("Edit Conjugation Input", textAlign: TextAlign.center),
      content: Selector<ConjugationTemplate, ConjugationInput>(
        selector: (_, template) => template.currentConjugationInput,
        builder: (context, conjugationInput, child) => FormBuilder(
            key: _formKey,
            initialValue: {
              "rootLetters": conjugationInput.rootLetters.displayValue(),
              'namedTemplate': conjugationInput.namedTemplate,
              "translation": conjugationInput.translation,
              "skipRuleProcessing":
                  conjugationInput.conjugationConfiguration.skipRuleProcessing,
              "removePassiveLine":
                  conjugationInput.conjugationConfiguration.removePassiveLine,
            },
            child: Column(
              children: [
                _buildRootLettersWidget(conjugationInput.rootLetters),
                _buildNamedTemplateWidget,
                _buildTranslationWidget,
                _buildVerbalNounsWidget(conjugationInput.verbalNouns),
                _buildSkipRuleProcessingWidget,
                _buildRemovePassiveLineWidget,
              ],
            )),
      ),
      actions: [
        ElevatedButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        ElevatedButton(onPressed: _submitForm, child: const Text("OK"))
      ],
    );
  }

  Widget _buildRootLettersWidget(RootLetters rootLetters) =>
      FormBuilderTextField(
        name: "rootLetters",
        textDirection: TextDirection.rtl,
        style: _arabicRegularStyle,
        decoration: const InputDecoration(
            labelText: "Root Letters",
            labelStyle: _labelStyle,
            hintText: "Click to open keyboard"),
        readOnly: true,
        onTap: () {
          showDialog(
              context: context,
              builder: (BuildContext context) => LayoutBuilder(
                  builder: (_, constrains) => ArabicKeyboardDialog(
                      rootLetters: rootLetters,
                      width: constrains.minWidth * 0.6,
                      height: constrains.minHeight * 0.5,
                      onChanged: (rl) {
                        setState(() {
                          _formKey.currentState?.fields['rootLetters']
                              ?.didChange(rl.displayValue());
                          _rootLetters = rl;
                        });
                      })));
        },
      );

  get _buildNamedTemplateWidget => FormBuilderDropdown(
        name: "namedTemplate",
        items: _namedTemplateValues,
        decoration: const InputDecoration(
          labelText: "Family",
          labelStyle: _labelStyle,
        ),
      );

  get _buildTranslationWidget => FormBuilderTextField(
        name: "translation",
        decoration: const InputDecoration(
            labelText: "Translation", labelStyle: _labelStyle),
        textInputAction: TextInputAction.next,
      );

  Widget _buildVerbalNounsWidget(List<VerbalNoun> verbalNouns) =>
      FormBuilderField(
        name: "verbalNouns",
        builder: (field) {
          return InputDecorator(
              decoration: const InputDecoration(
                  labelText: "Verbal Nouns", labelStyle: _labelStyle),
              child: MultiSelectBottomSheetField(
                initialValue: verbalNouns,
                initialChildSize: 0.4,
                listType: MultiSelectListType.CHIP,
                itemsTextStyle: _arabicRegularStyle,
                searchTextStyle: _arabicRegularStyle,
                searchHintStyle: _arabicRegularStyle,
                items: VerbalNoun.values
                    .map((e) => MultiSelectItem<VerbalNoun?>(e, e.label))
                    .toList(),
                onConfirm: (List<VerbalNoun?> values) => _verbalNouns = values,
              ));
        },
      );

  get _buildSkipRuleProcessingWidget => FormBuilderSwitch(
        name: "skipRuleProcessing",
        title: const Text(
          "Skip Rule Processing",
          style: _labelStyle,
        ),
      );

  get _buildRemovePassiveLineWidget => FormBuilderSwitch(
        name: "removePassiveLine",
        title: const Text(
          "Remove Passive Line",
          style: _labelStyle,
        ),
      );

  void _submitForm() {
    var template = context.read<ConjugationTemplate>();
    var namedTemplate =
        _formKey.currentState?.fields['namedTemplate']?.value as NamedTemplate;
    var translation =
        _formKey.currentState?.fields['translation']?.value as String;
    var skipRuleProcessing =
        _formKey.currentState?.fields['skipRuleProcessing']?.value as bool;
    var removePassiveLine =
        _formKey.currentState?.fields['removePassiveLine']?.value as bool;

    var current = template.currentConjugationInput;
    var rootLetters = _rootLetters ?? current.rootLetters;
    var verbalNouns = _verbalNouns?.nonNulls.toList() ?? current.verbalNouns;
    var conjugationConfiguration = current.conjugationConfiguration;

    var updated = current.copy(
        id: current.id,
        index: current.index,
        checked: current.checked,
        conjugationConfiguration: conjugationConfiguration.copy(
            removePassiveLine: removePassiveLine,
            skipRuleProcessing: skipRuleProcessing),
        namedTemplate: namedTemplate,
        rootLetters: rootLetters,
        translation: translation,
        verbalNouns: verbalNouns);
    template.addOrUpdate(updated);
    Navigator.pop(context, 'OK');
  }
}
