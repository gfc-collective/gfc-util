package org.gfccollective.util.types

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TBoolTest extends AnyFunSuite with Matchers {
  test("compiles") {
    implicitly[TTrue =:= TTrue]
    implicitly[TFalse =:= TFalse]
    implicitly[Not[TTrue] =:= TFalse]
    implicitly[Not[TFalse] =:= TTrue]
    implicitly[TFalse || TFalse =:= TFalse]
    implicitly[TFalse || TTrue =:= TTrue]
    implicitly[TTrue || TFalse =:= TTrue]
    implicitly[TTrue || TTrue =:= TTrue]
    implicitly[TFalse && TFalse =:= TFalse]
    implicitly[TFalse && TTrue =:= TFalse]
    implicitly[TTrue && TFalse =:= TFalse]
    implicitly[TTrue && TTrue =:= TTrue]
    implicitly[Not[TFalse && TTrue] =:= TTrue]
    implicitly[TTrue && Not[TTrue] =:= TFalse]
  }

  test("doesn't compile") {
    assertTypeError("implicitly[TTrue =:= TFalse]")
    assertTypeError("implicitly[Not[TTrue] =:= TTrue]")
    assertTypeError("implicitly[TFalse || TTrue =:= TFalse]")
    assertTypeError("implicitly[TTrue && TFalse =:= TTrue]")
  }
}
