import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'conjugation_input_dialog.dart';
import '../models/model.dart';

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
  late final layoutBuilder = LayoutBuilder(builder: (_, constrains) {
    return ConjugationInputDialog(
        width: constrains.maxWidth * 0.8, height: constrains.maxHeight * 0.8);
  });

  List<DataColumn> _createColumns() {
    return [
      DataColumn(label: Text('Family', style: _headerStyle)),
      DataColumn(label: Text('Root Letters', style: _headerStyle)),
      DataColumn(label: Text('Translation', style: _headerStyle)),
      DataColumn(label: Text('Verbal Nouns', style: _headerStyle)),
      DataColumn(label: Text('', style: _headerStyle))
    ];
  }

  DataRow _buildRow(int index, ConjugationInput row, BuildContext context) {
    return DataRow(
        cells: [
          DataCell(SizedBox(
              width: 100,
              child: Center(
                  child: Text(
                row.namedTemplate.displayValue(),
                textDirection: TextDirection.rtl,
                style: _arabicRegularStyle,
              )))),
          DataCell(SizedBox(
              width: 100,
              child: Center(
                child: Text(
                  row.rootLetters.displayValue(),
                  textDirection: TextDirection.rtl,
                  style: _arabicRegularStyle,
                ),
              ))),
          DataCell(SizedBox(
              width: 100, child: Center(child: Text(row.translation)))),
          DataCell(SizedBox(
              width: 150,
              child: Center(
                  child: Text(
                row.displayVerbalNouns(),
                overflow: TextOverflow.ellipsis,
                textDirection: TextDirection.rtl,
                style: _arabicRegularStyle,
              )))),
          DataCell(const SizedBox(width: 5, child: Text('')),
              showEditIcon: true, onTap: () {
            var input = context.read<ConjugationInput>();
            input.updateOnly(row);
            showDialog(
                context: context,
                builder: (BuildContext context) => layoutBuilder);
          })
        ],
        selected: row.checked,
        onSelectChanged: (bool? selected) {
          var template = context.read<ConjugationTemplate>();
          var updatedRow = row.copy(checked: selected!);
          template.addOrUpdate(updatedRow);
        });
  }

  List<DataRow> _createRows(
          BuildContext context, List<ConjugationInput> inputs) =>
      inputs
          .asMap()
          .map((index, row) => MapEntry(index, _buildRow(index, row, context)))
          .values
          .toList();

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
            fontWeight: FontWeight.bold, color: Colors.lightBlue),
      )),
    );
  }
}
