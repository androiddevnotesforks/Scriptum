package sgtmelon.test.cappuccino.automator

import androidx.test.uiautomator.UiDevice
import kotlin.random.Random
import sgtmelon.test.cappuccino.utils.await

/**
 * Automator for rotate device during tests.
 */
class RotateAutomator(private val uiDevice: UiDevice) {

    private var isNormal: Boolean = true

    fun toSide() = with(uiDevice) {
        isNormal = false
        if (Random.nextBoolean()) setOrientationLeft() else setOrientationRight()
        await(ROTATE_PAUSE)
    }

    fun toNormal() {
        isNormal = true
        uiDevice.setOrientationNatural()
        await(ROTATE_PAUSE)
    }

    fun switch() = if (isNormal) toSide() else toNormal()

    companion object {
        const val ROTATE_PAUSE = 1500L
    }
}