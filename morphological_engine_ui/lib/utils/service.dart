import 'dart:convert';
import 'dart:typed_data';

import 'package:http/http.dart' as http;

import '../models/conjugation_template.dart';

class MorphologicalEngineService {
  static const _uri =
      "http://127.0.0.1:8080/morphological-engine/morphological-chart/docx";

  static Future<Uint8List?> exportToWordDoc(ConjugationTemplate template) async {
    var body = jsonEncode(template);
    final response = await http.post(Uri.parse(_uri),
        headers: {
          "Content-Type": "application/json;charset=UTF-8",
          "Content-Length": "${body.length}"
        },
        body: body);

    if (response.statusCode == 200) {
      return response.bodyBytes;
    }
    return null;
  }
}
