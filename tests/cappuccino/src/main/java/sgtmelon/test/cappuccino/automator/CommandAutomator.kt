package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice

/**
 * Automator for execute device adb commands during tests.
 */
class CommandAutomator(private val uiDevice: UiDevice) {

    fun turnOnWifi() = uiDevice.executeShellCommand("svc wifi enable")

    /**
     * Increase long press timeout, for preventing fake espresso click performed like a
     * long one.
     */
    fun increaseLongPress() = changeLongPress(EXTRA_LONG_PRESS)

    fun decreaseLongPress() = changeLongPress(DEF_LONG_PRESS)

    /**
     * Decrease long press time needed for fast access (when it's important) to long press
     * feature.
     */
    inline fun smallLongPress(inBetween: () -> Unit) {
        decreaseLongPress()
        inBetween()
        increaseLongPress()
    }

    fun changeLongPress(timeMs: Long) {
        uiDevice.executeShellCommand("settings put secure long_press_timeout $timeMs")
    }

    companion object {
        private const val EXTRA_LONG_PRESS = 1500L
        private const val DEF_LONG_PRESS = 500L
    }
}