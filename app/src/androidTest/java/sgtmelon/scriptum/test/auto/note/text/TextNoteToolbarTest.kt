package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNoteToolbarTest : ParentUiTest() {

    @Test fun closeByToolbarOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }


    // TODO тест существует
    @Test fun contentEmptyOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = data.insertText(
            data.textNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    // TODO тест существует
    @Test fun contentFillOnOpen() = data.insertText().let {
        launch {
            mainScreen { openNotesPage { openTextNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun saveByControlOnCreate() = data.createText().let {
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

    @Test fun saveByBackPressOnCreate() = data.createText().let {
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

    @Test fun saveByControlOnEdit() = data.insertText().let {
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

    @Test fun saveByBackPressOnEdit() = data.insertText().let {
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


    @Test fun cancelOnEditByToolbar() = data.insertText().let {
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