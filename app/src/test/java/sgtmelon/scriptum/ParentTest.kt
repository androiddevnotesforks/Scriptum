package sgtmelon.scriptum

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before

/**
 * Parent class for Unit tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setUp() {}

    @After @CallSuper open fun tearDown() {}

}