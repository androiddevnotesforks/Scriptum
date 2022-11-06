package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import org.junit.Test
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.cleanup.testData.Scroll
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch
import sgtmelon.test.common.nextShortString

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest() {

    /**
     * Check snackbar container display correct
     */
    @Test fun containerBottomDisplay() = db.fillRank(count = 15).let {
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

    @Test fun actionClickSingle() = db.fillRank(count = 5).let {
        val p = it.indices.random()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    getSnackbar { clickCancel() }
                    assertSnackbarDismiss()
                    onAssertItem(it[p], p)
                }
            }
        }
    }

    @Test fun actionClickMany() = db.fillRank(count = 3).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    repeat(list.size) { onClickCancel(p = 0) }
                    repeat(list.size) {
                        getSnackbar { clickCancel() }
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

    @Test fun actionClickDismiss() = db.fillRank(count = 3).let {
        val removePosition = 1

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(removePosition)
                    it.removeAt(removePosition)

                    onClickCancel(p = 1)
                    getSnackbar { clickCancel() }
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


    @Test fun dismissOnPause() = db.fillRank(count = 3).let { list ->
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

    @Test fun dismissOnRename() = db.fillRank(count = 2).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)

                    openRenameDialog(list[0].name, p = 0) { onClickCancel() }

                    assertSnackbarDismiss()
                    for ((i, item) in list.withIndex()) {
                        onAssertItem(item, i)
                    }
                }
            }
        }
    }

    @Test fun dismissOnAddStart() = db.fillRank(count = 2).let { list ->
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

    @Test fun dismissOnAddEnd() = db.fillRank(count = 2).let { list ->
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

    @Test fun dismissOnAddIme() = db.fillRank(count = 2).let { list ->
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


    @Test fun scrollToUndoItem_onTop() = db.fillRank(count = 15).let {
        val p = it.indices.first()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.END)
                    getSnackbar { clickCancel() }

                    assertSnackbarDismiss()

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    onAssertItem(it[p], p)
                }
            }
        }
    }

    @Test fun scrollToUndoItem_onBottom() = db.fillRank(count = 15).let {
        val p = it.lastIndex

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.START)
                    getSnackbar { clickCancel() }

                    assertSnackbarDismiss()

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    onAssertItem(it[p], p)
                }
            }
        }
    }

}