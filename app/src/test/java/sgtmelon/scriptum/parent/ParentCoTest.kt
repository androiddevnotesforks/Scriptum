package sgtmelon.scriptum.parent

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.common.utils.isTesting

/**
 * Parent class for coroutines tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentCoTest : ParentTest() {

    @get:Rule val coTestRule = CoroutinesTestRule()

    @Before override fun setup() {
        super.setup()
        isTesting = true
    }

    @After override fun tearDown() {
        super.tearDown()
        isTesting = false
    }

    // TODO deprecated
    protected fun startCoTest(func: suspend TestCoroutineScope.() -> Unit) {
        coTestRule.dispatcher.runBlockingTest { func() }
    }

}