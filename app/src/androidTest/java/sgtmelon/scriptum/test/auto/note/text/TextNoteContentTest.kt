package sgtmelon.scriptum.test.auto.note.text

import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class TextNoteContentTest : ParentUiTest() {

    // TODO тест существует
    private fun contentEmptyOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    // TODO тест существует
    private fun contentFillOnOpen() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
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

    // TODO похожий тест textToolbar.saveByBackPressOnCreate
    private fun saveByBackPressOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(data.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    // tODO похожий тест textPanel.actionSaveOnEdit
    private fun saveByControlOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickEdit() }
                        onEnterText(data.uniqueString)
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
                        onEnterText(data.uniqueString)
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
                        onEnterText(data.uniqueString)
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}