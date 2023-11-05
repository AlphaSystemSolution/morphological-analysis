import 'package:flutter/material.dart';
import 'package:flutter_form_builder/flutter_form_builder.dart';

class NewTemplateDialog extends StatefulWidget {
  const NewTemplateDialog({super.key, required this.onChanged});

  final ValueChanged<String> onChanged;

  @override
  State<NewTemplateDialog> createState() => _NewTemplateDialogState();
}

class _NewTemplateDialogState extends State<NewTemplateDialog> {
  final _formKey = GlobalKey<FormBuilderState>();
  static const _labelStyle = TextStyle(fontWeight: FontWeight.bold);

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text("Create new template", textAlign: TextAlign.center),
      content: FormBuilder(
        key: _formKey,
        initialValue: const {"template_name": ""},
        child: _buildNameWidget,
      ),
      actions: <Widget>[
        ElevatedButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text("Cancel")),
        ElevatedButton(onPressed: _submitForm, child: const Text("OK")),
      ],
    );
  }

  get _buildNameWidget => FormBuilderTextField(
        name: "template_name",
        decoration: const InputDecoration(
            labelText: "Template Name", labelStyle: _labelStyle),
        textInputAction: TextInputAction.next,
      );

  void _submitForm() {
    var templateName =
        _formKey.currentState?.fields['template_name']?.value as String;
    if (templateName.isNotEmpty) {
      widget.onChanged(templateName);
      Navigator.pop(context, 'OK');
    }
  }
}
