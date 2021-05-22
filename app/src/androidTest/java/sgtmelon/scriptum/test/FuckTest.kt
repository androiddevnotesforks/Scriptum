package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getNewCalendar
import sgtmelon.extension.getText
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort

@RunWith(AndroidJUnit4::class)
class FuckTest : ParentUiTest() {

    @Test fun clickRepeatCorrectSeconds0() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds1() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds2() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds3() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds4() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds5() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds6() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds7() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds8() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds9() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds10() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds11() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds12() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds13() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds14() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds15() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds16() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds17() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds18() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds19() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds20() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds21() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds22() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds23() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds24() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds25() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds26() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds27() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds28() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds29() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds30() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds31() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds32() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds33() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds34() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds35() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds36() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds37() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds38() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds39() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds40() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds41() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds42() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds43() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds44() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds45() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds46() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds47() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds48() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds49() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds50() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds51() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds52() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun clickRepeatCorrectSeconds53() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

}