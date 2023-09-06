// ignore_for_file: constant_identifier_names

enum ArabicLetter implements Comparable<ArabicLetter> {
  Hamza(code: "'", label: "ء"),
  Alif(code: "A", label: "ا"),
  Ba(code: "b", label: "ب"),
  TaMarbuta(code: "p", label: "ة"),
  Ta(code: "t", label: "ت"),
  Tha(code: "v", label: "ث"),
  Jeem(code: "j", label: "ج"),
  Hha(code: "H", label: "ح"),
  Kha(code: "x", label: "خ"),
  Dal(code: "d", label: "د"),
  Thal(code: "*", label: "ذ"),
  Ra(code: "r", label: "ر"),
  Zain(code: "z", label: "ز"),
  Seen(code: "s", label: "س"),
  Sheen(code: "\$", label: "ش"),
  Sad(code: "S", label: "ص"),
  Ddad(code: "D", label: "ض"),
  Tta(code: "T", label: "ط"),
  Dtha(code: "Z", label: "ظ"),
  Ain(code: "E", label: "ع"),
  Ghain(code: "g", label: "غ"),
  Fa(code: "f", label: "ف"),
  Kaf(code: "k", label: "ك"),
  Qaf(code: "q", label: "ق"),
  Lam(code: "l", label: "ل"),
  Meem(code: "m", label: "م"),
  Noon(code: "n", label: "ن"),
  Ha(code: "h", label: "ه"),
  Waw(code: "w", label: "و"),
  Ya(code: "y", label: "ي"),
  Tatweel(code: "_", label: "\u0640"),
  Space(code: " ", label: "\u0020");

  const ArabicLetter({
    required this.code,
    required this.label,
  });

  final String code;
  final String label;

  @override
  int compareTo(ArabicLetter other) => code.compareTo(other.code);

  @override
  String toString() => label;

  static ArabicLetter fromCode(String code) {
    switch (code) {
      case "'":
        return ArabicLetter.Hamza;
      case "A":
        return ArabicLetter.Alif;
      default:
        throw Exception("Invalid code: $code");
    }
  }
  
}
