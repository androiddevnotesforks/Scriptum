package sgtmelon.scriptum.test

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.ui.dialog.AddDialogUi
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.Page

@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun addNoteText() {
        MainScreen {
            assert { isSelected(Page.NOTES) }
            onClickAdd()

            AddDialogUi {
                assert { onDisplayContent() }
                onClickItem(NoteType.TEXT)
            }

            // TODO (Проверка экрана заметки)
            pressBack()
        }
    }

    @Test fun addNoteRoll() {
        MainScreen {
            assert { isSelected(Page.NOTES) }
            onClickAdd()

            AddDialogUi {
                assert { onDisplayContent() }
                onClickItem(NoteType.ROLL)
            }

            // TODO (Проверка экрана заметки)
            pressBack()
        }
    }

}