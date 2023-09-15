import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';
import 'package:form_builder_validators/form_builder_validators.dart';
import 'package:morphological_engine_ui/models/chart_configuration.dart';
import 'package:morphological_engine_ui/models/model.dart';
import 'package:provider/provider.dart';

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

  final _formKey = GlobalKey<FormBuilderState>();
  PageOrientation? _pageOrientation;
  SortDirection? _sortDirection;
  DocumentFormat? _documentFormat;
  String? _arabicFontFamily;
  String? _translationFontFamily;
  int? _arabicFontSize;
  bool _arabicFontSizeHasError = false;
  int? _translationFontSize;
  bool _translationFontSizeHasError = false;
  int? _headingFontSize;
  bool _headingFontSizeHasError = false;
  bool? _showToc;
  bool? _showTitle;
  bool? _showLabels;
  bool? _removeAdverbs;
  bool? _showAbbreviatedConjugation;
  bool? _showDetailedConjugation;
  bool? _showMorphologicalTermCaptionInAbbreviatedConjugation;
  bool? _showMorphologicalTermCaptionInDetailConjugation;

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      scrollable: true,
      title: const Text("Chart Configuration", textAlign: TextAlign.center),
      content: _buildForm,
      actions: <Widget>[
        ElevatedButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        ElevatedButton(
            onPressed: _hasError ? null : _submitForm, child: const Text("OK"))
      ],
    );
  }

  Widget get _buildForm => Center(
        child: FormBuilder(
          key: _formKey,
          child: Selector<ConjugationTemplate, ChartConfiguration>(
              selector: (_, template) => template.chartConfiguration,
              builder: (context, chartConfiguration, child) {
                return Column(
                  children: [
                    _buildPageOrientationWidget(
                        chartConfiguration.pageOrientation),
                    _buildSortDirectionWidget(chartConfiguration.sortDirection),
                    _buildDocumentFormatWidget(chartConfiguration.format),
                    _buildArabicFontFamilyWidget(
                        chartConfiguration.arabicFontFamily),
                    _buildTranslationFontFamilyWidget(
                        chartConfiguration.translationFontFamily),
                    _buildArabicFontSize(chartConfiguration.arabicFontSize),
                    _buildTranslationFontSize(
                        chartConfiguration.translationFontSize),
                    _buildHeadingFontSize(chartConfiguration.headingFontSize),
                    _buildShowToc(chartConfiguration.showToc),
                    _buildShowTitle(chartConfiguration.showTitle),
                    _buildShowLabels(chartConfiguration.showLabels),
                    _buildRemoveAdverbs(chartConfiguration.removeAdverbs),
                    _buildShowAbbreviatedConjugation(
                        chartConfiguration.showAbbreviatedConjugation),
                    _buildShowDetailedConjugation(
                        chartConfiguration.showDetailedConjugation),
                    _buildShowMorphologicalTermCaptionInAbbreviatedConjugation(
                        chartConfiguration
                            .showMorphologicalTermCaptionInAbbreviatedConjugation),
                    _buildShowMorphologicalTermCaptionInDetailConjugation(
                        chartConfiguration
                            .showMorphologicalTermCaptionInDetailConjugation)
                  ],
                );
              }),
        ),
      );

  FormBuilderDropdown<PageOrientation> _buildPageOrientationWidget(
          PageOrientation pageOrientation) =>
      FormBuilderDropdown<PageOrientation>(
        name: "PageOrientation",
        initialValue: pageOrientation,
        decoration: const InputDecoration(
            labelText: "Page Orientation", hintText: "Select page orientation"),
        items: PageOrientation.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
        onChanged: (value) => setState(
            () => _pageOrientation = value ?? PageOrientation.Portrait),
      );

  FormBuilderDropdown<SortDirection> _buildSortDirectionWidget(
          SortDirection sortDirection) =>
      FormBuilderDropdown<SortDirection>(
        name: "SortDirection",
        initialValue: sortDirection,
        decoration: const InputDecoration(
            labelText: "Sort Direction", hintText: "Select sort direction"),
        items: SortDirection.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
        onChanged: (value) =>
            setState(() => _sortDirection = value ?? SortDirection.Ascending),
      );

  FormBuilderDropdown<DocumentFormat> _buildDocumentFormatWidget(
          DocumentFormat documentFormat) =>
      FormBuilderDropdown<DocumentFormat>(
        name: "DocumentFormat",
        initialValue: documentFormat,
        decoration: const InputDecoration(
            labelText: "Document format", hintText: "Select document format"),
        items: DocumentFormat.values
            .map((e) => DropdownMenuItem(
                  alignment: AlignmentDirectional.centerStart,
                  value: e,
                  child: Text(e.name),
                ))
            .toList(),
        onChanged: (value) =>
            setState(() => _documentFormat = value ?? DocumentFormat.Classic),
      );

  FormBuilderDropdown<String> _buildArabicFontFamilyWidget(
          String arabicFontFamily) =>
      FormBuilderDropdown<String>(
        name: "arabicFontFamily",
        initialValue: arabicFontFamily,
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
        onChanged: (value) =>
            setState(() => _arabicFontFamily = value ?? _arabicFontFamilies[0]),
      );

  FormBuilderDropdown<String> _buildTranslationFontFamilyWidget(
          String translationFontFamily) =>
      FormBuilderDropdown<String>(
        name: "translationFontFamily",
        initialValue: translationFontFamily,
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
        onChanged: (value) => setState(
            () => _translationFontFamily = value ?? _englishFontFamilies[0]),
      );

  FormBuilderTextField _buildArabicFontSize(int initialValue) =>
      FormBuilderTextField(
        name: "arabicFontSize",
        decoration: InputDecoration(
          labelText: 'Arabic Font Size',
          suffixIcon: _arabicFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        initialValue: initialValue.toString(),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        onChanged: (value) {
          setState(() {
            _arabicFontSizeHasError =
                !(_formKey.currentState?.fields['arabicFontSize']?.validate() ??
                    false);
            if (!_arabicFontSizeHasError) {
              _arabicFontSize = int.parse(value!);
            }
          });
        },
      );

  FormBuilderTextField _buildTranslationFontSize(int initialValue) =>
      FormBuilderTextField(
        name: "translationFontSize",
        decoration: InputDecoration(
          labelText: 'Translation Font Size',
          suffixIcon: _translationFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        initialValue: initialValue.toString(),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        onChanged: (value) {
          setState(() {
            _translationFontSizeHasError = !(_formKey
                    .currentState?.fields['translationFontSize']
                    ?.validate() ??
                false);
            if (!_translationFontSizeHasError) {
              _translationFontSize = int.parse(value!);
            }
          });
        },
      );

  FormBuilderTextField _buildHeadingFontSize(int initialValue) =>
      FormBuilderTextField(
        name: "headingFontSize",
        decoration: InputDecoration(
          labelText: 'Heading Font Size',
          suffixIcon: _headingFontSizeHasError
              ? const Icon(Icons.error, color: Colors.red)
              : const Icon(Icons.check, color: Colors.green),
        ),
        initialValue: initialValue.toString(),
        keyboardType: TextInputType.number,
        textInputAction: TextInputAction.next,
        validator: FormBuilderValidators.compose([
          FormBuilderValidators.required(),
          FormBuilderValidators.integer(),
          FormBuilderValidators.min(8),
          FormBuilderValidators.max(72),
        ]),
        onChanged: (value) {
          setState(() {
            _headingFontSizeHasError = !(_formKey
                    .currentState?.fields['headingFontSize']
                    ?.validate() ??
                false);
            if (!_headingFontSizeHasError) {
              _headingFontSize = int.parse(value!);
            }
          });
        },
      );

  FormBuilderSwitch _buildShowToc(bool initialValue) => FormBuilderSwitch(
        name: "showToc",
        title: const Text("Show Toc"),
        initialValue: initialValue,
        onChanged: (value) => setState(() => _showToc = value!),
      );

  FormBuilderSwitch _buildShowTitle(bool initialValue) => FormBuilderSwitch(
        name: "showTitle",
        title: const Text("Show Title"),
        initialValue: initialValue,
        onChanged: (value) => setState(() => _showTitle = value!),
      );

  FormBuilderSwitch _buildShowLabels(bool initialValue) => FormBuilderSwitch(
        name: "showLabels",
        title: const Text("Show Labels"),
        initialValue: initialValue,
        onChanged: (value) => setState(() => _showLabels = value!),
      );

  FormBuilderSwitch _buildRemoveAdverbs(bool initialValue) => FormBuilderSwitch(
        name: "removeAdverbs",
        title: const Text("Remove Adverbs"),
        initialValue: initialValue,
        onChanged: (value) => setState(() => _removeAdverbs = value!),
      );

  FormBuilderSwitch _buildShowAbbreviatedConjugation(bool initialValue) =>
      FormBuilderSwitch(
        name: "showAbbreviatedConjugation",
        title: const Text("Show Abbreviated Conjugation"),
        initialValue: initialValue,
        onChanged: (value) =>
            setState(() => _showAbbreviatedConjugation = value!),
      );

  FormBuilderSwitch _buildShowDetailedConjugation(bool initialValue) =>
      FormBuilderSwitch(
        name: "showDetailedConjugation",
        title: const Text("Show Detailed Conjugation"),
        initialValue: initialValue,
        onChanged: (value) => setState(() => _showDetailedConjugation = value!),
      );

  FormBuilderSwitch _buildShowMorphologicalTermCaptionInAbbreviatedConjugation(
          bool initialValue) =>
      FormBuilderSwitch(
        name: "showMorphologicalTermCaptionInAbbreviatedConjugation",
        title: const Text(
            "Show Morphological Term Caption In Abbreviated Conjugation"),
        initialValue: initialValue,
        onChanged: (value) => setState(() =>
            _showMorphologicalTermCaptionInAbbreviatedConjugation = value!),
      );

  FormBuilderSwitch _buildShowMorphologicalTermCaptionInDetailConjugation(
          bool initialValue) =>
      FormBuilderSwitch(
        name: "showMorphologicalTermCaptionInDetailConjugation",
        title:
            const Text("Show Morphological Term Caption In DetailC onjugation"),
        initialValue: initialValue,
        onChanged: (value) => setState(
            () => _showMorphologicalTermCaptionInDetailConjugation = value!),
      );

  get _hasError =>
      _arabicFontSizeHasError ||
      _translationFontSizeHasError ||
      _headingFontSizeHasError;

  void _submitForm() {
    var template = context.read<ConjugationTemplate>();
    var chartConfiguration = template.chartConfiguration.copy(
        pageOrientation: _pageOrientation,
        sortDirection: _sortDirection,
        format: _documentFormat,
        arabicFontFamily: _arabicFontFamily,
        translationFontFamily: _translationFontFamily,
        arabicFontSize: _arabicFontSize,
        translationFontSize: _translationFontSize,
        headingFontSize: _headingFontSize,
        showToc: _showToc,
        showTitle: _showTitle,
        showLabels: _showLabels,
        removeAdverbs: _removeAdverbs,
        showAbbreviatedConjugation: _showAbbreviatedConjugation,
        showDetailedConjugation: _showDetailedConjugation,
        showMorphologicalTermCaptionInAbbreviatedConjugation:
            _showMorphologicalTermCaptionInAbbreviatedConjugation,
        showMorphologicalTermCaptionInDetailConjugation:
            _showMorphologicalTermCaptionInDetailConjugation);

    template.chartConfiguration = chartConfiguration;
    Navigator.pop(context, 'OK');
  }
}
