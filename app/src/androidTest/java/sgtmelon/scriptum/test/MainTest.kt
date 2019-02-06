package sgtmelon.scriptum.test

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.PAGE
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

@LargeTest
class MainTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    @Test fun testNavigation() {
        MainScreen {
            assert {
                isSelected(PAGE.NOTES)
                onDisplayContent(showFab = true)
            }

            navigateTo(PAGE.RANK)
            assert {
                isSelected(PAGE.RANK)
                onDisplayContent(showFab = false)
            }

            navigateTo(PAGE.NOTES)
            assert {
                isSelected(PAGE.NOTES)
                onDisplayContent(showFab = true)
            }

            navigateTo(PAGE.BIN)
            assert {
                isSelected(PAGE.BIN)
                onDisplayContent(showFab = false)
            }
        }
    }

    @Test fun testContentEmpty() {
        db.clearAllTables()

        MainScreen {
            assert { isSelected(PAGE.NOTES) }
            NotesScreen {
                assert {
                    onDisplayContent()
                    onDisplayInfo()
                }
            }

            navigateTo(PAGE.RANK)
            assert { isSelected(PAGE.RANK) }
            RankScreen {
                assert {
                    onDisplayContent()
                    onDisplayInfo()
                }
            }

            navigateTo(PAGE.NOTES)
            assert { isSelected(PAGE.NOTES) }
            NotesScreen {
                assert {
                    onDisplayContent()
                    onDisplayInfo()
                }
            }

            navigateTo(PAGE.BIN)
            assert { isSelected(PAGE.BIN) }
            BinScreen {
                assert {
                    onDisplayContent()
                    onDisplayInfo()
                }
            }
        }
    }

}