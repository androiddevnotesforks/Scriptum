package sgtmelon.scriptum.test.ui.auto.screen.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.parent.ParentUiTest

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

    @Test fun createTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun createRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

}