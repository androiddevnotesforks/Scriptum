package sgtmelon.scriptum.test.auto.main.rank

import org.junit.Test
import sgtmelon.extension.nextShortString
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest() {

    @Test fun containerBottomDisplay() = data.fillRank(count = 15).let {
        launch {
            mainScreen {
                rankScreen {
                    onScroll(Scroll.END)
                    repeat(times = 5) {
                        onClickCancel(last, wait = true)
                        assertSnackbarDismiss()
                    }
                }
            }
        }
    }

    @Test fun actionClick_single() = data.fillRank(count = 5).let {
        val p = it.indices.random()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    getSnackbar { onClickCancel() }
                    assertSnackbarDismiss()
                    openRenameDialog(it[p].name, p)
                }
            }
        }
    }

    @Test fun actionClick_many() = data.fillRank(count = 3).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    repeat(list.size) { onClickCancel(p = 0) }
                    repeat(list.size) {
                        getSnackbar { onClickCancel() }
                        if (it != list.lastIndex) {
                            getSnackbar { assert() }
                        }
                    }

                    assertSnackbarDismiss()

                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                }
            }
        }
    }

    @Test fun actionClick_dismiss() = data.fillRank(count = 3).let {
        val removePosition = 1

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(removePosition)
                    it.removeAt(removePosition)

                    onClickCancel(p = 1)
                    getSnackbar { onClickCancel() }
                    getSnackbar { assert() }
                    openRenameDialog(it[1].name, p = 1) { onClickCancel() }
                }

                notesScreen(empty = true)

                rankScreen {
                    assertSnackbarDismiss()
                    it.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                }
            }
        }
    }


    @Test fun dismissOnPause() = data.fillRank(count = 3).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)
                }

                notesScreen(empty = true)
                rankScreen {
                    assertSnackbarDismiss()

                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                    repeat(list.size) { onClickCancel(p = 0) }
                }

                notesScreen(empty = true)
                rankScreen(empty = true) { assertSnackbarDismiss() }
            }
        }
    }

    @Test fun dismissOnDrag() {
        TODO()
    }

    @Test fun dismissOnRename() = data.fillRank(count = 2).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)

                    openRenameDialog(list[0].name, p = 0) { onClickCancel() }

                    assertSnackbarDismiss()
                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                }
            }
        }
    }

    @Test fun dismissOnAddStart() = data.fillRank(count = 2).let { list ->
        val name = nextShortString()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)

                    toolbar { onEnterName(name).onLongClickAdd() }

                    assertSnackbarDismiss()
                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, p = i + 1) { onCloseSoft() }
                    }
                    openRenameDialog(name, p = 0)
                }
            }
        }
    }

    @Test fun dismissOnAddEnd() = data.fillRank(count = 2).let { list ->
        val name = nextShortString()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)

                    toolbar { onEnterName(name).onClickAdd() }

                    assertSnackbarDismiss()
                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                    openRenameDialog(name, p = count - 1)
                }
            }
        }
    }

    @Test fun dismissOnAddIme() = data.fillRank(count = 2).let { list ->
        val name = nextShortString()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)

                    toolbar { onEnterName(name).onImeOptionClick() }

                    assertSnackbarDismiss()
                    list.forEachIndexed { i, item ->
                        openRenameDialog(item.name, i) { onCloseSoft() }
                    }
                    openRenameDialog(name, p = count - 1)
                }
            }
        }
    }


    @Test fun scrollToUndoItem_onTop() = data.fillRank(count = 15).let {
        val p = it.indices.first()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.END)
                    getSnackbar { onClickCancel() }

                    assertSnackbarDismiss()

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    openRenameDialog(it[p].name, p)
                }
            }
        }
    }

    @Test fun scrollToUndoItem_onBottom() = data.fillRank(count = 15).let {
        val p = it.lastIndex

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.START)
                    getSnackbar { onClickCancel() }

                    assertSnackbarDismiss()

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    openRenameDialog(it[p].name, p)
                }
            }
        }
    }

}