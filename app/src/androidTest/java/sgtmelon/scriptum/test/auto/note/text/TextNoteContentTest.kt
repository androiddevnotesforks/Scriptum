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
    @Test fun contentEmptyOnCreate() = testData.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = testData.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
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

    // TODO похожий тест textToolbar.saveByBackPressOnCreate
    @Test fun saveByBackPressOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(testData.uniqueString)
                        onPressBack()
                    }
                }
            }
        }
    }

    // tODO похожий тест textPanel.actionSaveOnEdit
    @Test fun saveByControlOnEdit() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickEdit() }
                        onEnterText(testData.uniqueString)
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnEdit() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onClickEdit() }
                        onEnterText(testData.uniqueString)
                        onPressBack()
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
                        controlPanel { onClickEdit() }
                        onEnterText(testData.uniqueString)
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}