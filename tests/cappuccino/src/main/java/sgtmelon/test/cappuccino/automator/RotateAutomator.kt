package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.test.cappuccino.utils.await

/**
 * Automator for rotate device during tests.
 */
class RotateAutomator(private val uiDevice: UiDevice) {

    private var isNormal: Boolean = true

    fun toSide() {
        /** Skip if already rotated to side. */
        if (!isNormal) return

        isNormal = false
        if (Random.nextBoolean()) uiDevice.setOrientationLeft() else uiDevice.setOrientationRight()
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