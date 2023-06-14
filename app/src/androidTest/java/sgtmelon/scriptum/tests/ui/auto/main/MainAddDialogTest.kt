package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase

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

    @Test fun createTextNote() = launchMain { openAddDialog { createText({ db.createText() }) } }

    @Test fun createRollNote() = launchMain { openAddDialog { createRoll({ db.createRoll() }) } }

}