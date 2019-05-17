package sgtmelon.scriptum.test.main

import androidx.test.espresso.Espresso.closeSoftKeyboard
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }


    @Test fun contentEmpty() = launch({ testData.clear() }) {
        mainScreen { openRankPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() = launch({ testData.clear().fillRank() }) {
        mainScreen { openRankPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() = launch({ testData.clear().fillRank(times = 20) }) {
        mainScreen {
            openRankPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    /**
     * Toolbar
     */

    @Test fun toolbarEnterAddEmpty() = launch({ testData.clear() }) {
        mainScreen {
            openRankPage {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(name = " ")
                    assert { isAddEnabled(enabled = false) }
                }
            }
        }
    }

    @Test fun toolbarEnterAddFromList() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    toolbar {
                        assert { isAddEnabled(enabled = false) }
                        onEnterName(rankItem.name)
                        assert { isAddEnabled(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnabled() = launch({ testData.clear() }) {
        mainScreen {
            openRankPage {
                toolbar {
                    assert { isAddEnabled(enabled = false) }
                    onEnterName(testData.rankItem.name)
                    assert { isAddEnabled(enabled = true) }
                }
            }
        }
    }

    @Test fun toolbarEnterClear() = launch({ testData.clear() }) {
        mainScreen {
            openRankPage {
                toolbar {
                    assert { isClearEnabled(enabled = false) }
                    onEnterName(testData.rankItem.name)
                    assert { isClearEnabled(enabled = true) }
                    onClickClear()
                    assert { isClearEnabled(enabled = false) }
                }
            }
        }
    }

    @Test fun toolbarEnterAddStart() {
        val name = testData.apply {
            clear()
            insertRank()
        }.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(name)
                        onLongClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    openRenameDialog(name)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() {
        val name = testData.apply {
            clear()
            insertRank()
        }.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(name)
                        onClickAdd()
                    }

                    assert { onDisplayContent(empty = false) }
                    openRenameDialog(name, p = count - 1)
                }
            }
        }
    }

    /**
     * Rank Card
     */

    @Test fun rankVisibleAnimationClick() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    onClickVisible(rankItem.name)
                    waitBefore(time = 500) { onClickVisible(rankItem.name) }
                }
            }
        }
    }

    @Test fun rankVisibleAnimationLongClick() {
        val rankList = testData.clear().fillRank(times = 5)

        launch {
            mainScreen {
                openRankPage {
                    onLongClickVisible(rankList[0].name)
                    waitBefore(time = 500) { onLongClickVisible(rankList[count - 1].name) }
                }
            }
        }
    }

    @Test fun rankVisibleForNotes() {
        val rankList = testData.clear().insertRankToBin()

        launch {
            mainScreen {
                openRankPage { onClickVisible(rankList[1].name) }
                openBinPage { assert { onDisplayContent(empty = false) } }
                openRankPage { onClickVisible(rankList[0].name) }
                openBinPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }

    @Test fun rankVisibleForBin() {

    }

    @Test fun rankClearFromList() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    onClickCancel(rankItem.name)
                    assert { onDisplayContent(empty = true) }
                }
            }
        }
    }

    @Test fun rankClearForNote() {
        val rankList = testData.clear().insertRankToNotes()

        launch {
            mainScreen {
                openRankPage { onClickVisible(rankList[0].name) }
                openNotesPage { assert { onDisplayContent(empty = true) } }
                openRankPage { onClickCancel(rankList[0].name) }
                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    /**
     * Rename Dialog
     */

    @Test fun renameDialogOpen() {
        val rankItem = testData.clear().insertRank()

        launch { mainScreen { openRankPage { openRenameDialog(rankItem.name) } } }
    }

    @Test fun renameDialogCloseSoft() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        closeSoftKeyboard()
                        onCloseSoft()
                    }

                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogCloseCancel() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) { onClickCancel() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplySameName() {
        val rankItem = testData.clear().insertRank()

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        onEnterName(rankItem.name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogBlockApplyFromList() {
        val listRank = testData.clear().fillRank(times = 2)

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(listRank[0].name) {
                        onEnterName(listRank[1].name)
                        assert { isAcceptEnable(enabled = false) }
                    }
                }
            }
        }
    }

    @Test fun renameDialogResult() {
        val rankItem = testData.clear().insertRank()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        onEnterName(newName)
                        assert { isAcceptEnable(enabled = true) }
                        onClickAccept()
                    }

                    assert { onDisplayContent(empty = false) }

                    openRenameDialog(newName)
                }
            }
        }
    }

}