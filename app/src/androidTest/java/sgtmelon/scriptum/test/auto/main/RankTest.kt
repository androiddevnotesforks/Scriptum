package sgtmelon.scriptum.test.auto.main

import org.junit.Test
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Тест работы [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openRankPage(empty = true) } }

    @Test fun contentList() = launch({ testData.fillRank() }) { mainScreen { openRankPage() } }

    @Test fun listScroll() = launch({ testData.fillRank() }) {
        mainScreen { openRankPage { onScrollThrough() } }
    }

    /**
     * Toolbar
     */

    @Test fun toolbarEnterAddEmpty() = launch {
        mainScreen {
            openRankPage(empty = true) { toolbar { onEnterName(name = " ", isAddEnabled = false) } }
        }
    }

    @Test fun toolbarEnterAddFromList() = testData.insertRank().let {
        launch {
            mainScreen { openRankPage { toolbar { onEnterName(it.name, isAddEnabled = false) } } }
        }
    }

    @Test fun toolbarEnterAddEnabled() = testData.uniqueString.let {
        launch {
            mainScreen {
                openRankPage(empty = true) { toolbar { onEnterName(it, isAddEnabled = true) } }
            }
        }
    }

    @Test fun toolbarEnterClear() = launch {
        mainScreen {
            openRankPage(empty = true) {
                toolbar {
                    onEnterName(testData.uniqueString, isAddEnabled = true)
                    onClickClear()
                }
            }
        }
    }

    @Test fun toolbarEnterAddStart() = testData.uniqueString.let {
        launch({ testData.insertRank() }) {
            mainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(it, isAddEnabled = true)
                        onLongClickAdd()
                    }

                    openRenameDialog(it, p = 0)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() = testData.uniqueString.let {
        launch({ testData.insertRank() }) {
            mainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(it, isAddEnabled = true)
                        onClickAdd()
                    }

                    openRenameDialog(it, p = count - 1)
                }
            }
        }
    }

    /**
     * Rank Card
     */

    @Test fun rankVisibleAnimationClick() = testData.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    onClickVisible(it)
                    wait(time = 500) { onClickVisible(it) }
                }
            }
        }
    }

    @Test fun rankVisibleAnimationLongClick() = testData.fillRank(count = 5).let {
        launch {
            mainScreen {
                openRankPage { it.forEach { waitAfter(time = 300) { onLongClickVisible(it) } } }
            }
        }
    }

    @Test fun rankVisibleForNotes() = testData.fillRankForNotes().let {
        launch {
            mainScreen {
                openNotesPage()
                openRankPage { onClickVisible(it[1]) }
                openNotesPage()
                openRankPage { onClickVisible(it[0]) }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun rankVisibleForBin() = testData.fillRankForBin().let {
        launch {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible(it[1]) }
                openBinPage()
                openRankPage { onClickVisible(it[0]) }
                openBinPage(empty = true)
            }
        }
    }

    @Test fun rankClearFromList() = testData.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    onClickCancel(it)
                    assert(empty = true)
                }
            }
        }
    }

    @Test fun rankClearForNote() = testData.fillRankForNotes().let {
        launch {
            mainScreen {
                openRankPage { onClickVisible(it[0]) }
                openNotesPage(empty = true)
                openRankPage { onClickCancel(it[0]) }
                openNotesPage()
            }
        }
    }

    /**
     * Rename Dialog
     */

    @Test fun renameDialogOpen() = testData.insertRank().let {
        launch { mainScreen { openRankPage { openRenameDialog(it.name) } } }
    }

    @Test fun renameDialogCloseSoft() = testData.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun renameDialogCloseCancel() = testData.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onClickCancel() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun renameDialogBlockApplySameName() = testData.insertRank().let {
        launch {
            mainScreen {
                openRankPage { openRenameDialog(it.name) { onEnterName(it.name, enabled = false) } }
            }
        }
    }

    @Test fun renameDialogBlockApplyFromList() = testData.fillRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it[0].name, p = 0) { onEnterName(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun renameDialogResult() = testData.insertRank().let {
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) {
                        onEnterName(newName, enabled = true)
                        onClickAccept()
                    }

                    openRenameDialog(newName)
                }
            }
        }
    }

}