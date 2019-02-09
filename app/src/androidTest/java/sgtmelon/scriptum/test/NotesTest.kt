package sgtmelon.scriptum.test

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.PAGE
import sgtmelon.scriptum.ui.screen.note.roll.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.text.TextNoteScreen
import sgtmelon.scriptum.ui.widget.note.STATE

@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    private val listAddNoteType: List<NoteType> = object : ArrayList<NoteType>() {
        init {
            add(NoteType.TEXT)
            add(NoteType.ROLL)
            add(NoteType.ROLL)
            add(NoteType.TEXT)
        }
    }

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testAddDialog() {
        MainScreen {
            assert { onDisplayContent(PAGE.NOTES) }

            addDialog {
                open()
                assert { onDisplayContent() }
                pressBack()
            }

            for (noteType in listAddNoteType) {
                addDialog {
                    open()
                    onClickItem(noteType)
                }

                when (noteType) {
                    NoteType.TEXT -> {
                        TextNoteScreen {
                            assert { onDisplayContent(STATE.EDIT) }
                            closeSoftKeyboard()
                            pressBack()
                        }
                    }
                    NoteType.ROLL -> {
                        RollNoteScreen {
                            assert { onDisplayContent(STATE.EDIT) }
                            closeSoftKeyboard()
                            pressBack()
                        }
                    }
                }

                assert { onDisplayContent(PAGE.NOTES) }
            }
        }
    }

}