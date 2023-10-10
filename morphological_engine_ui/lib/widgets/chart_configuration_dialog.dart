import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:form_builder_validators/form_builder_validators.dart';
import 'package:morphological_engine_ui/models/chart_configuration.dart';
import 'package:provider/provider.dart';

import '../models/conjugation_template.dart';

class ChartConfigurationDialog extends StatefulWidget {
  const ChartConfigurationDialog({super.key});

  @override
  State<ChartConfigurationDialog> createState() =>
      _ChartConfigurationDialogState();
}

class _ChartConfigurationDialogState extends State<ChartConfigurationDialog> {
  static final List<String> _arabicFontFamilies = [
    "KFGQPC Uthman Taha Naskh",
    "Arabic Typesetting",
    "Traditional Arabic"
  ];

  static final List<String> _englishFontFamilies = [
    "Candara",
    "Times New Roman",
    "Georgia",
    "Arial"
  ];

  static const _pageOrientationFieldName = "pageOrientation";
  static const _sortDirectionFieldName = "sortDirection";
  static const _documentFormatFieldName = "documentFormat";
  static const _arabicFontFamilyFieldName = "arabicFontFamily";
  static const _translationFontFamilyFieldName = "translationFontFamily";
  static const _arabicFontSizeFieldName = "arabicFontSize";
  static const _translationFontSizeFieldName = "translationFontSize";
  static const _headingFontSizeFieldName = "headingFontSize";
  static const _showTocFieldName = "showToc";
  static const _showTitleFieldName = "showTitle";
  static const _showLabelsFieldName = "showLabels";
  static const _removeAdverbsFieldName = "removeAdverbs";
  static const _showAbbreviatedConjugationFieldName =
      "showAbbreviatedConjugation";
  static const _showDetailedConjugationFieldName = "showDetailedConjugation";
  static const _showMorphologicalTermCaptionInAbbreviatedConjugationFieldName =
      "showMorphologicalTermCaptionInAbbreviatedConjugation";
  static const _showMorphologicalTermCaptionInDetailConjugationFieldName =
      "showMorphologicalTermCaptionInDetailConjugation";

