package pl.edu.agh.hubert.languages

abstract class LanguageWord {

}

abstract class TerminalWord extends LanguageWord {
  
  
}

abstract class CompositeWord extends LanguageWord {

  val heldWords: List[LanguageWord]

}
