package sgtmelon.safedialog

import androidx.annotation.CallSuper
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before

/**
 * Parent class for Unit tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After @CallSuper open fun tearDown() {
        unmockkAll()
    }
}