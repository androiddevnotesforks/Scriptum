package sgtmelon.scriptum.test.auto.dialog

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.test.ParentUiTest

@RunWith(AndroidJUnit4::class)
class DateTimeDialogTest : ParentUiTest() {

    @Test fun mainDataReset() = data.insertNotification(data.insertNote()).let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onNotification { onClickReset() } } }
            }
        }
    }

    @Test fun textNoteDateReset() = data.insertNotification(data.insertText()).let {
        if (it !is NoteItem.Text) throw NoteCastException()

        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onNotification(isUpdateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }

    @Test fun rollNoteDateReset() = data.insertNotification(data.insertRoll()).let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onNotification(isUpdateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }


    @Test fun mainToastToday() = data.insertNote().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        onNotification {
                            onClickApply {
                                onTime(min = 2)
                                waitAfter(TOAST_TIME) { onClickApply() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun noteToastToday() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onNotification {
                                onClickApply {
                                    onTime(min = 2)
                                    waitAfter(TOAST_TIME) { onClickApply() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun mainToastOther() = data.insertNote().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        onNotification {
                            onDate(day = 1).onClickApply {
                                onTime(min = 2)
                                waitAfter(TOAST_TIME) { onClickApply() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun noteToastOther() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onNotification {
                                onDate(day = 1).onClickApply {
                                    onTime(min = 2)
                                    waitAfter(TOAST_TIME) { onClickApply() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun timeApplyEnablePast() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onNotification { onClickApply { onTime(min = -1).onTime(min = 3) } }
                        }
                    }
                }
            }
        }
    }

    @Test fun timeApplyEnableList() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        val date = data.insertNotification().alarmDate
                        val calendar = date.getCalendar()
                        val dateList = arrayListOf(date)

                        controlPanel {
                            onNotification {
                                onDate(calendar).onClickApply(dateList) { onTime(calendar) }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TOAST_TIME = 1000L
    }
}