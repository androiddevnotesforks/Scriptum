package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test control panel for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore() = db.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openTextNote(it) { controlPanel { onRestore() } }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen() = db.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)

                binScreen {
                    openTextNote(it) { controlPanel { onRestoreOpen() }.onPressBack() }
                    assert(isEmpty = true)
                }

                notesScreen()
            }
        }
    }

    @Test fun actionOnBinClear() = db.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openTextNote(it) { controlPanel { onClear() } }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = db.insertText().let {
        launch {
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = db.insertText(db.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                notesScreen {
                    openTextNote(model) { controlPanel { onBind() }.onPressBack() }
                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = db.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onConvert() } } } } }
    }

    @Test fun actionOnReadDelete() = db.insertText().let {
        launch {
            mainScreen {
                binScreen(isEmpty = true)

                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }.assert(isEmpty = true)
                }

                binScreen()
            }
        }
    }

    @Test fun actionOnReadEdit() = db.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit() } } } } }
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
                notesScreen {
                    openTextNote(item, isRankEmpty = false) {
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
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit().onColor() } } } }
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
                notesScreen {
                    openTextNote(it) {
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
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditLongSave() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }

                        onEnterText()
                        toolbar { onEnterName(nextString()) }
                        onEnterText(nextString())

                        controlPanel { onLongSave() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

}