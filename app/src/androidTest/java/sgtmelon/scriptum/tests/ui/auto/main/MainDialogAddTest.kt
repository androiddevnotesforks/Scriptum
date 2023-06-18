package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.dialogs.sheet.AddSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.screen.dialogs.sheet.AddSheetDialogUi
import sgtmelon.scriptum.source.ui.screen.main.MainScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest

/**
 * Test add dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDialogAddTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    @Test override fun close() = launchMain {
        openAddDialog { softClose() }
        assert()

        openAddDialog { swipeClose() }
        assert()
    }

    @Test fun createTextNote() = launchMain { openAddDialog { createText({ db.createText() }) } }

    @Test fun createRollNote() = launchMain { openAddDialog { createRoll({ db.createRoll() }) } }

    @Test override fun rotateClose() = launchMain {
        assertRotationClose { softClose() }
        rotate.toNormal()
        assertRotationClose { swipeClose() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun MainScreen.assertRotationClose(closeDialog: AddSheetDialogUi.() -> Unit) {
        openAddDialog {
            rotate.toSide()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = launchMain {
        openAddDialog {
            rotate.toSide()

            /** Turn it back because of keyboard appears (on note screen) - it may fail test sometime. */
            rotate.toNormal()

            /** We check only click listener - doesn't matter what screen to open. */
            when (NoteType.values().random()) {
                NoteType.TEXT -> createText({ db.createText() })
                NoteType.ROLL -> createRoll({ db.createRoll() })
            }
        }
    }
}