package sgtmelon.scriptum.test.auto.note.text

import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    private fun saveByControlOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(data.uniqueString)
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    private fun saveByControlOnEdit() = data.insertText().let {
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



    private fun actionSaveOnCreate() = data.createText().let {
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

    private fun actionSaveOnEdit() = data.insertText().let {
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


    private fun actionBindToStatusBar() = data.insertText().let {
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

    private fun actionUnbindFromStatusBar() = data.insertText(
            data.textNote.apply { isStatus = true }
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

    private fun actionDelete() = data.insertText().let {
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