package sgtmelon.scriptum.test.auto.notification

import org.junit.Test
import sgtmelon.extension.getText
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for SnackBar in [NotificationActivity].
 */
class NotificationSnackbarTest : ParentUiTest() {

    @Test fun containerBottomDisplay() {
        fillScreen(count = 15)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onScroll(Scroll.END)
                        repeat(times = 5) { onClickCancel(last, wait = true) }
                    }
                }
            }
        }
    }

    @Test fun actionClick_single() {
        val list = fillScreen(count = 5)
        val p = list.indices.random()

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p)
                        getSnackbar { onClickCancel() }
                        onAssertItem(p, list[p])
                    }
                }
            }
        }
    }

    @Test fun actionClick_many() {
        val list = fillScreen(count = 3)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        repeat(list.size) { onClickCancel(p = 0) }
                        repeat(list.size) {
                            getSnackbar { onClickCancel() }
                            if (it != list.indices.last) {
                                getSnackbar { assert() }
                            }
                        }

                        list.forEachIndexed { i, item -> onAssertItem(i, item) }
                    }
                }
            }
        }
    }

    @Test fun actionClick_dismiss() {
        val list = fillScreen(count = 5)
        val removePosition = 1
        val actionPosition = 2

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(removePosition)

                        onClickCancel(1)
                        getSnackbar { onClickCancel() }
                        getSnackbar { assert() }
                        onAssertItem(1, list[actionPosition])

                        onClickClose()
                    }

                    list.removeAt(removePosition)

                    openNotification {
                        list.forEachIndexed { i, item -> onAssertItem(i, item) }
                    }
                }
            }
        }
    }

    @Test fun dismissRemove() {
        val list = fillScreen(count = 5)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        repeat(list.size) { onClickCancel(p = 0) }
                        onClickClose()
                    }
                    openNotification(empty = true)
                }
            }
        }
    }

    @Test fun openItemDismiss() {
        val list = fillScreen(count = 3)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p = 0)
                        when(val it = list[1]) {
                            is NoteItem.Text -> openText(it, p = 0) { onClickClose() }
                            is NoteItem.Roll -> openRoll(it, p = 0) { onClickClose() }
                        }
                    }
                }
            }
        }
    }

    private fun fillScreen(count: Int): MutableList<NoteItem> = ArrayList<NoteItem>().apply {
        repeat(count) {
            val date = getTime(min = NEXT_HOUR + it * NEXT_HOUR).getText()
            val item = with(data) { insertText(textNote.copy(name = "", color = randomColor)) }

            add(data.insertNotification(item, date))
        }
    }


    companion object {
        private const val NEXT_HOUR = 60
    }

}