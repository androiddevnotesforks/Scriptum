package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test toolbar arrow and back press for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin()  = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                binScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog { createText(it) { toolbar { onClickBack() } } }.assert()
                openAddDialog { createText(it) { onPressBack() } }.assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { toolbar { onClickBack() } } }.assert()
                notesScreen { openTextNote(it) { onPressBack() } }.assert()
            }
        }
    }


    @Test fun saveOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) {
                        toolbar { onEnterName(Random.nextString()) }
                        onEnterText(Random.nextString())
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(Random.nextString()) }
                        onEnterText(Random.nextString())
                        onPressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(Random.nextString())
                        toolbar { onEnterName(Random.nextString()).onClickBack() }
                    }
                }
            }
        }
    }

}