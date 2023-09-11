import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';

class Utils {
  static void showConfirmationDialog(
      BuildContext context,
      bool showCancelButton,
      String title,
      String contentText,
      VoidCallback? onSubmit) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text(title),
            content: Text(contentText),
            actions: [
              if (showCancelButton)
                ElevatedButton(
                    onPressed: () => Navigator.pop(context, 'Cancel'),
                    child: const Text("Cancel"))
              else
                Container(),
              ElevatedButton(
                  onPressed: () {
                    if (onSubmit != null) {
                      onSubmit();
                    }
                    Navigator.pop(context, 'OK');
                  },
                  child: const Text("OK"))
            ],
          );
        });
  }

  static String getParentPath(String filePath) {
    var index = filePath.lastIndexOf(Platform.pathSeparator);
    return filePath.substring(0, index);
  }

  static String getFileName(String filePath) {
    var index = filePath.lastIndexOf(Platform.pathSeparator);
    return filePath.substring(index + 1);
  }

  static PlatformFile toPlatformFile(String filePath) {
    return PlatformFile(path: filePath, name: getFileName(filePath), size: 0);
  }
}
