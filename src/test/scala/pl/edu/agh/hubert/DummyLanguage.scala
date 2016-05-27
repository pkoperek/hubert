package pl.edu.agh.hubert

import pl.edu.agh.hubert.engine._

object DummyLanguage {
  def dummyLanguage() = {
    Language("dummy", Set(classOf[DummyCompositeWord], classOf[DummyTerminalWord], classOf[OtherDummyTerminalWord]))
  }
}

class DummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: Input): Array[Double] = {
    Array.fill(input.variablesCount)(1.0)
  }
}

class OtherDummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: Input): Array[Double] = {
    Array.fill(input.variablesCount)(1.0)
  }
}

class DummyCompositeWord(val left: LanguageWord, val right: LanguageWord) extends CompositeWord(Array(left, right), 2) {
  override def evaluateInput(input: Input): Array[Double] = {
    internalWords.map(w => w.evaluateInput(input)).foldLeft(Array.fill(input.variablesCount)(0.0))((l: Array[Double], r: Array[Double]) => l.zip(r).map(p => p._1 + p._2))
  }
}
