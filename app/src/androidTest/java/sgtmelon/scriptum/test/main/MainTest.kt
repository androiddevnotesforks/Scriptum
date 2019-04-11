package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.main.RankScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Тест работы [MainActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val pageList = object : ArrayList<MainPage.Name>() {
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

        preference.firstStart = false
    }

    @Test fun startScreen() = launch {
        MainScreen { assert { onDisplayContent(MainPage.Name.NOTES) } }
    }

    @Test fun menuClickCorrectScreen() = launch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun addFabVisible() = launch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayFab(visible = page == MainPage.Name.NOTES) }
                }
            }
        }
    }

    @Test fun addDialogOpen() = launch {
        MainScreen {
            onClickFab()
            AddDialog { assert { onDisplayContent() } }
        }
    }

    @Test fun addDialogCreateTextNote() = launch {
        MainScreen {
            onClickFab()
            AddDialog { onClickItem(NoteType.TEXT) }

            TextNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun addDialogCreateRollNote() = launch {
        MainScreen {
            onClickFab()
            AddDialog { onClickItem(NoteType.ROLL) }

            RollNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun rankScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
            navigateTo(MainPage.Name.RANK)

            RankScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.Name.RANK)
        }
    }

    @Test fun notesScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            NotesScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.Name.NOTES)
        }
    }

    @Test fun binScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.Name.BIN)
        }
    }

}