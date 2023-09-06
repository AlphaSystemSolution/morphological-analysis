// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:morphological_engine_ui/models/arabic_letter.dart';
import 'package:morphological_engine_ui/models/model.dart';

class ArabicKeyboardDialog extends StatefulWidget {
  ArabicKeyboardDialog(
      {super.key,
      this.rootLetters = const RootLetters(),
      required this.width,
      required this.height,
      required this.onChanged});

  RootLetters rootLetters;
  double width;
  double height;
  final ValueChanged<RootLetters> onChanged;

  @override
  State<ArabicKeyboardDialog> createState() => _ArabicKeyboardDialogState();
}

class _ArabicKeyboardDialogState extends State<ArabicKeyboardDialog> {
  int _selectedIndex = 0;
  RootLetters _rootLetters = const RootLetters();
  final List<bool> _selectedToggles = [true, false, false, false];
  final keyboradButtonStyle = GoogleFonts.scheherazadeNew(fontSize: 16);
  final selectedLettersStyle = GoogleFonts.scheherazadeNew(fontSize: 24);

  @override
  void initState() {
    super.initState();
    setState(() {
      _rootLetters = widget.rootLetters;
    });
  }

  onPressed(ArabicLetter value) {
    switch (_selectedIndex) {
      case 0:
        setState(() {
          _rootLetters = _rootLetters.updateFirstRadical(value);
          _selectedToggles[_selectedIndex] = false;
          _selectedIndex = 1;
          _selectedToggles[_selectedIndex] = true;
        });
        break;

      case 1:
        setState(() {
          _rootLetters = _rootLetters.updateSecondRadical(value);
          _selectedToggles[_selectedIndex] = false;
          _selectedIndex = 2;
          _selectedToggles[_selectedIndex] = true;
        });
        break;

      case 2:
        setState(() {
          _rootLetters = _rootLetters.updateThirdRadical(value);
          _selectedToggles[_selectedIndex] = false;
          _selectedIndex = 3;
          _selectedToggles[_selectedIndex] = true;
        });
        break;

      case 3:
        setState(() {
          _rootLetters = _rootLetters.updateFourthRadical(value);
          _selectedToggles[_selectedIndex] = false;
          _selectedIndex = 0;
          _selectedToggles[_selectedIndex] = true;
        });
        break;

      default:
        break;
    }
  }

  createButton(ArabicLetter value) {
    return ElevatedButton(
        onPressed: () => onPressed(value),
        style: ElevatedButton.styleFrom(
            backgroundColor: const Color.fromRGBO(245, 245, 220, 1),
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(10))),
        child: Text(value.label, style: keyboradButtonStyle));
  }

  get slectedLettersView {
    return Center(
        child: Directionality(
      textDirection: TextDirection.rtl,
      child: ToggleButtons(
          isSelected: _selectedToggles,
          onPressed: (int index) {
            var newSelectedIndex = index;
            if (index == _selectedIndex) {
              newSelectedIndex = (index + 1) % _selectedToggles.length;
            }
            setState(() {
              _selectedToggles[_selectedIndex] = false;
              _selectedIndex = newSelectedIndex;
              _selectedToggles[_selectedIndex] = true;
            });
          },
          fillColor: const Color.fromRGBO(245, 245, 220, 1),
          constraints: const BoxConstraints(minHeight: 64.0, minWidth: 64.0),
          textStyle: selectedLettersStyle,
          children: _rootLetters
              .letters()
              .map((e) => Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(e.label),
                  ))
              .toList()),
    ));
  }

  get keyboardView {
    return Expanded(
        child: Directionality(
            textDirection: TextDirection.rtl,
            child: GridView.count(
                crossAxisCount: 10,
                mainAxisSpacing: 5,
                crossAxisSpacing: 5,
                children: [
                  createButton(ArabicLetter.Hamza),
                  createButton(ArabicLetter.Ba),
                  createButton(ArabicLetter.Ta),
                  createButton(ArabicLetter.Tha),
                  createButton(ArabicLetter.Jeem),
                  createButton(ArabicLetter.Hha),
                  createButton(ArabicLetter.Kha),
                  createButton(ArabicLetter.Dal),
                  createButton(ArabicLetter.Thal),
                  createButton(ArabicLetter.Ra),
                  createButton(ArabicLetter.Zain),
                  createButton(ArabicLetter.Seen),
                  createButton(ArabicLetter.Sheen),
                  createButton(ArabicLetter.Sad),
                  createButton(ArabicLetter.Ddad),
                  createButton(ArabicLetter.Tta),
                  createButton(ArabicLetter.Dtha),
                  createButton(ArabicLetter.Ain),
                  createButton(ArabicLetter.Ghain),
                  createButton(ArabicLetter.Fa),
                  createButton(ArabicLetter.Qaf),
                  createButton(ArabicLetter.Kaf),
                  createButton(ArabicLetter.Lam),
                  createButton(ArabicLetter.Meem),
                  createButton(ArabicLetter.Noon),
                  createButton(ArabicLetter.Ha),
                  createButton(ArabicLetter.Waw),
                  createButton(ArabicLetter.Ya)
                ])));
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
        scrollable: true,
        content: SizedBox(
          width: widget.width,
          height: widget.height,
          child: Column(children: [
            slectedLettersView,
            const SizedBox(height: 16.0),
            keyboardView
          ]),
        ),
        actions: <Widget>[
          TextButton(
              onPressed: () => Navigator.pop(context, 'Cancel'),
              child: const Text("Cancel")),
          TextButton(
              onPressed: () {
                widget.onChanged(_rootLetters);
                Navigator.pop(context, 'OK');
              },
              child: const Text("OK"))
        ]);
  }
}
