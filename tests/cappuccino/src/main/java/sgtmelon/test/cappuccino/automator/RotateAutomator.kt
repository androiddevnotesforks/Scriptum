package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.halfChance

/**
 * Automator for rotate device during tests.
 */
class RotateAutomator(private val uiDevice: UiDevice) {

    private var isNormal: Boolean = true

        // TODO use in tests only "switch"

    fun toSide() {
        /** Skip if already rotated to side. */
        if (!isNormal) return

        isNormal = false
        if (halfChance()) uiDevice.setOrientationLeft() else uiDevice.setOrientationRight()
        await(ROTATE_PAUSE)
    }

    fun toNormal() {
        /** Skip if already in normal rotation. */
        if (isNormal) return

        isNormal = true
        uiDevice.setOrientationNatural()
        await(ROTATE_PAUSE)
    }

    fun switch() = if (isNormal) toSide() else toNormal()

    companion object {
        const val ROTATE_PAUSE = 1500L
    }
}