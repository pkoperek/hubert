package pl.edu.agh.hubert.languages

abstract class Language {

  val words: List[LanguageWord]

}

class MathLanguage extends Language {
  val words = List[LanguageWord]()

}

class NauronsLanguage extends Language {
  val words = List[LanguageWord]()

}

object Languages {

  val languages = List(new MathLanguage, new NauronsLanguage)

}