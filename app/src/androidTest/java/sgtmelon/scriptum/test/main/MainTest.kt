package sgtmelon.scriptum.test.main

import android.content.Intent
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

    @Test fun correctScreenOnMenuClick() {
        testRule.launchActivity(Intent())

        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun startScreen() {
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent(MainPage.Name.NOTES) } }
    }

    @Test fun addFabVisibility() {
        testRule.launchActivity(Intent())

        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayFab(visible = page == MainPage.Name.NOTES) }
                }
            }
        }
    }

    @Test fun openAddDialog() {
        testRule.launchActivity(Intent())

        MainScreen {
            onClickFab()
            AddDialog { assert { onDisplayContent() } }
        }
    }

    @Test fun addDialogCreateTextNote() {
        testRule.launchActivity(Intent())

        MainScreen {
            onClickFab()
            AddDialog { onClickItem(NoteType.TEXT) }

            TextNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun addDialogCreateRollNote() {
        testRule.launchActivity(Intent())

        MainScreen {
            onClickFab()
            AddDialog { onClickItem(NoteType.ROLL) }

            RollNoteScreen { assert { onDisplayContent(State.NEW) } }
        }
    }

    @Test fun scrollTopRank() {
        testData.apply { clearAllData() }.fillRank(times = 20)

        testRule.launchActivity(Intent())

        MainScreen {
            navigateTo(MainPage.Name.RANK)

            RankScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.Name.RANK)
        }
    }

    @Test fun scrollTopNotes() {
        testData.apply { clearAllData() }.fillNotes(times = 20)

        testRule.launchActivity(Intent())

        MainScreen {
            NotesScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.Name.NOTES)
        }
    }

    @Test fun scrollTopBin() {
        testData.apply { clearAllData() }.fillBin(times = 20)

        testRule.launchActivity(Intent())

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