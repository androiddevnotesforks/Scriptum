package sgtmelon.scriptum.ui.auto.rank

import org.junit.Test
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.parent.ui.tests.launchRankList
import sgtmelon.scriptum.ui.cases.list.ListCancelSnackbarCase
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.nextShortString

/**
 * Test for Snackbar in [RankFragment].
 */
class RankSnackbarTest : ParentUiTest(),
    ListCancelSnackbarCase {

    @Test override fun displayInsets() = launchRankList { startDisplayInserts(screen = this) }

    @Test override fun singleActionClick() = launchRankList(count = 5) {
        val p = it.indices.random()

        itemCancel(p)
        snackbar { action() }
        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun manyActionClick() = launchRankList(count = 3) {
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

    @Test override fun scrollTopAfterAction() = launchRankList {
        val p = it.indices.first

        itemCancel(p)
        scrollTo(Scroll.END)
        snackbar { action() }

        await(RecyclerItemPart.SCROLL_TIME)
        RecyclerItemPart.PREVENT_SCROLL = true

        assertSnackbarDismissed()
        assertItem(it[p], p)
    }

    @Test override fun scrollBottomAfterAction() = launchRankList {
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

        launchMain {
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

    @Test override fun clearCacheOnDismiss() = db.fillRank().let {
        val p = it.indices.random()

        launchMain {
            openRank {
                itemCancel(p)
                snackbar { action() }
                assertSnackbarDismissed()
            }
            openNotes(isEmpty = true)
            openRank { assertSnackbarDismissed() }
        }
    }

    @Test override fun dismissTimeout() = db.fillRank().let {
        val p = it.indices.random()

        launchMain {
            openRank {
                itemCancel(p)
                await(SnackbarPart.DISMISS_TIME)
                assertSnackbarDismissed()
            }
            openNotes(isEmpty = true)
            openRank { assertSnackbarDismissed() }
        }
    }

    @Test fun dismissOnDrag() {
        TODO()
    }

    @Test fun dismissOnRename() = launchRankList(count = 2) {
        itemCancel(p = 0)
        it.removeAt(index = 0)

        snackbar { assert() }
        openRenameDialog(it[0].name, p = 0) { cancel() }

        assertSnackbarDismissed()
        assertList(it)
    }

    @Test fun dismissOnAddStart() = launchRankList(count = 2) {
        val name = nextShortString()

        toolbar { enter(name) }
        itemCancel(p = 0)
        it.removeAt(index = 0)

        snackbar { assert() }
        smallLongPressTime {
            toolbar { addToStart() }
        }
        assertSnackbarDismissed()

        for ((i, item) in it.withIndex()) {
            assertItem(item, p = i + 1)
        }
        openRenameDialog(name, p = 0)
    }

    @Test fun dismissOnAddEnd() = launchRankList(count = 2) {
        val name = nextShortString()

        toolbar { enter(name) }
        itemCancel(p = 0)
        it.removeAt(index = 0)

        snackbar { assert() }
        toolbar { addToEnd() }
        assertSnackbarDismissed()

        assertList(it)
        openRenameDialog(name, last)
    }

    @Test fun dismissOnAddIme() = launchRankList(count = 2) {
        val name = nextShortString()

        toolbar { enter(name) }
        itemCancel(p = 0)
        it.removeAt(index = 0)

        snackbar { assert() }
        toolbar { imeClick() }
        assertSnackbarDismissed()

        assertList(it)
        openRenameDialog(name, last)
    }
}