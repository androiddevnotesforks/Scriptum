package sgtmelon.scriptum.test.auto.main

import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RankFragment]
 */
class RankTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { rankScreen(empty = true) } }

    @Test fun contentList() = launch({ data.fillRank() }) { mainScreen { rankScreen() } }

    @Test fun listScroll() = launch({ data.fillRank() }) {
        mainScreen { rankScreen { onScrollThrough() } }
    }

    /**
     * Toolbar
     */

    @Test fun toolbarEnterAddEmpty() = launch {
        mainScreen {
            rankScreen(empty = true) { toolbar { onEnterName(name = " ", addEnabled = false) } }
        }
    }

    @Test fun toolbarEnterAddFromList() = data.insertRank().let {
        launch {
            mainScreen { rankScreen { toolbar { onEnterName(it.name, addEnabled = false) } } }
        }
    }

    @Test fun toolbarEnterAddEnabled() = data.uniqueString.let {
        launch {
            mainScreen {
                rankScreen(empty = true) { toolbar { onEnterName(it) } }
            }
        }
    }

    @Test fun toolbarEnterClear() = launch {
        val name = data.uniqueString

        mainScreen {
            rankScreen(empty = true) {
                toolbar {
                    onEnterName(data.uniqueString).onClickClear()
                    onEnterName(name).onClickAdd()
                }
                openRenameDialog(name) { onCloseSoft() }
            }
        }
    }

    @Test fun toolbarEnterAddOnEmpty() = data.uniqueString.let {
        launch {
            mainScreen {
                rankScreen(empty = true){
                    toolbar { onEnterName(it).onClickAdd() }
                    openRenameDialog(it, p = 0) { onCloseSoft() }

                    onClickCancel(p = 0)

                    toolbar { onEnterName(it).onLongClickAdd() }
                    openRenameDialog(it, p = 0)
                }
            }
        }
    }

    @Test fun toolbarEnterAddStart() = data.uniqueString.let {
        launch({ data.fillRank() }) {
            mainScreen {
                rankScreen {
                    onScroll(Scroll.END)

                    toolbar { onEnterName(it).onLongClickAdd() }
                    openRenameDialog(it, p = 0) { onCloseSoft() }

                    onClickCancel(p = 0)

                    toolbar { onEnterName(it).onLongClickAdd() }
                    openRenameDialog(it, p = 0)
                }
            }
        }
    }

    @Test fun toolbarEnterAddEnd() = data.uniqueString.let {
        launch({ data.fillRank() }) {
            mainScreen {
                rankScreen {
                    toolbar { onEnterName(it).onClickAdd() }
                    openRenameDialog(it, p = count - 1) { onCloseSoft() }

                    onClickCancel(p = count - 1)

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
                rankScreen {
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
                notesScreen()
                rankScreen { onClickVisible() }
                notesScreen(empty = true, hide = true)
                rankScreen { onClickVisible() }
                notesScreen()
            }
        }
    }

    @Test fun rankVisibleForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
            }
        }
    }

    @Test fun rankClearFromList() = data.insertRank().let {
        launch { mainScreen { rankScreen { onClickCancel().assert(empty = true) } } }
    }

    @Test fun rankClearForNote() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onClickVisible() }
                notesScreen(empty = true, hide = true)
                rankScreen { onClickCancel() }
                notesScreen()
            }
        }
    }

    @Test fun rankClearForBin() = data.insertRankForBin().let {
        launch {
            mainScreen {
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
                rankScreen { onClickCancel() }
                binScreen()
            }
        }
    }

    /**
     * Rename Dialog
     */

    @Test fun renameDialogClose() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onCloseSoft() }.assert(empty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(empty = false)
                }
            }
        }
    }

    @Test fun renameDialogApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name, enabled = false) } }
            }
        }
    }

    @Test fun renameDialogApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun renameDialogApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name.toUpperCase()) } }
            }
        }
    }

    @Test fun renameDialogResult() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onEnter(newName).onClickAccept() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}