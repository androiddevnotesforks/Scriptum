package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentUiTest() {

    //region Bin

    @Test fun binContentWithoutName() = data.insertTextToBin(data.textNote.copy(name = "")).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binContentWithName() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binActionRestore()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onClickRestore() } }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun binActionRestoreOpen()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) {
                        controlPanel { onClickRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun binActionClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openTextNote(it) { controlPanel { onClickClear() } }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }

    //endregion

    //region Close

    @Test fun closeOnBin()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    //endregion

}