package sgtmelon.scriptum.ui.auto.rank

import org.junit.Test
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.nextShortString

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest() {

    // TODO restore snackbar after returning to this page (test case: click cance -> open notes page -> open rank page -> check snackbar is visible)
    // TODO restore snackbar after app reopen (свернул-открыл)

    /**
     * Check insets-spacing for snackbar bottom.
     */
    @Test fun displayInsets() = startRankListTest {
        scrollTo(Scroll.END)
        repeat(times = 5) {
            itemCancel(last, isWait = true)
            assertSnackbarDismissed()
        }
    }

    @Test fun singleActionClick() = startRankListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { clickCancel() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test fun manyActionClick() = startRankListTest(count = 3) {
        repeat(it.size) { itemCancel(p = 0) }
        repeat(it.size) { i ->
            snackbar {
                clickCancel()
                if (i != it.lastIndex) {
                    assert()
                }
            }

        }

        assertSnackbarDismissed()
        assertList(it)
    }

    //region Cleanup

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
                    assertList(it)
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

                    assertList(list)
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
                    assertList(list)
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
                    assertList(list)
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
                    assertList(list)
                    openRenameDialog(name, p = count - 1)
                }
            }
        }
    }

    //endregion

    // TODO common with notifications

    @Test fun scrollTopAfterAction() = startRankListTest {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismissed()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }

    // TODO common with notifications

    @Test fun scrollBottomAfterAction() = startRankListTest {
        val p = it.lastIndex

        itemCancel(p)
        scrollTo(Scroll.START)
        snackbar { clickCancel() }
        await(RecyclerItemPart.SCROLL_TIME)

        assertSnackbarDismissed()
        RecyclerItemPart.PREVENT_SCROLL = true
        assertItem(it[p], p)
    }
}