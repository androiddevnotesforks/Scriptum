package sgtmelon.scriptum.basic.notifications

import android.app.Instrumentation
import android.content.Context
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.presentation.factory.NotificationFactory
import sgtmelon.scriptum.ui.automator.NotificationUi

/**
 * Automator for control application notifications.
 */
class NotificationAutomator(
    private val context: Context,
    instrumentation: Instrumentation
) {

    private val uiDevice = UiDevice.getInstance(instrumentation)

    fun assert(item: NoteItem) {
        assertNotification(item)
    }

    fun open(item: NoteItem) {
        TODO()
    }

    fun unbind(item: NoteItem) {
        openStatusBar()

        val notification = assertNotification(item)
        notification.unbindObject.click()
        item.isStatus = false

        closeStatusBar()
    }

    private fun openStatusBar() {
        uiDevice.openNotification()

        val appName = context.getString(R.string.app_name)
        uiDevice.wait(Until.hasObject(By.text(appName)), TIMEOUT)
    }

    private fun closeStatusBar() {
        uiDevice.pressBack()
        uiDevice.waitForIdle(700)
    }

    private fun assertNotification(item: NoteItem): NotificationUi.Note {
        val statusTitle = NotificationFactory.getStatusTitle(context, item)
        val statusText = NotificationFactory.getStatusText(context, item)
        val unbindText = context.getString(R.string.notification_button_unbind).toUpperCase()

        val titleObject = uiDevice.findObject(By.text(statusTitle))
        val textObject = uiDevice.findObject(By.textStartsWith(statusText))
        val unbindObject = uiDevice.findObject(By.text(unbindText))

        assertEquals(statusTitle, titleObject.text)
        assertTrue(textObject.text.startsWith(statusText))
        assertEquals(unbindText, unbindObject.text)

        return NotificationUi.Note(titleObject, textObject, unbindObject)
    }

    companion object {
        const val TIMEOUT: Long = 10 * 1000
    }
}