package sgtmelon.scriptum.ui.auto.rank

import org.junit.Test
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.ListCancelSnackbarCase
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.nextShortString

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest(),
    ListCancelSnackbarCase {

    @Test override fun displayInsets() = startRankListTest { startDisplayInserts(screen = this) }

    @Test override fun singleActionClick() = startRankListTest(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun manyActionClick() = startRankListTest(count = 3) {
        repeat(it.size) { itemCancel(p = 0) }
        repeat(it.size) { i ->
            snackbar {
                action()
                if (i != it.lastIndex) {
                    assert()
                }
            }
        }

        assertSnackbarDismissed()
        assertList(it)
    }

    @Test override fun scrollTopAfterAction() = startRankListTest {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun scrollBottomAfterAction() = startRankListTest {
        val p = it.lastIndex

        itemCancel(p)
        scrollTo(Scroll.START)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun restoreAfterPause() = db.fillRank().let {
        val p = it.indices.random()

        launch {
            mainScreen {
                openRank {
                    itemCancel(p)
                    snackbar { assert() }
                    openNotes(isEmpty = true) { assertSnackbarDismissed() }
                }

                openRank {
                    snackbar {
                        assert()
                        action()
                    }
                    assertSnackbarDismissed()
                    assertItem(it[p], p)
                }
            }
        }
    }

    @Test override fun clearCacheOnDismiss() = db.fillRank().let {
        val p = it.indices.random()

        launch {
            mainScreen {
                openRank {
                    itemCancel(p)
                    snackbar { action() }
                    assertSnackbarDismissed()
                }
                openNotes(isEmpty = true)
                openRank { assertSnackbarDismissed() }
            }
        }
    }

    @Test override fun dismissTimeout() = db.fillRank().let {
        val p = it.indices.random()

        launch {
            mainScreen {
                openRank {
                    itemCancel(p)
                    await(SnackbarPart.DISMISS_TIME)
                    assertSnackbarDismissed()
                }
                openNotes(isEmpty = true)
                openRank { assertSnackbarDismissed() }
            }
        }
    }

    @Test fun dismissOnDrag() {
        TODO()
    }

    @Test fun dismissOnRename() = db.fillRank(count = 2).let { list ->
        startRankTest {
            itemCancel(p = 0)
            list.removeAt(0)

            snackbar { assert() }
            openRenameDialog(list[0].name, p = 0) { cancel() }

            assertSnackbarDismissed()
            assertList(list)
        }
    }

    @Test fun dismissOnAddStart() = db.fillRank(count = 2).let { list ->
        val name = nextShortString()

        startRankTest {
            toolbar { enter(name) }
            itemCancel(p = 0)
            list.removeAt(0)

            snackbar { assert() }
            smallLongPressTime {
                toolbar { addToStart() }
            }
            assertSnackbarDismissed()

            for ((i, item) in list.withIndex()) {
                assertItem(item, p = i + 1)
            }
            openRenameDialog(name, p = 0)
        }
    }

    @Test fun dismissOnAddEnd() = db.fillRank(count = 2).let { list ->
        val name = nextShortString()

        startRankTest {
            toolbar { enter(name) }
            itemCancel(p = 0)
            list.removeAt(0)

            snackbar { assert() }
            toolbar { addToEnd() }
            assertSnackbarDismissed()

            assertList(list)
            openRenameDialog(name, last)
        }
    }

    @Test fun dismissOnAddIme() = db.fillRank(count = 2).let { list ->
        val name = nextShortString()

        startRankTest {
            toolbar { enter(name) }
            itemCancel(p = 0)
            list.removeAt(0)

            snackbar { assert() }
            toolbar { imeClick() }
            assertSnackbarDismissed()

            assertList(list)
            openRenameDialog(name, last)
        }
    }
}