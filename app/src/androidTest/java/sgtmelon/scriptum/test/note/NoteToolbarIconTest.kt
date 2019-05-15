package sgtmelon.scriptum.test.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест анимации кнопки у тулбара [MenuControlAnim] в [TextNoteFragment] и [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteToolbarIconTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clear()
    }

    @Test fun arrowBackOnCreateTextNote() = afterLaunch {
        MainScreen { openAddDialog { createTextNote { wait(time = 500) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = afterLaunch {
        MainScreen { openAddDialog { createRollNote { wait(time = 500) } } }
    }

    @Test fun notAnimateOnSaveCreateTextNote() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteItem.text)
                    waitAfter(time = 500) { controlPanel { onClickSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = afterLaunch {
        val listRoll = testData.listRoll

        MainScreen {
            openAddDialog {
                createRollNote {
                    enterPanel { onAddRoll(listRoll[0].text) }
                    waitAfter(time = 500) { controlPanel { onClickSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() {
        testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() {
        testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openRollNote {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveTextNote() {
        testData.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(time = 500) { onClickEdit() }
                                waitAfter(time = 500) { onClickSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() {
        testData.insertRoll()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(time = 500) { onClickEdit() }
                                waitAfter(time = 500) { onClickSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() {
        testData.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }

                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelRollNote() {
        testData.insertRoll()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }

                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

}