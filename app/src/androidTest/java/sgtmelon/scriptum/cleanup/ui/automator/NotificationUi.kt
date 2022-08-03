package sgtmelon.scriptum.cleanup.ui.automator

import androidx.test.uiautomator.UiObject2

/**
 * Sealed UI class for notifications in status bar.
 */
sealed class NotificationUi {

    data class Note(
        val titleObject: UiObject2,
        val textObject: UiObject2,
        val unbindObject: UiObject2
    ) : NotificationUi()
}