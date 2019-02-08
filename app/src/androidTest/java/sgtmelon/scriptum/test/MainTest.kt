package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.Page
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testNavigation() {
        val listPage: List<Page> = object : ArrayList<Page>() {
            init {
                add(Page.RANK)
                add(Page.NOTES)
                add(Page.BIN)
                add(Page.RANK)
                add(Page.BIN)
                add(Page.NOTES)
                add(Page.RANK)
            }
        }

        MainScreen {
            assert {
                isSelected(Page.NOTES)
                onDisplayContent(showFab = true)
            }

            for (page in listPage) {
                navigateTo(page)
                assert {
                    isSelected(page)
                    onDisplayContent(page == Page.NOTES)
                }
            }
        }
    }

    @Test fun testContentEmpty() {
        db.clearAllTables()

        val listPage: List<Page> = object : ArrayList<Page>() {
            init {
                add(Page.RANK)
                add(Page.NOTES)
                add(Page.BIN)
                add(Page.RANK)
                add(Page.BIN)
                add(Page.NOTES)
                add(Page.RANK)
            }
        }

        MainScreen {
            assert { isSelected(Page.NOTES) }
            NotesScreen {
                assert {
                    onDisplayContent()
                    onDisplayInfo()
                }
            }

            for (page in listPage) {
                navigateTo(page)
                assert { isSelected(page) }

                when (page) {
                    Page.RANK -> RankScreen {
                        assert {
                            onDisplayContent()
                            onDisplayInfo()
                        }
                    }
                    Page.NOTES -> NotesScreen {
                        assert {
                            onDisplayContent()
                            onDisplayInfo()
                        }
                    }
                    Page.BIN -> BinScreen {
                        assert {
                            onDisplayContent()
                            onDisplayInfo()
                        }
                    }
                }
            }
        }
    }

}