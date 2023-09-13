// ignore_for_file: constant_identifier_names

import 'package:quiver/core.dart';

class ChartConfiguration {
  final PageOrientation pageOrientation;
  final SortDirection sortDirection;
  final DocumentFormat format;
  final String arabicFontFamily;
  final String translationFontFamily;
  final int arabicFontSize;
  final int translationFontSize;
  final int headingFontSize;
  final bool showToc;
  final bool showTitle;
  final bool showLabels;
  final bool removeAdverbs;
  final bool showAbbreviatedConjugation;
  final bool showDetailedConjugation;
  final bool showMorphologicalTermCaptionInAbbreviatedConjugation;
  final bool showMorphologicalTermCaptionInDetailConjugation;

  const ChartConfiguration(
      {this.pageOrientation = PageOrientation.Portrait,
      this.sortDirection = SortDirection.Ascending,
      this.format = DocumentFormat.Classic,
      this.arabicFontFamily = "KFGQPC Uthman Taha Naskh",
      this.translationFontFamily = "Candara",
      this.arabicFontSize = 12,
      this.translationFontSize = 10,
      this.headingFontSize = 16,
      this.showToc = true,
      this.showTitle = true,
      this.showLabels = true,
      this.removeAdverbs = false,
      this.showAbbreviatedConjugation = true,
      this.showDetailedConjugation = true,
      this.showMorphologicalTermCaptionInAbbreviatedConjugation = true,
      this.showMorphologicalTermCaptionInDetailConjugation = true});

  @override
  int get hashCode => hashObjects([
        pageOrientation,
        sortDirection,
        format,
        arabicFontFamily,
        translationFontFamily,
        arabicFontSize,
        translationFontSize,
        headingFontSize,
        showToc,
        showTitle,
        showLabels,
        removeAdverbs,
        showAbbreviatedConjugation,
        showDetailedConjugation,
        showMorphologicalTermCaptionInAbbreviatedConjugation,
        showMorphologicalTermCaptionInDetailConjugation
      ]);

  @override
  bool operator ==(Object other) =>
      other is ChartConfiguration &&
      pageOrientation == other.pageOrientation &&
      sortDirection == other.sortDirection &&
      format == other.format &&
      arabicFontFamily == other.arabicFontFamily &&
      translationFontFamily == other.translationFontFamily &&
      arabicFontSize == other.arabicFontSize &&
      translationFontSize == other.translationFontSize &&
      headingFontSize == other.headingFontSize &&
      showToc == other.showToc &&
      showTitle == other.showTitle &&
      showLabels == other.showLabels &&
      removeAdverbs == other.removeAdverbs &&
      showAbbreviatedConjugation == other.showAbbreviatedConjugation &&
      showDetailedConjugation == other.showDetailedConjugation &&
      showMorphologicalTermCaptionInAbbreviatedConjugation ==
          other.showMorphologicalTermCaptionInAbbreviatedConjugation &&
      showMorphologicalTermCaptionInDetailConjugation ==
          other.showMorphologicalTermCaptionInDetailConjugation;

  ChartConfiguration copy(
          PageOrientation? pageOrientation,
          SortDirection? sortDirection,
          DocumentFormat? format,
          String? arabicFontFamily,
          String? translationFontFamily,
          int? arabicFontSize,
          int? translationFontSize,
          int? headingFontSize,
          bool? showToc,
          bool? showTitle,
          bool? showLabels,
          bool? removeAdverbs,
          bool? showAbbreviatedConjugation,
          bool? showDetailedConjugation,
          bool? showMorphologicalTermCaptionInAbbreviatedConjugation,
          bool? showMorphologicalTermCaptionInDetailConjugation) =>
      ChartConfiguration(
          pageOrientation: pageOrientation ?? this.pageOrientation,
          sortDirection: sortDirection ?? this.sortDirection,
          format: format ?? this.format,
          arabicFontFamily: arabicFontFamily ?? this.arabicFontFamily,
          translationFontFamily:
              translationFontFamily ?? this.translationFontFamily,
          arabicFontSize: arabicFontSize ?? this.arabicFontSize,
          translationFontSize: translationFontSize ?? this.translationFontSize,
          headingFontSize: headingFontSize ?? this.headingFontSize,
          showToc: showToc ?? this.showToc,
          showTitle: showTitle ?? this.showTitle,
          showLabels: showLabels ?? this.showLabels,
          removeAdverbs: removeAdverbs ?? this.removeAdverbs,
          showAbbreviatedConjugation:
              showAbbreviatedConjugation ?? this.showAbbreviatedConjugation,
          showDetailedConjugation:
              showDetailedConjugation ?? this.showDetailedConjugation,
          showMorphologicalTermCaptionInAbbreviatedConjugation:
              showMorphologicalTermCaptionInAbbreviatedConjugation ??
                  this.showMorphologicalTermCaptionInAbbreviatedConjugation,
          showMorphologicalTermCaptionInDetailConjugation:
              showMorphologicalTermCaptionInDetailConjugation ??
                  this.showMorphologicalTermCaptionInDetailConjugation);

