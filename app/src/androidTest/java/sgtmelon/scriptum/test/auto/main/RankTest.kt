package sgtmelon.scriptum.test.auto.main

import org.junit.Test
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Test for[RankFragment]
 */
class RankTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openRankPage(empty = true) } }

    @Test fun contentList() = launch({ data.fillRank() }) { mainScreen { openRankPage() } }

    @Test fun listScroll() = launch({ data.fillRank() }) {
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

    @Test fun toolbarEnterAddFromList() = data.insertRank().let {
        launch {
            mainScreen { openRankPage { toolbar { onEnterName(it.name, isAddEnabled = false) } } }
        }
    }

    @Test fun toolbarEnterAddEnabled() = data.uniqueString.let {
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
                    onEnterName(data.uniqueString, isAddEnabled = true)
                    onClickClear()
                }
            }
        }
    }

    @Test fun toolbarEnterAddStart() = data.uniqueString.let {
        launch({ data.insertRank() }) {
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

    @Test fun toolbarEnterAddEnd() = data.uniqueString.let {
        launch({ data.insertRank() }) {
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

    @Test fun rankVisibleAnimationClick() = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    onClickVisible(it)
                    wait(time = 500) { onClickVisible(it) }
                }
            }
        }
    }

    @Test fun rankVisibleAnimationLongClick() = data.fillRank(count = 5).let {
        launch {
            mainScreen {
                openRankPage { it.forEach { waitAfter(time = 300) { onLongClickVisible(it) } } }
            }
        }
    }

    @Test fun rankVisibleForNotes() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                openNotesPage()
                openRankPage { onClickVisible(it) }
                openNotesPage(empty = true, hide = true)
                openRankPage { onClickVisible(it) }
                openNotesPage()
            }
        }
    }

    @Test fun rankVisibleForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible(it) }
                openBinPage()
                openRankPage { onClickVisible(it) }
                openBinPage()
            }
        }
    }

    @Test fun rankClearFromList() = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    onClickCancel(it)
                    assert(empty = true)
                }
            }
        }
    }

    @Test fun rankClearForNote() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                openRankPage { onClickVisible(it) }
                openNotesPage(empty = true, hide = true)
                openRankPage { onClickCancel(it) }
                openNotesPage()
            }
        }
    }

    @Test fun rankClearForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible(it) }
                openBinPage()
                openRankPage { onClickCancel(it) }
                openBinPage()
            }
        }
    }

    /**
     * Rename Dialog
     */

    @Test fun renameDialogClose() = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onCloseSoft() }
                    assert(empty = false)
                    openRenameDialog(it.name) { onClickCancel() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun renameDialogApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage { openRenameDialog(it.name) { onReame(it.name, enabled = false) } }
            }
        }
    }

    @Test fun renameDialogApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it[0].name, p = 0) { onReame(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun renameDialogApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onReame(it.name.toUpperCase(), enabled = true) }
                }
            }
        }
    }

    @Test fun renameDialogResult() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onReame(newName, enabled = true).onClickAccept() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}