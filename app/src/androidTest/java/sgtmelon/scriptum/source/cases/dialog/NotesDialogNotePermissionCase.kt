package sgtmelon.scriptum.source.cases.dialog

import org.junit.Before
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.cases.permissions.PostNotificationsCase
import sgtmelon.scriptum.source.ui.screen.dialogs.NoteDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest

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
        TODO("Not yet implemented")
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