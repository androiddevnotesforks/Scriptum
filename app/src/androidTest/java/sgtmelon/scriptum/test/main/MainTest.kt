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
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Тест работы [MainActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val pageList = object : ArrayList<MainPage>() {
        init {
            add(MainPage.RANK)
            add(MainPage.NOTES)
            add(MainPage.BIN)
            add(MainPage.RANK)
            add(MainPage.BIN)
            add(MainPage.NOTES)
        }
    }

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun startScreen() = launch { MainScreen { assert { onDisplayContent(MainPage.NOTES) } } }

    @Test fun menuClickCorrectScreen() = launch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)

                    assert { onDisplayContent(page) }

                    when (page) {
                        MainPage.RANK -> rankScreen { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.NOTES -> notesScreen { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.BIN -> binScreen { assert { onDisplayContent(empty = count == 0) } }
                    }

                }
            }
        }
    }

    @Test fun addFabVisible() = launch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayFab(visible = page == MainPage.NOTES) }
                }
            }
        }
    }

    @Test fun addDialogOpen() = launch {
        MainScreen {
            onClickFab()
            addDialog { assert { onDisplayContent() } }
        }
    }

    @Test fun addDialogCloseSoft() = launch {
        MainScreen {
            onClickFab()
            addDialog { onCloseSoft() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCloseSwipe() = launch {
        MainScreen {
            onClickFab()
            addDialog { onCloseSwipe() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCreateTextNote() = launch {
        MainScreen {
            onClickFab()
            addDialog { onClickItem(NoteType.TEXT) }

            TextNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun addDialogCreateRollNote() = launch {
        MainScreen {
            onClickFab()
            addDialog { onClickItem(NoteType.ROLL) }

            RollNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun rankScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
            navigateTo(MainPage.RANK)

            rankScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.RANK)
        }
    }

    @Test fun notesScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.NOTES)
        }
    }

    @Test fun binScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.BIN)
        }
    }

}