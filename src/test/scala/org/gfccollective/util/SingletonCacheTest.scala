package org.gfccollective.util

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SingletonCacheTest extends AnyFunSuite with Matchers {
  test("SingletonCache") {
    def makeCounters = new scala.collection.mutable.HashMap[String, AtomicInteger]() {
      override def default(k: String): AtomicInteger = {
        this += k -> new AtomicInteger
        this(k)
      }
    }

    val cache = new SingletonCache[String]()
    val numCalls = makeCounters

    def mkObj(key: String) = cache(key) {
      numCalls(key).incrementAndGet
      new Object
    }

    val foo1 = mkObj("foo")
    foo1 should be theSameInstanceAs(mkObj("foo"))
    foo1 should be theSameInstanceAs(mkObj("foo"))

    val bar1 = mkObj("bar")
    bar1 should be theSameInstanceAs(mkObj("bar"))
    bar1 should be theSameInstanceAs(mkObj("bar"))

    // the whole point of this object is to only call given closure once
    // for each distinct key
    numCalls.values.map(_.get).toList should be(List(1,1))
  }

  test("cache values") {
    val cache = new SingletonCache[String]()

    val val1 = UUID.randomUUID()
    val val2 = UUID.randomUUID()
    cache("one") { val1 } should be theSameInstanceAs(val1)
    cache("one") { UUID.randomUUID() } should be theSameInstanceAs(val1)

    cache("two") { val2 } should be theSameInstanceAs(val2)
    cache("two") { UUID.randomUUID() } should be theSameInstanceAs(val2)

    val values: Set[UUID] = cache.values.toSet
    values.size should be (2)
    values should contain (val1)
    values should contain (val2)
  }

  test("asMap") {
    val cache = new SingletonCache[String]()

    val val1 = UUID.randomUUID()
    val val2 = UUID.randomUUID()
    cache("one") { val1 }
    cache("two") { val2 }

    val asMap = cache.asMap[UUID]
    asMap.size shouldBe 2
    asMap.get("one") shouldBe Some(val1)
    asMap.get("two") shouldBe Some(val2)

    cache("three") { UUID.randomUUID() }

    asMap.size shouldBe 3
    cache.asMap.size shouldBe 3
  }
}
