# gfc-util [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.gfccollective/gfc-util_2.12/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/org.gfccollective/gfc-util_2.12) [![Build Status](https://github.com/gfc-collective/gfc-util/workflows/Scala%20CI/badge.svg)](https://github.com/gfc-collective/gfc-util/actions) [![Coverage Status](https://coveralls.io/repos/gfc-collective/gfc-util/badge.svg?branch=main&service=github)](https://coveralls.io/github/gfc-collective/gfc-util?branch=main)

A library that contains a few scala utility classes.
A fork and new home of the now unmaintained Gilt Foundation Classes (`com.gilt.gfc`), now called the [GFC Collective](https://github.com/gfc-collective), maintained by some of the original authors.


## Getting gfc-util

The latest version is 1.0.0, released on 21/Jan/2020 and cross-built against Scala 2.12.x and 2.13.x.

If you're using SBT, add the following line to your build file:

```scala
libraryDependencies += "org.gfccollective" %% "gfc-util" % "1.0.0"
```

For Maven and other build tools, you can visit [search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Corg.gfccollective).
(This search will also list other available libraries from the GFC Collective.)

## Contents and Example Usage

### org.gfccollective.util.Retry

Allows a retry of a potentially failing function with or without an exponentially growing wait period:
```scala
// some potentially failing function
def inputNumber: Int = ???

// Retry the function up to 10 times or until it succeeds
val number: Int = Retry.retry(10)(inputNumber)
```
```scala
// some potentially failing function
def readFile: Seq[String] = ???

// Retry the function up to 10 times until it succeeds, with an exponential backoff,
// starting at 10 ms and doubling each iteration until it reaches 1 second, i.e.
// 10ms, 20ms, 40ms, 80ms, 160ms, 320ms, 640ms, 1s, 1s, 1s
val contents: Seq[String] = Retry.retryWithExponentialDelay(maxRetryTimes = 10,
                                                            maxRetryTimeout = 5.minutes.fromNow,
                                                            initialDelay = 10.millis,
                                                            maxDelay = 1.second,
                                                            exponentFactor = 2)
                                                           (readFile)
```

Allows a retry of a function `I => O` via a function `I => Either[I, O]`:
```scala
val arr: Array[Boolean] = Array.fill(5)(false)

// Up to 10 times, set a random array index to 'true' and 
// return "success" when all array elements are 'true', or throw 'TooManyRetries'
Retry.retryFold(maxRetryTimes = 10)(arr){ a: Array[Boolean] =>
  if (a.forall(identity)) {
    Right("success")
  } else {
    a(scala.util.Random.nextInt(a.length)) = true
    Left(a)
  }
}
```
```scala
// Set a random array index to 'true' and return "success" when all array elements are 'true'.
// Retry this up to 10 times until it succeeds, with an exponential backoff,
// starting at 10 ms and doubling each iteration until it reaches 1 second, i.e.
// 10ms, 20ms, 40ms, 80ms, 160ms, 320ms, 640ms, 1s, 1s, 1s, or throw 'TooManyRetries'
def func(a: Array[Boolean]): Either[Array[Boolean], String] = {
  if (a.forall(identity)) {
    Right("success")
  } else {
    a(scala.util.Random.nextInt(a.length)) = true
    Left(a)
  }
}

val result: String = Retry.retryFoldWithExponentialDelay(maxRetryTimes = 10,
                                                         maxRetryTimeout = 5.minutes.fromNow,
                                                         initialDelay = 10.millis,
                                                         maxDelay = 1.second,
                                                         exponentFactor = 2)
                                                        (arr)
                                                        (func)
```


### org.gfccollective.util.RateLimiter and org.gfccollective.util.ThreadSafeRateLimiter

RateLimiter can be used to rate-limit calls to a work function, e.g. a function that writes to a db.
ThreadSafeRateLimiter is a thread safe version of RateLimiter, that synchronizes calls to the limit function.
```scala
val rateLimiter = new ThreadSafeRateLimiter(100) // Limit to 100 calls/second

def writeObject(obj: DBObject) = rateLimiter.limit {
  db.insert(obj)
}
```

### org.gfccollective.util.SingletonCache

A cache for objects that need their lifecycle to be managed and can't be just throw-away,
e.g. for when ConcurrentHashMap.putIfAbsent(new Something()) may result in a Something
instance that need to be closed if another thread's putIfAbsent was successful.
```scala
val inputStreamCache = new SingletonCache[File]

val file = new File("foo")

val is: InputStream = inputStreamCache(file) {
  new FileInputStream(file)
}
```

### org.gfccollective.util.Throwables

Utility to unwind nested Throwable stacks
```scala
val e1 = new Exception()
val e2 = new Exception(e1)
val e3 = new Exception(e3)

val t: Throwable = Throwables.rootCause(e3)

t should be theSameInstanceAs e1
```

## License

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
