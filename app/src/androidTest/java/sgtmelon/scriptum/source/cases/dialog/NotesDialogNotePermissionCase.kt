package sgtmelon.scriptum.source.cases.dialog

import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import org.junit.Before
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.cases.permissions.PostNotificationsCase
import sgtmelon.scriptum.source.ui.screen.dialogs.NoteDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain


/**
 * Parent class for tests permissions of [NoteDialogUi] inside [MainPage.NOTES].
 */
abstract class NotesDialogNotePermissionCase(private val type: NoteType) : ParentUiRotationTest(),
    PostNotificationsCase {

    @Before override fun setUp() {
        super.setUp()
        assertPostNotificationsNotGranted(context)
    }

    abstract fun insert(): NoteItem

    override fun allow() {

        val item = insert()
        launchMain {
            openNotes {
                openNoteDialog(item) {
                    bind()

                        val allowPermissions: UiObject = uiDevice.findObject(UiSelector().text("Allow"))
                        if (allowPermissions.exists()) {
                            allowPermissions.click()
                        }
                }

                openNoteDialog(item) {
                    bind()
                }
            }
        }
    }

    override fun deny() {
        TODO("Not yet implemented")
    }

    override fun denyInfo() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun rotateClose() {
        TODO("Not yet implemented")
    }

    override fun rotateWork() {
        TODO("Not yet implemented")
    }
}