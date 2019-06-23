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
class TextNoteToolbarTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun closeByToolbarOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    // TODO тест существует
    @Test fun contentEmptyOnCreate() = testData.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = testData.insertText(
            testData.textNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = testData.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() = testData.insertTextToBin(
            testData.textNote.apply { name = "" }
    ).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnOpenFromBin() = testData.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    // TODO тест существует
    @Test fun contentFillOnRestoreOpen() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { controlPanel { onClickRestoreOpen() } } }
            }
        }
    }


    @Test fun saveByControlOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        onEnterText(testData.uniqueString)
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        onEnterText(testData.uniqueString)
                        onPressBack()
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
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(testData.uniqueString) }
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
                        toolbar { onEnterName(testData.uniqueString) }
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
                        toolbar {
                            onEnterName(testData.uniqueString)
                            onClickBack()
                        }
                    }
                }
            }
        }
    }

}