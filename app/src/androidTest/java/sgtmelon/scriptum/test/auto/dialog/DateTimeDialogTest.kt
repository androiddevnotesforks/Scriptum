package sgtmelon.scriptum.test.auto.dialog

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.test.ParentUiTest

@RunWith(AndroidJUnit4::class)
class DateTimeDialogTest : ParentUiTest() {

    @Test fun textNoteDateReset() = data.insertNotification(data.insertText()).let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onNotification(updateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }

    @Test fun rollNoteDateReset() = data.insertNotification(data.insertRoll()).let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onNotification(updateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }


    @Test fun toastToday() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onNotification {
                                onClickApply {
                                    onChangeTime(minute = 2)
                                    waitAfter(TOAST_TIME) { onClickApply() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun toastOther() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel {
                            onNotification {
                                onChangeDate(day = 1)
                                onClickApply {
                                    onChangeTime(minute = 2)
                                    waitAfter(TOAST_TIME) { onClickApply() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val TOAST_TIME = 1000L
    }

}