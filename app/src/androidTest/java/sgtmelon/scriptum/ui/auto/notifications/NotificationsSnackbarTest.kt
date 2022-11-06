package sgtmelon.scriptum.ui.auto.notifications

import org.junit.Test
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.ui.testing.model.key.Scroll
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch
import sgtmelon.scriptum.ui.testing.parent.screen.RecyclerItem

/**
 * Test for SnackBar in [NotificationsActivity].
 */
class NotificationsSnackbarTest : ParentUiTest() {

    @Test fun containerBottomDisplay() {
        fillScreen(count = 15)

        launch {
            mainScreen {
                notesScreen {
                    openNotifications {
                        onScroll(Scroll.END)
                        repeat(times = 5) {
                            itemCancel(last, isWait = true)
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
                    openNotifications {
                        itemCancel(p)
                        getSnackbar { clickCancel() }
                        assertSnackbarDismiss()
                        assertItem(p, list[p])
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
                    openNotifications {
                        repeat(list.size) { itemCancel(p = 0) }
                        repeat(list.size) {
                            getSnackbar { clickCancel() }
                            if (it != list.lastIndex) {
                                getSnackbar { assert() }
                            }
                        }

                        assertSnackbarDismiss()

                        for ((i, item) in list.withIndex()) {
                            assertItem(i, item)
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
                    openNotifications {
                        itemCancel(removePosition)

                        itemCancel(p = 1)
                        getSnackbar { clickCancel() }
                        getSnackbar { assert() }
                        assertItem(1, list[actionPosition])

                        clickClose()
                    }

                    list.removeAt(removePosition)

                    openNotifications {
                        assertSnackbarDismiss()
                        for ((i, item) in list.withIndex()) {
                            assertItem(i, item)
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
                    openNotifications {
                        itemCancel(p)
                        getSnackbar { clickCancel() }
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
                    openNotifications {
                        itemCancel(p = 0)

                        list.removeAt(index = 0)

                        when (val it = list[0]) {
                            is NoteItem.Text -> openText(it, p = 0) { clickClose() }
                            is NoteItem.Roll -> openRoll(it, p = 0) { clickClose() }
                        }

                        assertSnackbarDismiss()

                        for ((i, item) in list.withIndex()) {
                            assertItem(i, item)
                        }

                        repeat(list.size) { itemCancel(p = 0) }
                        clickClose()
                    }

                    openNotifications(isEmpty = true) { assertSnackbarDismiss() }
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
                    openNotifications {
                        itemCancel(p)
                        onScroll(Scroll.END)
                        getSnackbar { clickCancel() }

                        assertSnackbarDismiss()

                        RecyclerItem.PREVENT_SCROLL = true
                        assertItem(p, list[p])
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
                    openNotifications {
                        itemCancel(p)
                        onScroll(Scroll.START)
                        getSnackbar { clickCancel() }

                        assertSnackbarDismiss()

                        RecyclerItem.PREVENT_SCROLL = true
                        assertItem(p, list[p])
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