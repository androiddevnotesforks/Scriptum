package sgtmelon.scriptum

import android.app.Application
import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

/**
 * Parent class for ViewModel tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentViewModelTest : ParentTest() {

    @MockK protected lateinit var application: Application
    @MockK protected lateinit var context: Context

    @get:Rule val coTestRule = CoroutinesTestRule()

    protected fun startCoTest(func: suspend TestCoroutineScope.() -> Unit) {
        coTestRule.dispatcher.runBlockingTest { func() }
    }

    override fun setUp() {
        super.setUp()

        MockKAnnotations.init(this, relaxUnitFun = true)

        every { application.applicationContext } returns context
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    abstract fun onDestroy()

}