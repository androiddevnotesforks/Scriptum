package sgtmelon.scriptum.domain.model.state

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [IconState].
 */
class IconStateTest : ParentTest() {

    private val iconState = IconState()

    @Test fun notAnimate() {
        assertTrue(iconState.animate)
        iconState.notAnimate { assertFalse(iconState.animate) }
        assertTrue(iconState.animate)
    }

}