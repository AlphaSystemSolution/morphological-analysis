// ignore_for_file: constant_identifier_names

enum NamedTemplate implements Comparable<NamedTemplate> {
  FormICategoryAGroupUTemplate(form: "I", word: "فَعَلَ يَفْعُلُ"),
  FormICategoryAGroupITemplate(form: "I", word: "فَعَلَ يَفْعِلُ"),
  FormICategoryAGroupATemplate(form: "I", word: "فَعَلَ يَفْعَلُ"),
  FormICategoryIGroupATemplate(form: "I", word: "فَعِلَ يَفْعَلُ"),
  FormICategoryIGroupITemplate(form: "I", word: "فَعِلَ يَفْعِلُ"),
  FormICategoryUTemplate(form: "I", word: "فَعُلَ يَفْعُلُ"),
  FormIITemplate(form: "II", word: "تَفْعِيل"),
  FormIIITemplate(form: "III", word: "مُفَاعَلَة"),
  FormIVTemplate(form: "IV", word: "إِفْعَال"),
  FormVTemplate(form: "V", word: "تَفَعُّل"),
  FormVITemplate(form: "VI", word: "تَفَاعُل"),
  FormVIITemplate(form: "VII", word: "اِنْفِعَال"),
  FormVIIITemplate(form: "VIII", word: "اِفْتِعَال"),
  FormIXTemplate(form: "IX", word: "اِفْعِلَال"),
  FormXTemplate(form: "X", word: "اِسْتِفْعَال");

  const NamedTemplate({
    required this.form,
    required this.word,
  });

  final String form;
  final String word;

  @override
  int compareTo(NamedTemplate other) => word.compareTo(other.word);


  String displayValue() {
    return "$word ($form)";
  }

  static NamedTemplate fromName(String name) {
    switch (name) {
      case "FormICategoryAGroupUTemplate":
        return FormICategoryAGroupUTemplate;
      case "FormICategoryAGroupITemplate":
        return FormICategoryAGroupITemplate;
      case "FormICategoryAGroupATemplate":
        return FormICategoryAGroupATemplate;
      case "FormICategoryIGroupATemplate":
        return FormICategoryIGroupATemplate;
      case "FormICategoryIGroupITemplate":
        return FormICategoryIGroupITemplate;
      case "FormICategoryUTemplate":
        return FormICategoryUTemplate;
      case "FormIITemplate":
        return FormIITemplate;
      case "FormIIITemplate":
        return FormIIITemplate; 
      case "FormIVTemplate":
        return FormIVTemplate;
      case "FormVTemplate":
        return FormVTemplate;
      case "FormVITemplate":
        return FormVITemplate;
      case "FormVIITemplate":
        return FormVIITemplate;
      case "FormVIIITemplate":
        return FormVIIITemplate;
      case "FormIXTemplate":
        return FormIXTemplate;
      case "FormXTemplate":
        return FormXTemplate;
      default: throw Exception("Invalid name: $name");
    }
  }
}