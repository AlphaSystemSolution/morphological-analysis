// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'conjugation_entry_dialog.dart';
import '../models/model.dart';

class MorphologicalEngineTableView extends StatefulWidget {
  MorphologicalEngineTableView(
      {super.key, required this.entries, required this.onChanged});

  List<ConjugationEntry> entries;
  final ValueChanged<List<ConjugationEntry>> onChanged;

  @override
  State<MorphologicalEngineTableView> createState() =>
      _MorphologicalEngineTableViewState();
}

class _MorphologicalEngineTableViewState
    extends State<MorphologicalEngineTableView> {
  List<ConjugationEntry> _items = [];
  final arabicRegularStyle = GoogleFonts.scheherazadeNew(fontSize: 20);

  final headerStyle =
      GoogleFonts.robotoMono(fontWeight: FontWeight.bold, fontSize: 16);

  @override
  void initState() {
    super.initState();
    setState(() {
      _items = widget.entries;
    });
  }

  List<DataColumn> _createColumns() {
    return [
      DataColumn(label: Text('Family', style: headerStyle)),
      DataColumn(label: Text('Root Letters', style: headerStyle)),
      DataColumn(label: Text('Translation', style: headerStyle)),
      DataColumn(label: Text('', style: headerStyle))
    ];
  }

  void showEditDialog(int index, ConjugationEntry entry) {
    showDialog(
        context: context,
        builder: (BuildContext context) => LayoutBuilder(
            builder: (_, constrains) => ConjugationEntryDialog(
                entry: entry,
                width: constrains.minHeight * 0.6,
                height: constrains.minHeight * 0.4,
                onChanged: (value) => {
                      setState(() {
                        _items[index] = value;
                      })
                    })));
  }

  DataRow _buildRow(int index, ConjugationEntry row) {
    return DataRow(
        cells: [
          DataCell(SizedBox(
              width: 100,
              child: Center(
                  child: Text(
                row.family.displayValue(),
                textDirection: TextDirection.rtl,
                style: arabicRegularStyle,
              )))),
          DataCell(Center(
              child: Text(
            row.rootLetters.displayValue(),
            textDirection: TextDirection.rtl,
            style: arabicRegularStyle,
          ))),
          DataCell(Center(child: Text(row.translation))),
          DataCell(const Text(''), showEditIcon: true, onTap: () {
            showEditDialog(index, row);
          })
        ],
        selected: row.checked,
        onSelectChanged: (bool? selected) => {
              setState(
                () {
                  print("selected: $index, ${row.id}, $selected");
                  var item = _items[index];
                  _items[index] = ConjugationEntry(
                      id: item.id,
                      family: item.family,
                      rootLetters: item.rootLetters,
                      checked: selected!);

                  print("${_items[index].id} ${_items[index].checked}");
                },
              )
            });
  }

  List<DataRow> _createRows() => _items
      .asMap()
      .map((index, row) => MapEntry(index, _buildRow(index, row)))
      .values
      .toList();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: DataTable(
        columns: _createColumns(),
        rows: _createRows(),
        dividerThickness: 5,
        dataRowMaxHeight: 80,
        showBottomBorder: true,
        showCheckboxColumn: true,
        headingTextStyle: const TextStyle(
            fontWeight: FontWeight.bold, color: Colors.lightBlue),
      ),
    );
  }
}