  Map toJson() => {
        "pageOrientation": pageOrientation.name,
        "sortDirection": sortDirection.name,
        "format": format.name,
        "arabicFontFamily": arabicFontFamily,
        "translationFontFamily": translationFontFamily,
        "arabicFontSize": arabicFontSize,
        "translationFontSize": translationFontSize,
        "headingFontSize": headingFontSize,
        "showToc": showToc,
        "showTitle": showTitle,
        "showLabels": showLabels,
        "removeAdverbs": removeAdverbs,
        "showAbbreviatedConjugation": showAbbreviatedConjugation,
        "showDetailedConjugation": showDetailedConjugation,
        "showMorphologicalTermCaptionInAbbreviatedConjugation":
            showMorphologicalTermCaptionInAbbreviatedConjugation,
        "showMorphologicalTermCaptionInDetailConjugation":
            showMorphologicalTermCaptionInDetailConjugation
      };

  @override
  String toString() => """ChartConfiguration(
      pageOrientation: $pageOrientation,
      sortDirection: $sortDirection,
      format: $format,
      arabicFontFamily: $arabicFontFamily,
      translationFontFamily: $translationFontFamily,
      arabicFontSize: $arabicFontSize,
      translationFontSize: $translationFontSize,
      headingFontSize: $headingFontSize,
      showToc: $showToc,
      showTitle: $showTitle,
      showLabels: $showLabels,
      removeAdverbs: $removeAdverbs,
      showAbbreviatedConjugation: $showAbbreviatedConjugation,
      showDetailedConjugation: $showDetailedConjugation,
      showMorphologicalTermCaptionInAbbreviatedConjugation: $showMorphologicalTermCaptionInAbbreviatedConjugation,
          showMorphologicalTermCaptionInDetailConjugation: $showMorphologicalTermCaptionInDetailConjugation
    )""";

  factory ChartConfiguration.fromJson(Map<String, dynamic> data) =>
      ChartConfiguration(
          pageOrientation: PageOrientation.values
              .byName(data['pageOrientation'] ?? 'Portrait'),
          sortDirection:
              SortDirection.values.byName(data['sortDirection'] ?? 'Ascending'),
          format: DocumentFormat.values.byName(data['format'] ?? 'Classic'),
          arabicFontFamily:
              data['arabicFontFamily'] ?? "KFGQPC Uthman Taha Naskh",
          translationFontFamily: data['translationFontFamily'] ?? "Candara",
          arabicFontSize: data['arabicFontSize'] ?? 12,
          translationFontSize: data['translationFontSize'] ?? 10,
          headingFontSize: data['headingFontSize'] ?? 16,
          showToc: data['showToc'] ?? true,
          showTitle: data['showTitle'] ?? true,
          showLabels: data['showLabels'] ?? true,
          removeAdverbs: data['removeAdverbs'] ?? false,
          showAbbreviatedConjugation:
              data['showAbbreviatedConjugation'] ?? true,
          showDetailedConjugation: data['showDetailedConjugation'] ?? true,
          showMorphologicalTermCaptionInAbbreviatedConjugation: data[
                  'showMorphologicalTermCaptionInAbbreviatedConjugation'] ??
              true,
          showMorphologicalTermCaptionInDetailConjugation:
              data['showMorphologicalTermCaptionInDetailConjugation'] ?? true);
}

enum PageOrientation {
  Portrait,
  Landscape;
}

enum SortDirection {
  Ascending,
  Descending;
}

enum SortDirective {
  None,
  Type,
  Alphabetical;
}

enum DocumentFormat {
  Classic,
  AbbreviateConjugationSingleRow;
}
