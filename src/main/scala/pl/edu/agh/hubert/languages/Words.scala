package pl.edu.agh.hubert.languages

abstract class InputRow {
  def valueForId(id: String): Double
}

abstract class LanguageWord() {
  def evaluateInput(input: InputRow): Double
}

abstract class TerminalWord extends LanguageWord {}

abstract class CompositeWord(val internalWords: List[LanguageWord]) extends LanguageWord {
}
