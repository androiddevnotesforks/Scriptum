package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

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

        prefUtils.firstStart = false
    }

    @Test fun contentDisplay() {
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent() } }
    }

    @Test fun navigationWork() {
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

    @Test fun rightFirstFragment() {
        testRule.launchActivity(Intent())

        MainScreen { assert { onDisplayContent(MainPage.Name.NOTES) } }
    }

    @Test fun rightFragmentPlacementAndFabVisibility() {
        testRule.launchActivity(Intent())

        MainScreen {
            repeat(times = 3) {
                for (page in pageList) {
                    navigateTo(page)
                    assert {
                        onDisplayFab(visible = page == MainPage.Name.NOTES)
                        onDisplayContent(page)
                    }
                }
            }
        }
    }

    @Test fun fabOpenAddDialog() {
        testRule.launchActivity(Intent())

        MainScreen {
            onClickFab()
            AddDialog { assert { onDisplayContent() } }
        }
    }

    @Test fun fabCreateTextNote() {
        testRule.launchActivity(Intent())

        MainScreen {
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
        testRule.launchActivity(Intent())

        MainScreen {
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

    fun scrollTopRank() {
        TODO("no tests")
    }

    @Test fun scrollTopNotes() {
        testData.apply {
            clearAllData()
            fillNotes(times = 20)
        }

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
        testData.apply {
            clearAllData()
            fillBin(times = 20)
        }

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