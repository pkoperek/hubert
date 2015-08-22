package pl.edu.agh.hubert.languages

import pl.edu.agh.hubert.InputRow


abstract class LanguageWord() {
  def evaluateInput(input: InputRow): Double
}

abstract class TerminalWord extends LanguageWord {}

abstract class CompositeWord(
                              val internalWords: Array[_ <: LanguageWord],
                              val requiredInternalWordsNo: Int
                              ) extends LanguageWord {
}
