package sgtmelon.scriptum.test.auto.note

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
class TextNoteTest : ParentUiTest() {

    @Test fun binContentWithoutName() = data.insertTextToBin(
            data.textNote.apply { name = "" }
    ).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binContentWithName() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binClose()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
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

}