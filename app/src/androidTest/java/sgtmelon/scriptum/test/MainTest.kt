package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.PAGE
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val listPage: List<PAGE> = object : ArrayList<PAGE>() {
        init {
            add(PAGE.RANK)
            add(PAGE.NOTES)
            add(PAGE.BIN)
            add(PAGE.RANK)
            add(PAGE.BIN)
            add(PAGE.NOTES)
            add(PAGE.RANK)
        }
    }

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testNavigation() {
        MainScreen {
            assert {
                isSelected(PAGE.NOTES)
                onDisplayContent(PAGE.NOTES)
            }

            for (page in listPage) {
                navigateTo(page)
                assert {
                    isSelected(page)
                    onDisplayContent(page)
                }
            }
        }
    }

    @Test fun testContentEmpty() {
        db.clearAllTables()

        MainScreen {
            assert { isSelected(PAGE.NOTES) }
            NotesScreen {
                assert {
                    onDisplayContent() // TODO(Отображение списка/информации от bool) (объединить методы)
                    onDisplayInfo()
                }
            }

            for (page in listPage) {
                navigateTo(page)
                assert { isSelected(page) }

                when (page) {
                    PAGE.RANK -> RankScreen {
                        assert {
                            onDisplayContent() // TODO(Отображение списка/информации от bool) (объединить методы)
                            onDisplayInfo()
                        }
                    }
                    PAGE.NOTES -> NotesScreen {
                        assert {
                            onDisplayContent()
                            onDisplayInfo()
                        }
                    }
                    PAGE.BIN -> BinScreen {
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