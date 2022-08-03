package sgtmelon.scriptum.parent

import org.junit.After
import sgtmelon.scriptum.parent.automator.RotateAutomator

/**
 * Parent class for automatic Rotation tests.
 *
 * For rotate screen use: ctrl + leftArrow/rightArrow.
 */
abstract class ParentUiRotationTest : ParentUiTest() {

    protected val rotate by lazy { RotateAutomator(uiDevice) }

    @After override fun tearDown() {
        super.tearDown()
        rotate.toNormal()
    }
}