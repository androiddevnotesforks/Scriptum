package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test of [RankFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RankRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            rankScreen(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun contentList() = launch({ db.fillRank() }) {
        mainScreen {
            rankScreen {
                rotate.toSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun renameDialog() = db.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) {
                        onEnter(newName)
                        rotate.toSide()
                        assert(newName)
                    }
                }
            }
        }
    }

    @Test fun snackbar() = db.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    repeat(times = 3) { time ->
                        itemCancel()

                        if (time.isDivideEntirely()) {
                            rotate.toSide()
                        } else {
                            rotate.toNormal()
                        }

                        getSnackbar().clickCancel()
                    }
                }
            }
        }
    }
}