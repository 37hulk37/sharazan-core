package com.sharazan.core.pipeline

import com.sharazan.core.exception.ApplicationException
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Uri
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PipelineTest {

    @Test
    fun `run interceptors 'preProcess()' and 'postProcess()' then compare requests recordings`() {
        val requests = mutableListOf<String>()

        val phase = Phase("test", listOf(
            recordingInterceptor("first", requests),
            recordingInterceptor("second", requests),
        ))
        val pipeline = Pipeline(listOf(phase))


        val processedRequest = pipeline.preProcess(request())
        pipeline.postProcess(processedRequest, Response(Status.OK))

        assertEquals(listOf(
            "before:first",
            "before:second",
            "after:second",
            "after:first"
        ), requests)
    }

    @Test
    fun `run pipeline 'preProcess()' with empty phases then compare requests`() {
        val pipeline = Pipeline(emptyList())
        val original = request()

        assertEquals(original, pipeline.preProcess(original))
    }

    @Test
    fun `check thrown exceptions when pipeline with interceptor throws exception `() {
        val phase = Phase("test with throwing exception", listOf(
            interceptor(before = { throw ApplicationException("What's going on?") })
        ))
        val pipeline = Pipeline(listOf(phase))

        val exception = assertFailsWith<ApplicationException> {
            pipeline.preProcess(request())
        }
        assertEquals("What's going on?", exception.message)
    }

    @Test
    fun `compare interceptors history where on of them throws exception`() {
        val requests = mutableListOf<String>()
        val phase = Phase("test with one of interceptors thrown exception", listOf(
            recordingInterceptor("first", requests),
            interceptor(before = { throw ApplicationException("boom") }),
        ))
        val pipeline = Pipeline(listOf(phase))

        assertFailsWith<ApplicationException> {
            pipeline.preProcess(request())
        }

        // "first" already ran and stamped its header onto an intermediate Request,
        // but Phase.preProcess's fold has no way to hand that partial Request back
        // to the caller once a later interceptor throws - it's simply gone.
        assertEquals(listOf("before:first"), requests)
    }

    @Test
    fun `postProcess folds phases in the same order as preProcess, not reversed`() {
        val events = mutableListOf<String>()
        val phaseOne = Phase("one", listOf(recordingInterceptor("phase-one", events)))
        val phaseTwo = Phase("two", listOf(recordingInterceptor("phase-two", events)))
        val pipeline = Pipeline(listOf(phaseOne, phaseTwo))

        val processedRequest = pipeline.preProcess(request())
        events.clear()
        pipeline.postProcess(processedRequest, Response(Status.OK))

        // only interceptors *within* a phase run in reverse on the way out -
        // the phases themselves keep the same order both ways.
        assertEquals(listOf("after:phase-one", "after:phase-two"), events)
    }

    @Test
    fun `after sees the same fully pre-processed request for every interceptor, not its own before result`() {
        val seenRequests = mutableListOf<Request>()

        val phase = Phase("test", listOf(
            interceptor(before = { it.header("X-first", "true") }),
            interceptor(before = { it.header("X-second", "true") }),
            interceptor(after = { req, response -> seenRequests += req; response }),
            interceptor(after = { req, response -> seenRequests += req; response }),
        ))
        val pipeline = Pipeline(listOf(phase))

        val processedRequest = pipeline.preProcess(request())
        pipeline.postProcess(processedRequest, Response(Status.OK))

        assertEquals(listOf(processedRequest, processedRequest), seenRequests)
        assertEquals("true", processedRequest.header("X-first"))
        assertEquals("true", processedRequest.header("X-second"))
    }

    @Test
    fun `a phase with no interceptors passes the request and response through unchanged`() {
        val phase = Phase("empty", emptyList())
        val pipeline = Pipeline(listOf(phase))
        val originalRequest = request()
        val originalResponse = Response(Status.OK)

        val processedRequest = pipeline.preProcess(originalRequest)
        val processedResponse = pipeline.postProcess(processedRequest, originalResponse)

        assertEquals(originalRequest, processedRequest)
        assertEquals(originalResponse, processedResponse)
    }

    @Test
    fun `an exception thrown by an interceptor's after propagates out of postProcess uncaught`() {
        val phase = Phase("test", listOf(interceptor(after = { _, _ -> throw ApplicationException("boom") })))
        val pipeline = Pipeline(listOf(phase))

        val exception = assertFailsWith<ApplicationException> {
            pipeline.postProcess(request(), Response(Status.OK))
        }
        assertEquals("boom", exception.message)
    }

    @Test
    fun `a phase that throws during preProcess prevents later phases from running at all`() {
        val requests = mutableListOf<String>()
        val phaseOne = Phase("one", listOf(interceptor(before = { throw ApplicationException("boom") })))
        val phaseTwo = Phase("two", listOf(recordingInterceptor("phase-two", requests)))
        val pipeline = Pipeline(listOf(phaseOne, phaseTwo))

        assertFailsWith<ApplicationException> {
            pipeline.preProcess(request())
        }

        assertTrue(requests.isEmpty())
    }

    private fun request() = Request(Method.GET, Uri.of("/test"))


    private fun interceptor(
        before: (Request) -> Request = { it },
        after: (Request, Response) -> Response = { _, response -> response },
    ): Interceptor = object : Interceptor {

        override fun before(request: Request): Request
                = before(request)

        override fun after(request: Request, response: Response): Response
                = after(request, response)

    }

    private fun recordingInterceptor(name: String, requests: MutableList<String>): Interceptor = interceptor(
        before = { request ->
            requests.add("before:$name")

            request.header("X-$name", name)
        },
        after = { _, response ->
            requests.add("after:$name")

            response.header("X-$name", name)
        },
    )

}




