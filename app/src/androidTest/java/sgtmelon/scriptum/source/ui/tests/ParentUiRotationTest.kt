package sgtmelon.scriptum.source.ui.tests

import org.junit.After
import sgtmelon.test.cappuccino.automator.RotateAutomator

/**
 * Parent class for automatic Rotation tests.
 *
 * For rotate screen use: ctrl + leftArrow/rightArrow.
 */
abstract class ParentUiRotationTest : ParentUiTest() {

    protected val rotate = RotateAutomator(uiDevice)

    @After override fun tearDown() {
        super.tearDown()
        rotate.toNormal()
    }
}