package com.gilt.gfc.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ZipUtilsTest extends AnyFunSuite with Matchers {
  
  private val stuff = Array[String]("Ciao", "Hello", "master", "originators", "GILT")

  test("zips correctly a simple array of strings") {
    val actual: Array[String] = ZipUtils.unzip[Array[String]](ZipUtils.zip[Array[String]](stuff))
    assert(actual === stuff)
  }

  test("zips an empty array in a zipped empty array") {
    val actual: Array[Int] = ZipUtils.unzip[Array[Int]](ZipUtils.zip[Array[Int]](Array.empty[Int]))
    assert(actual === Array.empty[Int])
  }
}
