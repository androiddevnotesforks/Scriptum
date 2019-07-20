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
class TextNoteToolbarTest : ParentUiTest() {

    @Test fun closeByToolbarOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert()
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