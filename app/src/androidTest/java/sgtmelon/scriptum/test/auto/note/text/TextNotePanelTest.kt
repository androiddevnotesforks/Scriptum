package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    // TODO тест существует
    @Test fun displayOnCreate() = testData.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun displayOnOpenNote() = testData.insertText().let {
        launch { mainScreen { openNotesPage { openTextNote(it) } } }
    }


    @Test fun saveByControlOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(testData.uniqueString)
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByPressBackOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(testData.uniqueString)
                        controlPanel { onPressBack() }
                    }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            onClickEdit()
                            onClickSave()
                        }
                    }
                }
            }
        }
    }

    @Test fun saveByPressBackOnEdit() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            onClickEdit()
                            onPressBack()
                        }
                    }
                }
            }
        }
    }


    @Test fun cancelOnEditByToolbar() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            onClickEdit()
                            toolbar { onClickBack() }
                        }
                    }
                }
            }
        }
    }

    @Test fun actionSaveOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(text = "1")
                        onEnterText(text = "")
                        onEnterText(text = "123")
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun actionSaveOnEdit() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            onClickEdit()
                            onEnterText(text = "")
                            onEnterText(text = "123")
                            controlPanel { onClickSave() }
                        }
                    }
                }
            }
        }
    }


    @Test fun actionBindToStatusBar() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickBind() }
                        onPressBack()
                    }

                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun actionUnbindFromStatusBar() = testData.insertText(
            testData.textNote.apply { isStatus = true }
    ).let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickBind() }
                        onPressBack()
                    }

                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun actionDelete() = testData.insertText().let {
        launch {
            mainScreen {
                openBinPage(empty = true)

                openNotesPage {
                    openTextNote(it) { controlPanel { onClickDelete() } }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }

}