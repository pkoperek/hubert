package pl.edu.agh.hubert.languages

class CompositeLanguage(
                         val compositeWords: Array[Class[_ <: CompositeWord]],
                         val terminalWords: Array[Class[_ <: TerminalWord]]
                         ) extends Language(compositeWords ++ terminalWords) {
}
