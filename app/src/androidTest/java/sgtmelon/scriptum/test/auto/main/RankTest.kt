package sgtmelon.scriptum.test.auto.main

import org.junit.Test
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RankFragment]
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
                openRankPage(empty = true) { toolbar { onEnterName(it) } }
            }
        }
    }

    @Test fun toolbarEnterClear() = launch {
        mainScreen {
            openRankPage(empty = true) {
                toolbar { onEnterName(data.uniqueString).onClickClear() }
            }
        }
    }

    @Test fun toolbarEnterAddStart() = data.uniqueString.let {
        launch({ data.insertRank() }) {
            mainScreen {
                openRankPage {
                    toolbar { onEnterName(it).onLongClickAdd() }
                    openRenameDialog(it, p = 0)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() = data.uniqueString.let {
        launch({ data.insertRank() }) {
            mainScreen {
                openRankPage {
                    toolbar { onEnterName(it).onClickAdd() }
                    openRenameDialog(it, p = count - 1)
                }
            }
        }
    }

    @Test fun toolbarUpdateOnRename() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    toolbar {
                        onEnterName(newName)
                        openRenameDialog(it.name) { onEnter(newName).onClickAccept() }
                        assert(isAddEnabled = false)
                    }
                }
            }
        }
    }

    /**
     * Rank Card
     */

    @Test fun rankVisibleForNotes() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                openNotesPage()
                openRankPage { onClickVisible() }
                openNotesPage(empty = true, hide = true)
                openRankPage { onClickVisible() }
                openNotesPage()
            }
        }
    }

    @Test fun rankVisibleForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible() }
                openBinPage()
                openRankPage { onClickVisible() }
                openBinPage()
            }
        }
    }

    @Test fun rankClearFromList() = data.insertRank().let {
        launch { mainScreen { openRankPage { onClickCancel().assert(empty = true) } } }
    }

    @Test fun rankClearForNote() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                openRankPage { onClickVisible() }
                openNotesPage(empty = true, hide = true)
                openRankPage { onClickCancel() }
                openNotesPage()
            }
        }
    }

    @Test fun rankClearForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible() }
                openBinPage()
                openRankPage { onClickCancel() }
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
                    openRenameDialog(it.name) { onCloseSoft() }.assert(empty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(empty = false)
                }
            }
        }
    }

    @Test fun renameDialogApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage { openRenameDialog(it.name) { onEnter(it.name, enabled = false) } }
            }
        }
    }

    @Test fun renameDialogApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun renameDialogApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                openRankPage { openRenameDialog(it.name) { onEnter(it.name.toUpperCase()) } }
            }
        }
    }

    @Test fun renameDialogResult() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) { onEnter(newName).onClickAccept() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}