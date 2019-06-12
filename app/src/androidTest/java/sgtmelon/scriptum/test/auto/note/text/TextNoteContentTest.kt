package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNoteContentTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun contentEmptyOnCreate() = launch {
            mainScreen {
            openAddDialog { createTextNote { assert { onDisplayText(State.EDIT, text = "") } } }
        }
    }

    @Test fun contentFillOnOpen() {
        val noteItem = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteItem.text) }
                        controlPanel { onClickEdit() }
                        assert { onDisplayText(State.EDIT, noteItem.text) }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteItem = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage { openTextNote { assert { onDisplayText(State.BIN, noteItem.text) } } }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteItem = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote {
                        assert { onDisplayText(State.BIN, noteItem.text) }
                        controlPanel { onClickRestoreOpen() }
                        assert { onDisplayText(State.READ, noteItem.text) }
                    }
                }
            }
        }
    }


    @Test fun saveByControlOnCreate() = launch {
        val noteItem = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteItem.text)

                    assert { onDisplayText(State.EDIT, noteItem.text) }
                    controlPanel { onClickSave() }
                    assert { onDisplayText(State.READ, noteItem.text) }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = launch {
        val noteItem = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteItem.text)

                    assert { onDisplayText(State.EDIT, noteItem.text) }
                    onPressBack()
                    assert { onDisplayText(State.READ, noteItem.text) }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        controlPanel { onClickSave() }

                        assert { onDisplayText(State.READ, newText) }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnEdit() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        onPressBack()

                        assert { onDisplayText(State.READ, newText) }
                    }
                }
            }
        }
    }


    @Test fun cancelOnEditByToolbar() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        toolbar { onClickBack() }

                        assert { onDisplayText(State.READ, noteItem.text) }
                    }
                }
            }
        }
    }

}