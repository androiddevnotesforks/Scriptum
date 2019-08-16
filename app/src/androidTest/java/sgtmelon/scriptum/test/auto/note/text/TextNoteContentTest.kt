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
class TextNoteContentTest : ParentUiTest() {

    // TODO тест существует
    @Test fun contentEmptyOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    @Test fun saveByControlOnCreate() = data.createText().let {
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
    @Test fun saveByBackPressOnCreate() = data.createText().let {
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
    @Test fun saveByControlOnEdit() = data.insertText().let {
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

    @Test fun saveByBackPressOnEdit() = data.insertText().let {
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


    @Test fun cancelOnEditByToolbar() = data.insertText().let {
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