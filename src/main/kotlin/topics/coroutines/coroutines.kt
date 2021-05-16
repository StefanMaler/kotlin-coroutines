package topics.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * seems like we need some dependencies; not quite sure, though.
 *
 * `
 * org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2
 * `
 *
 * https://kotlinlang.org/docs/coroutines-basics.html
 * https://kotlinlang.org/docs/coroutines-overview.html
 * https://kotlinlang.org/docs/coroutines-guide.html
 *
 * https://kotlinlang.org/docs/composing-suspending-functions.html#concurrent-using-async
 * parallelize suspend functions with async { }
 */

// tag::snippet-main[]
fun main() {
    coroutinesBasics()
    // TODO: https://play.kotlinlang.org/hands-on/Introduction%20to%20Coroutines%20and%20Channels/01_Introduction
}
// end::snippet-main[]

/**
 * based on [Coroutines basics](https://kotlinlang.org/docs/coroutines-basics.html)
 */
private fun coroutinesBasics() {
    // https://kotlinlang.org/docs/coroutines-basics.html#your-first-coroutine
    // needs kotlinx-coroutines - not sure we can get that on the classpath... when downloaded with gradle, ends up in cache with a hash in path; should probably not be linked to without gradle means
    runBlocking { // establishes a coroutine context, blocks current thread until all coroutines in it are done ; this: CoroutineScope shown as IDE hint
        launch { // launch a new coroutine and continue - concurrent execution
            delay(500) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        print("Hello ") // main coroutine continues while a previous one is delayed
    }
    // > Hello World!

    // run in parallel: can use coroutineScope (similar to runBlocking, but suspending instead of blocking)
    // https://kotlinlang.org/docs/coroutines-basics.html#scope-builder-and-concurrency
    runBlocking {
        launch {
            delay(1000L)
            println("World 2")
        }
        launch {
            delay(500L)
            print("World 1 ")
        }
        print("Hello ")
    }

    // https://kotlinlang.org/docs/coroutines-basics.html#coroutines-are-light-weight
    runBlocking {
        repeat(10) { // launch a lot of coroutines, eg 100_000
            launch {
                delay(500L)
                print(".")
            }
        }
    }
}

//    GlobalScope.launch {
//        delay(1000)
//        println("Hello")
//    }

/*
 https://kotlinlang.org/docs/coroutines-overview.html
    https://kotlinlang.org/docs/coroutines-guide.html
    https://kotlinlang.org/docs/coroutines-basics.html
    TODO check: https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md
    TODO check: https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md

    https://kotlinlang.org/docs/async-programming.html
        https://kotlinlang.org/docs/async-programming.html#threading
        https://kotlinlang.org/docs/async-programming.html#futures-promises-and-others
            different programming model (no loops, ...)
            return type is wrapped
        https://kotlinlang.org/docs/async-programming.html#reactive-extensions
            observable streams: data is infinite, observable pattern with extensions;
            return type is wrapped (reactor has Mono+Flux but in general everything in rx(java, C#, js) is an infinite stream)
        https://kotlinlang.org/docs/async-programming.html#coroutines
            suspendable computations (continuations)
            programming model should be similar for synchronous and asynchronous code (also used in Go, #C async/await, ECMA script async/await)
 */
