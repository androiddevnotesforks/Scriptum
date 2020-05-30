package sgtmelon.scriptum.test.auto.main.rank

import org.junit.Test
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
                    repeat(times = 5) { onClickCancel(last, wait = true) }
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
                    onAssertItem(it[p])
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
                        if (it != list.indices.last) {
                            getSnackbar { assert() }
                        }
                    }

                    list.forEach { item -> onAssertItem(item) }
                }
            }
        }
    }

    @Test fun actionClick_dismiss() = data.fillRank(count = 5).let {
        val removePosition = 1

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(removePosition)

                    onClickCancel(p = 1)
                    getSnackbar { onClickCancel() }
                    getSnackbar { assert() }
                    onAssertItem(it[1])
                }

                notesScreen(empty = true)

                it.removeAt(removePosition)

                rankScreen { it.forEach { onAssertItem(it) } }
            }
        }
    }


    @Test fun dismissOnPause() = data.fillRank(count = 5).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p = 0)
                    list.removeAt(0)
                }

                notesScreen(empty = true)
                rankScreen {
                    list.forEach { onAssertItem(it) }
                    repeat(list.size) { onClickCancel(p = 0) }
                }

                notesScreen(empty = true)
                rankScreen(empty = true)
            }
        }
    }

    @Test fun dismissOnDrag() {
        TODO()
    }


    @Test fun scrollToUndoItem_onTop() = data.fillRank(count = 15).let {
        val p = it.indices.first()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.END)
                    getSnackbar { onClickCancel() }

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    onAssertItem(it[p])
                }
            }
        }
    }

    @Test fun scrollToUndoItem_onBottom() = data.fillRank(count = 15).let {
        val p = it.indices.last()

        launch {
            mainScreen {
                rankScreen {
                    onClickCancel(p)
                    onScroll(Scroll.START)
                    getSnackbar { onClickCancel() }

                    ParentRecyclerItem.PREVENT_SCROLL = true
                    onAssertItem(it[p])
                }
            }
        }
    }

}