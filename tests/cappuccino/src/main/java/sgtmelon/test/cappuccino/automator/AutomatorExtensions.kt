package sgtmelon.test.cappuccino.automator

import android.view.KeyEvent
import androidx.test.uiautomator.UiDevice

fun UiDevice.pastFromClipboard() = pressKeyCode(KeyEvent.KEYCODE_V, KeyEvent.META_CTRL_MASK)