  final _formKey = GlobalKey<FormBuilderState>();
  bool _arabicFontSizeHasError = false;
  bool _translationFontSizeHasError = false;
  bool _headingFontSizeHasError = false;

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      scrollable: true,
      title: const Text("Chart Configuration", textAlign: TextAlign.center),
      content: Selector<ConjugationTemplate, ChartConfiguration>(
          selector: (_, template) => template.chartConfiguration,
          builder: (context, chartConfiguration, child) => FormBuilder(
                key: _formKey,
                initialValue: {
                  _pageOrientationFieldName: chartConfiguration.pageOrientation,
                  _sortDirectionFieldName: chartConfiguration.sortDirection,
                  _documentFormatFieldName: chartConfiguration.format,
                  _arabicFontFamilyFieldName:
                      chartConfiguration.arabicFontFamily,
                  _translationFontFamilyFieldName:
                      chartConfiguration.translationFontFamily,
                  _arabicFontSizeFieldName:
                      chartConfiguration.arabicFontSize.toString(),
                  _translationFontSizeFieldName:
                      chartConfiguration.translationFontSize.toString(),
                  _headingFontSizeFieldName:
                      chartConfiguration.headingFontSize.toString(),
                  _showTocFieldName: chartConfiguration.showToc,
                  _showTitleFieldName: chartConfiguration.showTitle,
                  _showLabelsFieldName: chartConfiguration.showLabels,
                  _removeAdverbsFieldName: chartConfiguration.removeAdverbs,
                  _showAbbreviatedConjugationFieldName:
                      chartConfiguration.showAbbreviatedConjugation,
                  _showDetailedConjugationFieldName:
                      chartConfiguration.showDetailedConjugation,
                  _showMorphologicalTermCaptionInAbbreviatedConjugationFieldName:
                      chartConfiguration
                          .showMorphologicalTermCaptionInAbbreviatedConjugation,
                  _showMorphologicalTermCaptionInDetailConjugationFieldName:
                      chartConfiguration
                          .showMorphologicalTermCaptionInDetailConjugation
                },
                child: Column(
                  children: [
                    _buildPageOrientationWidget,
                    _buildSortDirectionWidget,
                    _buildDocumentFormatWidget,
                    _buildArabicFontFamilyWidget,
                    _buildTranslationFontFamilyWidget,
                    _buildArabicFontSize,
                    _buildTranslationFontSize,
                    _buildHeadingFontSize,
                    _buildShowToc,
                    _buildShowTitle,
                    _buildShowLabels,
                    _buildRemoveAdverbs,
                    _buildShowAbbreviatedConjugation,
                    _buildShowDetailedConjugation,
                    _buildShowMorphologicalTermCaptionInAbbreviatedConjugation,
                    _buildShowMorphologicalTermCaptionInDetailConjugation
                  ],
                ),
              )),
      actions: <Widget>[
        ElevatedButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        ElevatedButton(
            onPressed: _hasError ? null : _submitForm, child: const Text("OK"))
      ],
    );
  }

  get _buildPageOrientationWidget => FormBuilderDropdown<PageOrientation>(
        name: _pageOrientationFieldName,
        decoration: const InputDecoration(
            labelText: "Page Orientation", hintText: "Select page orientation"),
        items: PageOrientation.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
      );

  get _buildSortDirectionWidget => FormBuilderDropdown<SortDirection>(
        name: _sortDirectionFieldName,
        decoration: const InputDecoration(
            labelText: "Sort Direction", hintText: "Select sort direction"),
        items: SortDirection.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
      );

  get _buildDocumentFormatWidget => FormBuilderDropdown<DocumentFormat>(
        name: _documentFormatFieldName,
        decoration: const InputDecoration(
            labelText: "Document format", hintText: "Select document format"),
        items: DocumentFormat.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
      );

  get _buildArabicFontFamilyWidget => FormBuilderDropdown<String>(
        name: _arabicFontFamilyFieldName,
        decoration: const InputDecoration(
            labelText: "Arabic Font Family",
            hintText: "Select arabic font family"),
        items: _arabicFontFamilies
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e),
                ))
            .toList(),
      );

  get _buildTranslationFontFamilyWidget => FormBuilderDropdown<String>(
        name: _translationFontFamilyFieldName,
        decoration: const InputDecoration(
            labelText: "Translation Font Family",
            hintText: "Select translation font family"),
        items: _englishFontFamilies
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e),
                ))
            .toList(),
      );

  get _buildArabicFontSize => FormBuilderTextField(
        name: _arabicFontSizeFieldName,
        decoration: InputDecoration(
          labelText: 'Arabic Font Size',
          suffixIcon: _arabicFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        valueTransformer: (value) => int.parse(value!),
        onChanged: (value) {
          setState(() {
            _arabicFontSizeHasError = !(_formKey
                    .currentState?.fields[_arabicFontSizeFieldName]
                    ?.validate() ??
                false);
          });
        },
      );

  get _buildTranslationFontSize => FormBuilderTextField(
        name: _translationFontSizeFieldName,
        decoration: InputDecoration(
          labelText: 'Translation Font Size',
          suffixIcon: _translationFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        valueTransformer: (value) => int.parse(value!),
        onChanged: (value) {
          setState(() {
            _translationFontSizeHasError = !(_formKey
                    .currentState?.fields[_translationFontSizeFieldName]
                    ?.validate() ??
                false);
          });
        },
      );

  get _buildHeadingFontSize => FormBuilderTextField(
        name: _headingFontSizeFieldName,
        decoration: InputDecoration(
          labelText: 'Heading Font Size',
          suffixIcon: _headingFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        valueTransformer: (value) => int.parse(value!),
        onChanged: (value) {
          setState(() {
            _headingFontSizeHasError = !(_formKey
                    .currentState?.fields[_headingFontSizeFieldName]
                    ?.validate() ??
                false);
          });
        },
      );

  get _buildShowToc =>
      FormBuilderSwitch(name: _showTocFieldName, title: const Text("Show Toc"));

  get _buildShowTitle => FormBuilderSwitch(
      name: _showTitleFieldName, title: const Text("Show Title"));

  get _buildShowLabels => FormBuilderSwitch(
      name: _showLabelsFieldName, title: const Text("Show Labels"));

  get _buildRemoveAdverbs => FormBuilderSwitch(
        name: _removeAdverbsFieldName,
        title: const Text("Remove Adverbs"),
      );

  get _buildShowAbbreviatedConjugation => FormBuilderSwitch(
        name: _showAbbreviatedConjugationFieldName,
        title: const Text("Show Abbreviated Conjugation"),
      );

  get _buildShowDetailedConjugation => FormBuilderSwitch(
        name: _showDetailedConjugationFieldName,
        title: const Text("Show Detailed Conjugation"),
      );

  get _buildShowMorphologicalTermCaptionInAbbreviatedConjugation =>
      FormBuilderSwitch(
        name: _showMorphologicalTermCaptionInAbbreviatedConjugationFieldName,
        title: const Text(
            "Show Morphological Term Caption In Abbreviated Conjugation"),
      );

  get _buildShowMorphologicalTermCaptionInDetailConjugation =>
      FormBuilderSwitch(
        name: _showMorphologicalTermCaptionInDetailConjugationFieldName,
        title:
            const Text("Show Morphological Term Caption In DetailC onjugation"),
      );

  get _hasError =>
      _arabicFontSizeHasError ||
      _translationFontSizeHasError ||
      _headingFontSizeHasError;

  void _submitForm() {
    var template = context.read<ConjugationTemplate>();
    var currentState = _formKey.currentState;
    var chartConfiguration = template.chartConfiguration.copy(
        pageOrientation: currentState?.fields[_pageOrientationFieldName]?.value
            as PageOrientation,
        sortDirection: currentState?.fields[_sortDirectionFieldName]?.value
            as SortDirection,
        format: currentState?.fields[_documentFormatFieldName]?.value
            as DocumentFormat,
        arabicFontFamily:
            currentState?.fields[_arabicFontFamilyFieldName]?.value as String,
        translationFontFamily: currentState
            ?.fields[_translationFontFamilyFieldName]?.value as String,
        arabicFontSize: int.parse(
            currentState?.fields[_arabicFontSizeFieldName]?.value as String),
        translationFontSize: int.parse(currentState
            ?.fields[_translationFontSizeFieldName]?.value as String),
        headingFontSize: int.parse(
            currentState?.fields[_headingFontSizeFieldName]?.value as String),
        showToc: currentState?.fields[_showTocFieldName]?.value as bool,
        showTitle: currentState?.fields[_showTitleFieldName]?.value as bool,
        showLabels: currentState?.fields[_showLabelsFieldName]?.value as bool,
        removeAdverbs: currentState?.fields[_removeAdverbsFieldName]?.value as bool,
        showAbbreviatedConjugation: currentState?.fields[_showAbbreviatedConjugationFieldName]?.value as bool,
        showDetailedConjugation: currentState?.fields[_showDetailedConjugationFieldName]?.value as bool,
        showMorphologicalTermCaptionInAbbreviatedConjugation: currentState?.fields[_showMorphologicalTermCaptionInAbbreviatedConjugationFieldName]?.value as bool,
        showMorphologicalTermCaptionInDetailConjugation: currentState?.fields[_showMorphologicalTermCaptionInDetailConjugationFieldName]?.value as bool);

    template.chartConfiguration = chartConfiguration;
    Navigator.pop(context, 'OK');
  }
}
