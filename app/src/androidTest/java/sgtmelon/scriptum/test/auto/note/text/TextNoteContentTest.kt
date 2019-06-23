package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
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

    // TODO тест существует
    @Test fun contentEmptyOnCreate() = testData.createTextNote().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = testData.insertTextNote().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    @Test fun contentFillOnOpenFromBin() = testData.insertTextNoteToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnRestoreOpen() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { controlPanel { onClickRestoreOpen() } } }
            }
        }
    }

    // TODO тест существует
    @Test fun saveByControlOnCreate() = testData.createTextNote().let {
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
    @Test fun saveByBackPressOnCreate() = testData.createTextNote().let {
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
    @Test fun saveByControlOnEdit() = testData.insertTextNote().let {
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

    @Test fun saveByBackPressOnEdit() = testData.insertTextNote().let {
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


    @Test fun cancelOnEditByToolbar() = testData.insertTextNote().let {
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