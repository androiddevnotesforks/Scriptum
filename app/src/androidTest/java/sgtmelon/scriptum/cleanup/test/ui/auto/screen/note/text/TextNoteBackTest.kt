package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin { openText(it) { toolbar { clickBack() } } }.assert()
                openBin { openText(it) { pressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog { createText(it) { toolbar { clickBack() } } }.assert()
                openAddDialog { createText(it) { pressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertText().let {
        launch {
            mainScreen {
                openNotes { openText(it) { toolbar { clickBack() } } }.assert()
                openNotes { openText(it) { pressBack() } }.assert()
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
                        pressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                        pressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        onEnterText(nextString())
                        toolbar { onEnterName(nextString()).clickBack() }
                    }
                }
            }
        }
    }
}