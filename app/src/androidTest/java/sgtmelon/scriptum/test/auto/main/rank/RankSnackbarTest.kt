package sgtmelon.scriptum.test.auto.main.rank

import org.junit.Test
import sgtmelon.extension.nextShortString
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest() {

    /**
     * Check snackbar container display correct
     */
    @Test fun containerBottomDisplay() = data.fillRank(count = 15).let {
        launch {
            mainScreen {
                rankScreen {
                    onScroll(Scroll.END)
                    repeat(times = 5) {
                        onClickCancel(last, isWait = true)
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
                    onAssertItem(it[p], p)
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

                    for ((i , item) in list.withIndex()) {
                        onAssertItem(item, i)
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

                notesScreen(isEmpty = true)

                rankScreen {
                    assertSnackbarDismiss()

                    for ((i , item) in it.withIndex()) {
                        onAssertItem(item, i)
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

                notesScreen(isEmpty = true)
                rankScreen {
                    assertSnackbarDismiss()

                    for ((i, item) in list.withIndex()) {
                        onAssertItem(item, i)
                    }
                    repeat(list.size) { onClickCancel(p = 0) }
                }

                notesScreen(isEmpty = true)
                rankScreen(isEmpty = true) { assertSnackbarDismiss() }
            }
        }
    }

    // TODO finish test
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
                    for ((i , item) in list.withIndex()) {
                        onAssertItem(item, i)
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
                    for ((i , item) in list.withIndex()) {
                        onAssertItem(item, p = i + 1)
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
                    for ((i , item) in list.withIndex()) {
                        onAssertItem(item, i)
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
                    for ((i , item) in list.withIndex()) {
                        onAssertItem(item, i)
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
                    onAssertItem(it[p], p)
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
                    onAssertItem(it[p], p)
                }
            }
        }
    }

}