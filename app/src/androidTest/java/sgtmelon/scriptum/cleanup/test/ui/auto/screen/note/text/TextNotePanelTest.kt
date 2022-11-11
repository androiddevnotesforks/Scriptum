package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test control panel for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openText(it) { controlPanel { onRestore() } }
                    assert(isEmpty = true)
                }
                openNotes()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)

                openBin {
                    openText(it) { controlPanel { onRestoreOpen() }.pressBack() }
                    assert(isEmpty = true)
                }

                openNotes()
            }
        }
    }

    @Test fun actionOnBinClear() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openText(it) { controlPanel { onClear() } }
                    assert(isEmpty = true)
                }
                openNotes(isEmpty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = db.insertText().let {
        launch {
            mainScreen { openNotes { openText(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = db.insertText(db.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                openNotes {
                    openText(model) { controlPanel { onBind() }.pressBack() }
                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = db.insertText().let {
        launch { mainScreen { openNotes { openText(it) { controlPanel { onConvert() } } } } }
    }

    @Test fun actionOnReadDelete() = db.insertText().let {
        launch {
            mainScreen {
                openBin(isEmpty = true)

                openNotes {
                    openText(it) { controlPanel { onDelete() } }
                    assert(isEmpty = true)
                }

                openBin()
            }
        }
    }

    @Test fun actionOnReadEdit() = db.insertText().let {
        launch { mainScreen { openNotes { openText(it) { controlPanel { onEdit() } } } } }
    }


    // TODO finish test
    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateRank() = db.fillRank(count = 3).let {
        val item = db.createText()
        launch {
            mainScreen {
                openAddDialog {
                    createText(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = db.fillRank(count = 3).let {
        val item = db.insertText()
        launch {
            mainScreen {
                openNotes {
                    openText(item, isRankEmpty = false) {
                        controlPanel { onEdit().onRank(it) }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateColor() = db.createText().let {
        launch { mainScreen { openAddDialog { createText(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = db.insertText().let {
        launch {
            mainScreen { openNotes { openText(it) { controlPanel { onEdit().onColor() } } } }
        }
    }


    @Test fun actionOnCreateSave() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                            .onEnterText()
                            .onEnterText(nextString())

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }

                        onEnterText()
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateLongSave() = db.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())
                            .onEnterText()
                            .onEnterText(nextString())

                        controlPanel { onLongSave() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditLongSave() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }

                        onEnterText()
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())

                        controlPanel { onLongSave() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}