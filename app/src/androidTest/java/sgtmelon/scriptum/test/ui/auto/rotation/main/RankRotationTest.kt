package sgtmelon.scriptum.test.ui.auto.rotation.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test of [RankFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RankRotationTest : ParentRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            rankScreen(isEmpty = true) {
                automator.rotateSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun contentList() = launch({ data.fillRank() }) {
        mainScreen {
            rankScreen {
                automator.rotateSide()
                assert(isEmpty = false)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun renameDialog() = data.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) {
                        onEnter(newName)
                        automator.rotateSide()
                        assert(newName)
                    }
                }
            }
        }
    }

    @Test fun snackbar() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    repeat(times = 3) { time ->
                        onClickCancel()

                        if (time % 2 == 0) {
                            automator.rotateSide()
                        } else {
                            automator.rotateNatural()
                        }

                        getSnackbar().onClickCancel()
                    }
                }
            }
        }
    }
}