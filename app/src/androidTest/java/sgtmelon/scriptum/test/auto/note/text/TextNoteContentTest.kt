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
class TextNoteContentTest : ParentUiTest() {

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
        val noteEntity = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteEntity.text) }
                        controlPanel { onClickEdit() }
                        assert { onDisplayText(State.EDIT, noteEntity.text) }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage { openTextNote { assert { onDisplayText(State.BIN, noteEntity.text) } } }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote {
                        assert { onDisplayText(State.BIN, noteEntity.text) }
                        controlPanel { onClickRestoreOpen() }
                        assert { onDisplayText(State.READ, noteEntity.text) }
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

                    assert { onDisplayText(State.EDIT, noteEntity.text) }
                    controlPanel { onClickSave() }
                    assert { onDisplayText(State.READ, noteEntity.text) }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = launch {
        val noteEntity = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(noteEntity.text)

                    assert { onDisplayText(State.EDIT, noteEntity.text) }
                    onPressBack()
                    assert { onDisplayText(State.READ, noteEntity.text) }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteEntity = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteEntity.text) }

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
        val noteEntity = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteEntity.text) }

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
        val noteEntity = testData.insertText()
        val newText = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        assert { onDisplayText(State.READ, noteEntity.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        toolbar { onClickBack() }

                        assert { onDisplayText(State.READ, noteEntity.text) }
                    }
                }
            }
        }
    }

}