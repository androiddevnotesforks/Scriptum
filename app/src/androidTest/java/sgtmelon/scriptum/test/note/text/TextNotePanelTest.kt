package sgtmelon.scriptum.test.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clear()
    }

    @Test fun displayOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog {
                createTextNote { controlPanel { assert { onDisplayContent(State.NEW) } } }
            }
        }
    }

    @Test fun displayOnOpenNote() {
        beforeLaunch { testData.insertText() }

        MainScreen {
            openNotesPage {
                openTextNote { controlPanel { assert { onDisplayContent(State.READ) } } }
            }
        }
    }

    @Test fun displayOnOpenNoteFromBin() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openBinPage {
                openTextNote { controlPanel { assert { onDisplayContent(State.BIN) } } }
            }
        }
    }

    @Test fun displayOnRestoreOpen() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openBinPage {
                openTextNote {
                    controlPanel {
                        assert { onDisplayContent(State.BIN) }
                        onClickRestoreOpen()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }


    @Test fun saveByControlOnCreate() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteItem.text)
                    controlPanel {
                        assert { onDisplayContent(State.NEW) }
                        onClickSave()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun saveByPressBackOnCreate() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteItem.text)
                    controlPanel {
                        assert { onDisplayContent(State.NEW) }
                        onPressBack()
                        assert { onDisplayContent(State.READ) }
                    }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        beforeLaunch { testData.insertText() }

        MainScreen {
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

    @Test fun saveByPressBackOnEdit() {
        beforeLaunch { testData.insertText() }

        MainScreen {
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


    @Test fun cancelOnEditByToolbar() {
        beforeLaunch { testData.insertText() }

        MainScreen {
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
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }

            openBinPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { controlPanel { onClickRestore() } }
                assert { onDisplayContent(empty = true) }
            }

            openNotesPage { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun actionRestoreOpenFromBin() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }

            openBinPage {
                assert { onDisplayContent(empty = false) }
                openTextNote {
                    controlPanel { onClickRestoreOpen() }
                    assert { onDisplayContent(State.READ) }
                    onPressBack()
                }
                assert { onDisplayContent(empty = true) }
            }

            openNotesPage { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun actionClearFromBin() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }

            openBinPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { controlPanel { onClickClear() } }
                assert { onDisplayContent(empty = true) }
            }

            openNotesPage { assert { onDisplayContent(empty = true) } }
        }
    }


    @Test fun actionSaveOnCreate() = afterLaunch {
        MainScreen {
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

    @Test fun actionSaveOnEdit() {
        beforeLaunch { testData.insertText() }

        MainScreen {
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
        val noteItem = testData.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickBind() } }
                        onPressBack()
                    }

                    openNoteDialog(noteItem.apply { isStatus = true })
                }
            }
        }
    }

    @Test fun actionUnbindFromStatusBar() {
        val noteItem = testData.insertText(testData.textNote.apply { isStatus = true })

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickBind() } }
                        onPressBack()
                    }
                    openNoteDialog(noteItem.apply { isStatus = false })
                }
            }
        }
    }

    @Test fun actionDelete() {
        beforeLaunch { testData.insertText() }

        MainScreen {
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