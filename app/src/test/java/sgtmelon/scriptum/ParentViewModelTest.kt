package sgtmelon.scriptum

import android.app.Application
import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain

/**
 * Parent class for ViewModel tests.
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
abstract class ParentViewModelTest : ParentTest() {

    @MockK protected lateinit var application: Application
    @MockK protected lateinit var context: Context

    private val threadSurrogate = newSingleThreadContext("UI thread")

    override fun setUp() {
        super.setUp()

        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(threadSurrogate)

        every { application.applicationContext } returns context
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    abstract fun onDestroy()

}