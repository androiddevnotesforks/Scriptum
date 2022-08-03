package sgtmelon.scriptum.parent

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before

/**
 * Parent class for tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setUp() = Unit

    @After @CallSuper open fun tearDown() = Unit
}