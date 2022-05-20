package topics.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Docs:
 * * [Flow](https://kotlinlang.org/docs/flow.html)
 *
 * https://stackoverflow.com/questions/60448590/should-a-library-function-be-suspend-or-return-deferred
 * >
 *   Kotlin coroutines are designed along the ["sequential by default"](https://kotlinlang.org/docs/reference/coroutines/composing-suspending-functions.html#sequential-by-default) guideline. That means that your API should always expose suspend funs and the user, if and when they really need it, can easily wrap them in async.
 *
 *   The advantage of that is analogous to the advantages of cold flows with respect to hot flows: a suspendable function is active only while control is inside it. When it returns, it has not left behind a task running in the background.
 *
 *   Whenever you return a Deferred, the user must start worrying what happens if they don't manage to await on the result. Some code paths may ignore it, the calling code may get an exception, and then their application has a leak.
 *
 */
@OptIn(ExperimentalCoroutinesApi::class) // ...This class requires opt-in itself and can only be used with the compiler argument -opt-in=kotlin.RequiresOptIn.
class CoroutinesTest {

    @Test
    fun test() = runTest {
        println("Hello")
        delay(500) // does not delay in test context
        println("World!")
    }

    fun staticFlowEndless(): Flow<Long> = flow {
        while(true){
            emit(1)
        }
    }

    fun staticFlow(): Flow<Long> = flow {
        for (i in 1..5) {
            emit(2)
        }
    }

    fun staticSequenceEndless(): Sequence<Long> = sequence<Long> {
        while(true) {
            yield(1)
        }
    }

    @Test
    fun zip() = runTest {

        // TODO: see if there are matchers that can work with flow directly
        val list: List<Pair<Long, Long>> = staticFlowEndless().zip(staticFlow()) { a, b ->
            a to b
        }
            .toList()


        assertThat(list).containsExactly(
            1L to 2L,
            1L to 2L,
            1L to 2L,
            1L to 2L,
            1L to 2L,
        )

        assertThat(list).isEqualTo(listOf(
            1L to 2L,
            1L to 2L,
            1L to 2L,
            1L to 2L,
            1L to 2L,
        ))

        println("collect:")
        staticFlow().collect(::println)
        println("toList:")
        staticFlow().toList().apply(::println)
        println("toList (endless):")
        staticFlowEndless().take(10).toList().apply(::println)

        println("toList (endless):")
        staticFlowEndless().take(10).toList().apply(::println)
        println("Sequence#toList (endless):")
        staticSequenceEndless().take(4).toList()
    }

    // CompletableFuture can be mapped to Deferred using org.jetbrains.kotlinx:kotlinx-coroutines-jdk8
    @Test
    fun deferredTest() = runTest {
        val deferred: Deferred<Int> = async {
            1
        }
        assertThat(deferred.await()).isEqualTo(1)
    }

    @Test
    fun deferredFlowInteropTest() = runTest {
        val deferred: Deferred<Int> = async {
            println("deferred runs")
            1
        }
        // Deferred is cold?
        val f = flow {
            println("flow runs init") // 1 time
            while(true){
                println("flow runs") // 4 times
                emit("a")
            }
        }
        // Flow is cold

        assertThat(f.take(4).map { it to deferred.await() }.toList()).isEqualTo(
            listOf(
                "a" to 1,"a" to 1,"a" to 1,"a" to 1,
            )
        )
    }

    @Test
    suspend fun selectTest() = runTest {
        TODO()
    }

}

/*
override suspend fun load(queryParams: SearchParams): SearchEngineResult = coroutineScope {
    val searchRequest = SearchRequest().indices(indexName)
        .source(SearchSourceBuilder()
            .size(2)
            .query(QueryBuilders.matchAllQuery()))

    val searchHits = async {
        elasticsearchClient.search(searchRequest).asFlow()
            .map<SearchHit?, HitESResponse> { objectMapper.readValue(it.toString()) }
            .map { it.source }
            .map { elasticSearchResponseMapper.toApiResponse(it) }
            .toList()
    }
    val hitCount =  async {
        elasticsearchClient.count(searchRequest).asFlow().singleOrNull() ?: 0
    }

    SearchEngineResult(hitCount.await(), searchHits.await())
}
 */