package sgtmelon.scriptum.cleanup.test.ui.auto.screen.notification

import org.junit.Test
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.testData.Scroll
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.ParentUiTest

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
                        repeat(times = 5) {
                            onClickCancel(last, isWait = true)
                            assertSnackbarDismiss()
                        }
                    }
                }
            }
        }
    }

    @Test fun actionClickSingle() {
        val list = fillScreen(count = 5)
        val p = list.indices.random()

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p)
                        getSnackbar { onClickCancel() }
                        assertSnackbarDismiss()
                        onAssertItem(p, list[p])
                    }
                }
            }
        }
    }

    @Test fun actionClickMany() {
        val list = fillScreen(count = 3)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        repeat(list.size) { onClickCancel(p = 0) }
                        repeat(list.size) {
                            getSnackbar { onClickCancel() }
                            if (it != list.lastIndex) {
                                getSnackbar { assert() }
                            }
                        }

                        assertSnackbarDismiss()

                        for ((i, item) in list.withIndex()) {
                            onAssertItem(i, item)
                        }
                    }
                }
            }
        }
    }

    @Test fun actionClickDismiss() {
        val list = fillScreen(count = 5)
        val removePosition = 1
        val actionPosition = 2

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(removePosition)

                        onClickCancel(p = 1)
                        getSnackbar { onClickCancel() }
                        getSnackbar { assert() }
                        onAssertItem(1, list[actionPosition])

                        onClickClose()
                    }

                    list.removeAt(removePosition)

                    openNotification {
                        assertSnackbarDismiss()
                        for ((i, item) in list.withIndex()) {
                            onAssertItem(i, item)
                        }
                    }
                }
            }
        }
    }

    @Test fun actionClickNoteCheck() {
        val list = fillScreen(count = 5)
        val p = list.indices.random()

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p)
                        getSnackbar { onClickCancel() }
                        assertSnackbarDismiss()

                        when (val item = list[p]) {
                            is NoteItem.Text -> openText(item, p) {
                                controlPanel { onNotification(isUpdateDate = true) }
                            }
                            is NoteItem.Roll -> openRoll(item, p) {
                                controlPanel { onNotification(isUpdateDate = true) }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun dismissOnPause() {
        val list = fillScreen(count = 3)

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p = 0)

                        list.removeAt(index = 0)

                        when (val it = list[0]) {
                            is NoteItem.Text -> openText(it, p = 0) { onClickClose() }
                            is NoteItem.Roll -> openRoll(it, p = 0) { onClickClose() }
                        }

                        assertSnackbarDismiss()

                        for ((i, item) in list.withIndex()) {
                            onAssertItem(i, item)
                        }

                        repeat(list.size) { onClickCancel(p = 0) }
                        onClickClose()
                    }

                    openNotification(isEmpty = true) { assertSnackbarDismiss() }
                }
            }
        }
    }


    @Test fun scrollToUndoItemOnTop() {
        val list = fillScreen(count = 15)
        val p = list.indices.first()

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p)
                        onScroll(Scroll.END)
                        getSnackbar { onClickCancel() }

                        assertSnackbarDismiss()

                        ParentRecyclerItem.PREVENT_SCROLL = true
                        onAssertItem(p, list[p])
                    }
                }
            }
        }
    }

    @Test fun scrollToUndoItemOnBottom() {
        val list = fillScreen(count = 15)
        val p = list.lastIndex

        launch {
            mainScreen {
                notesScreen {
                    openNotification {
                        onClickCancel(p)
                        onScroll(Scroll.START)
                        getSnackbar { onClickCancel() }

                        assertSnackbarDismiss()

                        ParentRecyclerItem.PREVENT_SCROLL = true
                        onAssertItem(p, list[p])
                    }
                }
            }
        }
    }


    private fun fillScreen(count: Int): MutableList<NoteItem> = ArrayList<NoteItem>().apply {
        repeat(count) {
            val date = getClearCalendar(addMinutes = NEXT_HOUR + it * NEXT_HOUR).toText()
            val color = Color.values().random()
            val item = with(db) { insertText(textNote.copy(name = "", color = color)) }

            add(db.insertNotification(item, date))
        }
    }

    companion object {
        private const val NEXT_HOUR = 60
    }
}