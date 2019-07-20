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

    @Test fun binContentWithoutName() = testData.insertTextToBin(
            testData.textNote.apply { name = "" }
    ).let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binContentWithName() = testData.insertTextToBin().let {
        launch { mainScreen { openBinPage { openTextNote(it) } } }
    }

    @Test fun binClose()  = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openTextNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openTextNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun binActionRestore()  = testData.insertTextToBin().let {
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

    @Test fun binActionRestoreOpen()  = testData.insertTextToBin().let {
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

    @Test fun binActionClear() = testData.insertTextToBin().let {
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