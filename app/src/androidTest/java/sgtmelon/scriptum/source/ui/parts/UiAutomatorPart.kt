package sgtmelon.scriptum.source.ui.parts

import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import sgtmelon.scriptum.source.di.TestInjector

/**
 * Basic UI element for test with UiAutomator
 */
abstract class UiAutomatorPart {

    protected val context = TestInjector.context
    protected val uiDevice = TestInjector.uiDevice

    protected fun getObject(text: String): UiObject = uiDevice.findObject(UiSelector().text(text))

    companion object {
        const val AWAIT_TIMEOUT = 500L
    }
}