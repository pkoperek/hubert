package pl.edu.agh.hubert.languages

abstract class Language {

  val words: List[LanguageWord]

}

class MathLanguage extends Language {
  val words = List[LanguageWord]()

}

class NeuronsLanguage extends Language {
  val words = List[LanguageWord]()

}
