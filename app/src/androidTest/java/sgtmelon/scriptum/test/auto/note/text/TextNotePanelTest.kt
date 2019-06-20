package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun displayOnCreate() = launch {
        mainScreen {
            openAddDialog {
                createTextNote { controlPanel { assert { onDisplayContent(State.NEW) } } }
            }
        }
    }

    @Test fun displayOnOpenNote() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage {
                openTextNote { controlPanel { assert { onDisplayContent(State.READ) } } }
            }
        }
    }

    @Test fun displayOnOpenNoteFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch { mainScreen { openBinPage { openTextNote(noteEntity) } } }
    }

    @Test fun displayOnRestoreOpen() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote(noteEntity) {
                        controlPanel {
                            onClickRestoreOpen()
                            assert { onDisplayContent(State.READ) }
                        }
                    }
                }
            }
        }
    }


    @Test fun saveByControlOnCreate() = launch {
        val noteEntity = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteEntity.text)
                    controlPanel {
                        assert { onDisplayContent(State.NEW) }
                        onClickSave()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun saveByPressBackOnCreate() = launch {
        val noteEntity = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteEntity.text)
                    controlPanel {
                        assert { onDisplayContent(State.NEW) }
                        onPressBack()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage {
                openTextNote {
                    controlPanel {
                        assert { onDisplayContent(State.READ) }
                        onClickEdit()
                        assert { onDisplayContent(State.EDIT) }
                        onClickSave()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun saveByPressBackOnEdit() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage {
                openTextNote {
                    controlPanel {
                        assert { onDisplayContent(State.READ) }
                        onClickEdit()
                        assert { onDisplayContent(State.EDIT) }
                        onPressBack()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }


    @Test fun cancelOnEditByToolbar() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage {
                openTextNote {
                    controlPanel {
                        onClickEdit()
                        assert { onDisplayContent(State.EDIT) }
                        toolbar { onClickBack() }
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }


    @Test fun actionRestoreFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openTextNote(noteEntity) { controlPanel { onClickRestore() } }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun actionRestoreOpenFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openTextNote(noteEntity) {
                        controlPanel { onClickRestoreOpen() }
                        assert { onDisplayContent(State.READ) }
                        onPressBack()
                    }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun actionClearFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openTextNote(noteEntity) { controlPanel { onClickClear() } }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }


    @Test fun actionSaveOnCreate() = launch {
        mainScreen {
            openAddDialog {
                createTextNote {
                    controlPanel { assert { isEnabledSave(enabled = false) } }
                    onEnterText(text = "1")
                    controlPanel { assert { isEnabledSave(enabled = true) } }
                    onEnterText(text = "")
                    controlPanel { assert { isEnabledSave(enabled = false) } }
                    onEnterText(text = "123")

                    controlPanel {
                        assert { isEnabledSave(enabled = true) }

                        onClickSave()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun actionSaveOnEdit() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage {
                openTextNote {
                    controlPanel {
                        onClickEdit()
                        assert { isEnabledSave(enabled = true) }
                        onEnterText(text = "")
                        assert { isEnabledSave(enabled = false) }
                        onEnterText(text = "123")

                        controlPanel {
                            assert { isEnabledSave(enabled = true) }

                            onClickSave()
                            assert { onDisplayContent(State.READ) }
                        }
                    }

                }
            }
        }
    }


    @Test fun actionBindToStatusBar() {
        val noteEntity = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        controlPanel { onClickBind() }
                        wait(time = 500)
                        onPressBack()
                    }

                    openNoteDialog(noteEntity.apply { isStatus = true })
                }
            }
        }
    }

    @Test fun actionUnbindFromStatusBar() {
        val noteEntity = testData.insertText(testData.textNote.apply { isStatus = true })

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        controlPanel { onClickBind() }
                        wait(time = 500)
                        onPressBack()
                    }
                    openNoteDialog(noteEntity.apply { isStatus = false })
                }
            }
        }
    }

    @Test fun actionDelete() = launch({ testData.insertText() }) {
        mainScreen {
            openBinPage { assert { onDisplayContent(empty = true) } }

            openNotesPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { controlPanel { onClickDelete() } }
                assert { onDisplayContent(empty = true) }
            }

            openBinPage { assert { onDisplayContent(empty = false) } }
        }
    }

}