package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы [MainActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val pageList = arrayListOf(
            MainPage.RANK, MainPage.NOTES, MainPage.BIN,
            MainPage.RANK, MainPage.BIN, MainPage.NOTES
    )

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
    }

    @Test fun startScreen() = launch { mainScreen { assert { onDisplayContent(MainPage.NOTES) } } }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen {
            repeat(times = 3) {
                pageList.forEach {
                    when (it) {
                        MainPage.RANK -> openRankPage { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.NOTES -> openNotesPage { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.BIN -> openBinPage { assert { onDisplayContent(empty = count == 0) } }
                    }

                    assert { onDisplayContent(it) }
                }
            }
        }
    }

    @Test fun addFabVisible() = launch {
        mainScreen {
            repeat(times = 3) {
                pageList.forEach {
                    onNavigateTo(it)
                    assert { onDisplayFab(visible = it == MainPage.NOTES) }
                }
            }
        }
    }

    /**
     * Add Dialog
     */

    @Test fun addDialogOpen() = launch { mainScreen { openAddDialog() } }

    @Test fun addDialogCloseSoft() = launch {
        mainScreen {
            openAddDialog { onCloseSoft() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCloseSwipe() = launch {
        mainScreen {
            openAddDialog { onCloseSwipe() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCreateTextNote() =
            launch { mainScreen { openAddDialog { createTextNote() } } }

    @Test fun addDialogCreateRollNote() =
            launch { mainScreen { openAddDialog { createRollNote() } } }

    /**
     * Page Scroll Top
     */

    @Test fun rankScreenScrollTop() = launch({ testData.clear().fillRank(times = 20) }) {
        mainScreen {
            openRankPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.RANK)
        }
    }

    @Test fun notesScreenScrollTop() = launch({ testData.clear().fillNotes(times = 20) }) {
        mainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.NOTES)
        }
    }

    @Test fun binScreenScrollTop() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.BIN)
        }
    }

}