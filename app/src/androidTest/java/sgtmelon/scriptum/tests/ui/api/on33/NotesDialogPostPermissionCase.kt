package sgtmelon.scriptum.tests.ui.api.on33

import org.junit.Before
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.cases.permissions.BindNotePermissionCase
import sgtmelon.scriptum.source.ui.screen.dialogs.NoteDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesItem


/**
 * Parent class for test post permission called from [NoteDialogUi] inside [MainPage.NOTES].
 */
abstract class NotesDialogPostPermissionCase(private val type: NoteType) : ParentUiRotationTest(),
    BindNotePermissionCase {

    @Before override fun setUp() {
        super.setUp()

        throwOnWrongApi()
        assertPermissionNotGranted(context)
    }

    abstract fun insert(): NoteItem

    override fun allow() = launchNotesItem(insert()) {
        openNoteDialog(it) { bind() }
        postPermission { allow() }

        /** Assert it not appears. */
        openNoteDialog(it) { bind() }
        openPreferences()
    }

    override fun denyInfo() = denyWork()

    override fun denyInfoClose() = denyClose()

    override fun denyInfoRotateWork() = denyWork { rotate.switch() }

    override fun denyInfoRotateClose() = denyClose { rotate.switch() }

    private fun denyWork(before: () -> Unit = {}) = launchNotesItem(insert()) {
        repeat (times = 2) { _ ->
            openNoteDialog(it) { bind() }
            postPermission { deny() }
        }
        openNoteDialog(it) { bind() }
        postPermissionDeny {
            before()
            positive()
        }
    }

    private fun denyClose(before: () -> Unit = {}) = launchNotesItem(insert()) {
        repeat (times = 2) { _ ->
            openNoteDialog(it) { bind() }
            postPermission { deny() }
        }
        openNoteDialog(it) { bind() }
        postPermissionDeny {
            before()
            softClose()
        }

        /** Check previous one actually closed */
        openPreferences()
    }
}