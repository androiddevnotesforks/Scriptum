package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val listPage = object : ArrayList<MainPage.Name>() {
        init {
            add(MainPage.Name.RANK)
            add(MainPage.Name.NOTES)
            add(MainPage.Name.BIN)
            add(MainPage.Name.RANK)
            add(MainPage.Name.BIN)
            add(MainPage.Name.NOTES)
        }
    }

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
        testRule.launchActivity(Intent())
    }

    @Test fun navigationWork() {
        MainScreen {
            assert { onDisplayContent() }

            repeat(times = 3) {
                for (page in listPage) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun rightFirstFragment() {
        MainScreen {
            assert {
                onDisplayContent()
                onDisplayContent(MainPage.Name.NOTES)
            }
        }
    }

    @Test fun rightFragmentPlacementAndFabVisibility() {
        MainScreen {
            assert { onDisplayContent() }

            repeat(times = 3) {
                for (page in listPage) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun fabOpenAddDialog() {
        MainScreen {
            assert { onDisplayContent() }

            onClickFab()
            AddDialog { assert { onDisplayContent() } }
        }
    }

    @Test fun fabCreateTextNote() {
        MainScreen {
            assert { onDisplayContent() }

            onClickFab()
            AddDialog {
                assert { onDisplayContent() }
                onClickItem(NoteType.TEXT)
            }

            TextNoteScreen {
                assert { onDisplayContent(State.NEW) }
                closeSoftKeyboard()
                pressBack()
            }
        }
    }

    @Test fun fabCreateRollNote() {
        MainScreen {
            assert { onDisplayContent() }

            onClickFab()
            AddDialog {
                assert { onDisplayContent() }
                onClickItem(NoteType.ROLL)
            }

            RollNoteScreen {
                assert { onDisplayContent(State.NEW) }
                closeSoftKeyboard()
                pressBack()
            }
        }
    }

}