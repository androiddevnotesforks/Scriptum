package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
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
                        MainPage.RANK -> rankScreen { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.NOTES -> notesScreen { assert { onDisplayContent(empty = count == 0) } }
                        MainPage.BIN -> binScreen { assert { onDisplayContent(empty = count == 0) } }
                    }

                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun addFabVisible() = afterLaunch {
        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert { onDisplayFab(visible = page == MainPage.NOTES) }
                }
            }
        }
    }

    @Test fun addDialogOpen() = afterLaunch { MainScreen { addDialogUi() } }

    @Test fun addDialogCloseSoft() = afterLaunch {
        MainScreen {
            addDialogUi { onCloseSoft() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCloseSwipe() = afterLaunch {
        MainScreen {
            addDialogUi { onCloseSwipe() }
            assert { onDisplayContent() }
        }
    }

    @Test fun addDialogCreateTextNote() = afterLaunch {
        MainScreen {
            addDialogUi { onClickItem(NoteType.TEXT) }
            textNoteScreen()
        }
    }

    @Test fun addDialogCreateRollNote() = afterLaunch {
        MainScreen {
            addDialogUi { onClickItem(NoteType.ROLL) }
            rollNoteScreen()
        }
    }

    @Test fun rankScreenScrollTop() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
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
            binScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            scrollTop(MainPage.BIN)
        }
    }

}