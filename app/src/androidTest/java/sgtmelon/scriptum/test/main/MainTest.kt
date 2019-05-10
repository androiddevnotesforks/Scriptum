package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

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

    @Test fun startScreen() = afterLaunch {
        MainScreen { assert { onDisplayContent(MainPage.NOTES) } }
    }

    @Test fun menuClickCorrectScreen() = afterLaunch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    when (page) {
                        MainPage.RANK -> openRankPage { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.NOTES -> openNotesPage { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.BIN -> openBinPage { assert { onDisplayContent(empty = count == 0) } }
                    }

                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun addFabVisible() = afterLaunch {
        MainScreen {
            repeat(times = 3) {
                pageList.forEach {
                    onNavigateTo(it)
                    assert { onDisplayFab(visible = it == MainPage.NOTES) }
                }
            }
        }
    }


    @Test fun addDialogOpen() = afterLaunch { MainScreen { openAddDialog() } }

    @Test fun addDialogCloseSoft() = afterLaunch {
        MainScreen {
            openAddDialog { onCloseSoft() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCloseSwipe() = afterLaunch {
        MainScreen {
            openAddDialog { onCloseSwipe() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCreateTextNote() = afterLaunch {
        MainScreen { openAddDialog { createTextNote() } }
    }

    @Test fun addDialogCreateRollNote() = afterLaunch {
        MainScreen { openAddDialog { createRollNote() } }
    }


    @Test fun rankScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
            openRankPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.RANK)
        }
    }

    @Test fun notesScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.NOTES)
        }
    }

    @Test fun binScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            onScrollTop(MainPage.BIN)
        }
    }

}