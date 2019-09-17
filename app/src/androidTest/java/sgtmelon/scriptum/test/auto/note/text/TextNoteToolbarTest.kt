package sgtmelon.scriptum.test.auto.note.text

import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class TextNoteToolbarTest : ParentUiTest() {

    // TODO тест существует
    private fun contentEmptyOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    private fun contentEmptyOnOpen() = data.insertText(
            data.textNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    private fun contentFillOnOpen() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    private fun saveByControlOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    private fun saveByBackPressOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(data.uniqueString)
                        onPressBack()
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
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    private fun saveByBackPressOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(data.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }


    private fun cancelOnEditByToolbar() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar {
                            onEnterName(data.uniqueString)
                            onClickBack()
                        }
                    }
                }
            }
        }
    }

}