package sgtmelon.scriptum.cleanup.parent

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.extensions.isCoTesting
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Parent class for coroutines tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentCoTest : ParentTest() {

    @get:Rule val coTestRule = CoroutinesTestRule()

    @Before override fun setUp() {
        super.setUp()
        isCoTesting = true
    }

    @After override fun tearDown() {
        super.tearDown()
        isCoTesting = false
    }

    // TODO deprecated
    @Deprecated("Use simple runBlocking {...}")
    protected fun startCoTest(func: suspend TestCoroutineScope.() -> Unit) {
        coTestRule.dispatcher.runBlockingTest { func() }
    }

}