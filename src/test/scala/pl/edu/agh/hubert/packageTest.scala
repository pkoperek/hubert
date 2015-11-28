package pl.edu.agh.hubert

import org.scalatest.FunSuite

class packageTest extends FunSuite {

  test("pairs doesn't return null") {
    assert(pairs(Array(1, 1)) != null, "Should not be null")
  }

  test("pairs contain proper elements") {
    // Given
    val toPair = Array(1, 1, 2, 2, 3, 3)

    // When
    val paired = pairs(toPair)

    // Then
    assert(paired(0)._1 == 1)
    assert(paired(0)._2 == 1)
    assert(paired(1)._1 == 2)
    assert(paired(1)._2 == 2)
    assert(paired(2)._1 == 3)
    assert(paired(2)._2 == 3)
  }

}
