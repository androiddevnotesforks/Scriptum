package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import org.junit.Test
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextShortString

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest() {

    // TODO restore snackbar after returning to this page (test case: click cance -> open notes page -> open rank page -> check snackbar is visible)
    // TODO restore snackbar after app reopen (свернул-открыл)

    /**
     * Check snackbar container display correct
     */
    @Test fun containerBottomDisplay() = db.fillRank(count = 15).let {
        launch {
            mainScreen {
                openRank {
                    scrollTo(Scroll.END)
                    repeat(times = 5) {
                        itemCancel(last, isWait = true)
                        assertSnackbarDismissed()
                    }
                }
            }
        }
    }

    @Test fun actionClickSingle() = db.fillRank(count = 5).let {
        val p = it.indices.random()

        launch {
            mainScreen {
                openRank {
                    itemCancel(p)
                    snackbar { clickCancel() }
                    assertSnackbarDismissed()
                    assertItem(it[p], p)
                }
            }
        }
    }

    @Test fun actionClickMany() = db.fillRank(count = 3).let { list ->
        launch {
            mainScreen {
                openRank {
                    repeat(list.size) { itemCancel(p = 0) }
                    repeat(list.size) {
                        snackbar {
                            // TODO check how it was in notifications test
                            clickCancel()
                            if (it != list.lastIndex) {
                                assert()
                            }
                        }

                    }

                    assertSnackbarDismissed()

                    for ((i , item) in list.withIndex()) {
                        assertItem(item, i)
                    }
                }
            }
        }
    }

    @Test fun actionClickDismiss() = db.fillRank(count = 3).let {
        val removePosition = 1

        launch {
            mainScreen {
                openRank {
                    itemCancel(removePosition)
                    it.removeAt(removePosition)

                    itemCancel(p = 1)
                    snackbar { clickCancel() }
                    snackbar().assert()
                    openRenameDialog(it[1].name, p = 1) { cancel() }
                }

                openNotes(isEmpty = true)

                openRank {
                    assertSnackbarDismissed()

                    for ((i, item) in it.withIndex()) {
                        assertItem(item, i)
                    }
                }
            }
        }
    }


    @Test fun dismissOnPause() = db.fillRank(count = 3).let { list ->
        launch {
            mainScreen {
                openRank {
                    itemCancel(p = 0)
                    list.removeAt(0)
                }

                openNotes(isEmpty = true)
                openRank {
                    assertSnackbarDismissed()

                    for ((i, item) in list.withIndex()) {
                        assertItem(item, i)
                    }
                    repeat(list.size) { itemCancel(p = 0) }
                }

                openNotes(isEmpty = true)
                openRank(isEmpty = true) { assertSnackbarDismissed() }
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
                openRank {
                    itemCancel(p = 0)
                    list.removeAt(0)

                    openRenameDialog(list[0].name, p = 0) { cancel() }

                    assertSnackbarDismissed()
                    for ((i, item) in list.withIndex()) {
                        assertItem(item, i)
                    }
                }
            }
        }
    }

    @Test fun dismissOnAddStart() = db.fillRank(count = 2).let { list ->
        val name = nextShortString()

        launch {
            mainScreen {
                openRank {
                    itemCancel(p = 0)
                    list.removeAt(0)

                    toolbar { enter(name).addToStart() }

                    assertSnackbarDismissed()
                    for ((i, item) in list.withIndex()) {
                        assertItem(item, p = i + 1)
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
                openRank {
                    itemCancel(p = 0)
                    list.removeAt(0)

                    toolbar { enter(name).addToEnd() }

                    assertSnackbarDismissed()
                    for ((i, item) in list.withIndex()) {
                        assertItem(item, i)
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
                openRank {
                    itemCancel(p = 0)
                    list.removeAt(0)

                    toolbar { enter(name).imeClick() }

                    assertSnackbarDismissed()
                    for ((i, item) in list.withIndex()) {
                        assertItem(item, i)
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
                openRank {
                    itemCancel(p)
                    scrollTo(Scroll.END)
                    snackbar { clickCancel() }

                    assertSnackbarDismissed()

                    RecyclerItemPart.PREVENT_SCROLL = true
                    assertItem(it[p], p)
                }
            }
        }
    }

    @Test fun scrollToUndoItem_onBottom() = db.fillRank(count = 15).let {
        val p = it.lastIndex

        launch {
            mainScreen {
                openRank {
                    itemCancel(p)
                    scrollTo(Scroll.START)
                    snackbar { clickCancel() }

                    assertSnackbarDismissed()

                    RecyclerItemPart.PREVENT_SCROLL = true
                    assertItem(it[p], p)
                }
            }
        }
    }

}