package com.brustoloni.taxi.presentation.setup

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.brustoloni.taxi.data.di.mockURLModule
import com.brustoloni.taxi.data.infraestructure.ResourceUtils
import io.mockk.MockKAnnotations
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
@LargeTest
abstract class BaseInstrumentedTest : KoinTest {

    lateinit var mockWebServer: MockWebServer

    @Before
    open fun setUp() {

        mockWebServer = MockWebServer()

        MockKAnnotations.init(this, relaxUnitFun = true)

        mockWebServer.start(Constants.PORT)

        StandAloneContext.loadKoinModules(
            listOf(
                mockURLModule
            )
        )
    }

    @After
    fun tearDown() {
        if(::mockWebServer.isInitialized){
            mockWebServer.shutdown()
        }
    }

    fun setupMockWebServer(pathMock: String, delay: Long = 1, statusCode: Int = 200) {
        val json = ResourceUtils().openFile(pathMock)
        val mockResponse = MockResponse()

        mockResponse
            .setResponseCode(statusCode)
            .setBody(json)
            .throttleBody(Long.MAX_VALUE, delay, TimeUnit.MILLISECONDS)

        mockWebServer.enqueue(mockResponse)
    }

}