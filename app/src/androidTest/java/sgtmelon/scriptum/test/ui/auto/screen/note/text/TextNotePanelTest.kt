package sgtmelon.scriptum.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test control panel for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openTextNote(it) { controlPanel { onRestore() } }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen()  = data.insertTextToBin().let {
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

    @Test fun actionOnBinClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openTextNote(it) { controlPanel { onClear() } }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = data.insertText(data.textNote.copy(isStatus = isStatus))

        launch {
            mainScreen {
                notesScreen {
                    openTextNote(model) { controlPanel { onBind() }.onPressBack() }
                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onConvert() } } } } }
    }

    @Test fun actionOnReadDelete() = data.insertText().let {
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

    @Test fun actionOnReadEdit() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit() } } } } }
    }


    // TODO finish test
    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateRank() = data.fillRank(count = 3).let {
        val item = data.createText()
        launch {
            mainScreen {
                openAddDialog {
                    createText(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = data.fillRank(count = 3).let {
        val item = data.insertText()
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

    @Test fun actionOnCreateColor() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openTextNote(it) { controlPanel { onEdit().onColor() } } } }
        }
    }


    @Test fun actionOnCreateSave() = data.createText().let {
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

    @Test fun actionOnEditSave() = data.insertText().let {
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

    @Test fun actionOnCreateLongSave() = data.createText().let {
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

    @Test fun actionOnEditLongSave() = data.insertText().let {
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