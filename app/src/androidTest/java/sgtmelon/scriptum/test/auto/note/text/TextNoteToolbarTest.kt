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
class TextNoteToolbarTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun closeByToolbarOnCreate() = launch {
        mainScreen {
            openAddDialog { createTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnCreate() = launch {
        mainScreen {
            openAddDialog { createTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpen() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage { openTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpen() = launch({ testData.insertText() }) {
        mainScreen {
            openNotesPage { openTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() = launch({ testData.insertTextToBin() }) {
        mainScreen {
            openBinPage { openTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() = launch({ testData.insertTextToBin() }) {
        mainScreen {
            openBinPage { openTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }


    @Test fun contentEmptyOnCreate() = launch {
        mainScreen {
            openAddDialog {
                createTextNote { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun contentEmptyOnOpen() {
        val noteEntity = testData.insertText(testData.textNote.apply { name = "" })

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpen() {
        val noteEntity = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() {
        val noteEntity = testData.insertTextToBin(testData.textNote.apply { name = "" })

        launch {
            mainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                        controlPanel { onClickRestoreOpen() }
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
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
                    toolbar { onEnterName(noteEntity.name) }
                    onEnterText(noteEntity.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = launch {
        val noteEntity = testData.textNote

        mainScreen {
            openAddDialog {
                createTextNote {
                    toolbar { onEnterName(noteEntity.name) }
                    onEnterText(noteEntity.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteEntity = testData.insertText()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }

                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        controlPanel { onClickSave() }

                        toolbar { assert { onDisplayName(State.READ, newName) } }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnEdit() {
        val noteEntity = testData.insertText()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }

                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        onPressBack()

                        toolbar { assert { onDisplayName(State.READ, newName) } }
                    }
                }
            }
        }
    }


    @Test fun cancelOnEditByToolbar() {
        val noteEntity = testData.insertText()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }

                        controlPanel { onClickEdit() }
                        toolbar {
                            onEnterName(newName)
                            onClickBack()
                        }

                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                    }
                }
            }
        }
    }

}