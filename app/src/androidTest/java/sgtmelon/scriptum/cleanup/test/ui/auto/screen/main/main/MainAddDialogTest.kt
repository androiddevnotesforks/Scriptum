package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test add dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainAddDialogTest : ParentUiTest() {

    @Test fun dialogClose() = launch {
        mainScreen {
            openAddDialog { onCloseSoft() }.assert()
            openAddDialog { onCloseSwipe() }.assert()
        }
    }

    @Test fun createTextNote() = db.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun createRollNote() = db.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

}