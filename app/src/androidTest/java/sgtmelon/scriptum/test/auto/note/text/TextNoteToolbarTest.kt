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

    @Test fun closeByToolbarOnCreate() = testData.createTextNote().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = testData.createTextNote().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    // TODO тест существует
    @Test fun contentEmptyOnCreate() = testData.createTextNote().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = testData.insertTextNote(
            testData.textNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = testData.insertTextNote().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() = testData.insertTextNoteToBin(
            testData.textNote.apply { name = "" }
    ).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
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


    @Test fun saveByControlOnCreate() = testData.createTextNote().let {
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

    @Test fun saveByBackPressOnCreate() = testData.createTextNote().let {
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

    @Test fun saveByControlOnEdit() = testData.insertTextNote().let {
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

    @Test fun saveByBackPressOnEdit() = testData.insertTextNote().let {
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


    @Test fun cancelOnEditByToolbar() = testData.insertTextNote().let {
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