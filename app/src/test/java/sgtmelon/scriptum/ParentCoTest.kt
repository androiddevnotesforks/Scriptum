package sgtmelon.scriptum

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import sgtmelon.scriptum.extension.isTesting

/**
 * Parent class for coroutines tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentCoTest : ParentTest() {

    @get:Rule val coTestRule = CoroutinesTestRule()

    override fun setUp() {
        super.setUp()
        isTesting = true
    }

    override fun tearDown() {
        super.tearDown()
        isTesting = false
    }

    protected fun startCoTest(func: suspend TestCoroutineScope.() -> Unit) {
        coTestRule.dispatcher.runBlockingTest { func() }
    }

}