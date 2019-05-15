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
class TextNoteToolbarTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clear()
    }

    @Test fun closeByToolbarOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog { createTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog { createTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpen() {
        beforeLaunch { testData.insertText() }

        MainScreen {
            openNotesPage { openTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpen() {
        beforeLaunch { testData.insertText() }

        MainScreen {
            openNotesPage { openTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openBinPage { openTextNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() {
        beforeLaunch { testData.insertTextToBin() }

        MainScreen {
            openBinPage { openTextNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }


    @Test fun contentEmptyOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog {
                createTextNote { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun contentEmptyOnOpen() {
        val noteItem = testData.insertText(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpen() {
        val noteItem = testData.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() {
        val noteItem = testData.insertTextToBin(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                        controlPanel { onClickRestoreOpen() }
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
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
                    toolbar { onEnterName(noteItem.name) }
                    onEnterText(noteItem.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            openAddDialog {
                createTextNote {
                    toolbar { onEnterName(noteItem.name) }
                    onEnterText(noteItem.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

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
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

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
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openTextNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

                        controlPanel { onClickEdit() }
                        toolbar {
                            onEnterName(newName)
                            onClickBack()
                        }

                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                    }
                }
            }
        }
    }

}