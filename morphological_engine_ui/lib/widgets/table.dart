import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import '../models/conjugation_input.dart';
import '../models/conjugation_template.dart';
import 'conjugation_input_dialog.dart';

class MorphologicalEngineTableView extends StatefulWidget {
  const MorphologicalEngineTableView({super.key});

  @override
  State<MorphologicalEngineTableView> createState() =>
      _MorphologicalEngineTableViewState();
}

class _MorphologicalEngineTableViewState
    extends State<MorphologicalEngineTableView> {
  final _arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);
  final _headerStyle =
      GoogleFonts.robotoMono(fontWeight: FontWeight.bold, fontSize: 16);

  late final editDialog = const ConjugationInputDialog();

  List<DataColumn> _createColumns() {
    return [
      DataColumn(
          label: Text('Family', style: _headerStyle),
          tooltip: "Morphological family"),
      DataColumn(
          label: Text('Root Letters', style: _headerStyle),
          tooltip: "Root letters"),
      DataColumn(
          label: Text('Translation', style: _headerStyle),
          tooltip: "Translation"),
      DataColumn(
          label: Text('Verbal Nouns', style: _headerStyle),
          tooltip: "Verbal nouns"),
      DataColumn(
          label: Text('', style: _headerStyle), tooltip: "View dictionary"),
      DataColumn(label: Text('', style: _headerStyle), tooltip: "Edit row")
    ];
  }

  DataRow _buildRow(ConjugationInput row, BuildContext context) {
    return DataRow(
        cells: [
          DataCell(Center(
              child: Text(
            row.namedTemplate.displayValue(),
            textDirection: TextDirection.rtl,
            style: _arabicRegularStyle,
          ))),
          DataCell(Center(
            child: Text(
              row.rootLetters.displayValue(),
              textDirection: TextDirection.rtl,
              style: _arabicRegularStyle,
            ),
          )),
          DataCell(Center(child: Text(row.translation))),
          DataCell(Center(
              child: Text(
            row.displayVerbalNouns(),
            overflow: TextOverflow.ellipsis,
            textDirection: TextDirection.rtl,
            style: _arabicRegularStyle,
          ))),
          DataCell(Center(
            child: IconButton(
                onPressed: () {
                  print(">>>>>>");
                },
                icon: const Icon(Icons.info)),
          )),
          DataCell(const Center(child: Text('')), showEditIcon: true,
              onTap: () {
            var template = context.read<ConjugationTemplate>();
            template.selectedIndex = row.index;
            showDialog(
                context: context,
                builder: (BuildContext context) => editDialog);
          })
        ],
        selected: row.checked,
        onSelectChanged: (bool? selected) {
          var template = context.read<ConjugationTemplate>();
          template.selectedIndex = row.index;
          var updatedRow = row.copy(checked: selected!);
          template.addOrUpdate(updatedRow);
        });
  }

  List<DataRow> _createRows(
          BuildContext context, List<ConjugationInput> inputs) =>
      inputs.map((row) => _buildRow(row, context)).toList();

  @override
  Widget build(BuildContext context) {
    var template = context.watch<ConjugationTemplate>();
    return Scaffold(
      body: SingleChildScrollView(
          child: DataTable(
        columns: _createColumns(),
        rows: _createRows(context, template.inputs),
        dividerThickness: 1,
        dataRowMaxHeight: 80,
        showBottomBorder: true,
        showCheckboxColumn: true,
        headingTextStyle: const TextStyle(
            fontWeight: FontWeight.bold, color: Colors.lightGreen),
      )),
    );
  }
}
