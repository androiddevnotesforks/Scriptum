package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase

/**
 * Test add dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainAddDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = launchMain {
        openAddDialog { softClose() }
        assert()
        openAddDialog { swipeClose() }
        assert()
    }

    @Test fun createTextNote() = db.createText().let {
        launchMain { openAddDialog { createText(it) } }
    }

    @Test fun createRollNote() = db.createRoll().let {
        launchMain { openAddDialog { createRoll(it) } }
    }
}