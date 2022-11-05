package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                binScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog { createText(it) { toolbar { onClickBack() } } }.assert()
                openAddDialog { createText(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                notesScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }


    @Test fun saveOnCreate() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                        onPressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(nextString())
                        toolbar { onEnterName(nextString()).onClickBack() }
                    }
                }
            }
        }
    }

}