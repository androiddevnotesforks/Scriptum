package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test keyboard ime click for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteImeTest : ParentUiTest() {

    @Test fun toolbarImeNext() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onAdd(Random.nextString()).onEnterText(Random.nextString())
                            }
                            toolbar { onEnterName(Random.nextString()).onImeOptionName() }
                        }
                    }
                }
            }
        }
    }

    @Test fun itemImeNext() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onAdd(Random.nextString()).onEnterText(Random.nextString())
                            }
                            onEnterText(Random.nextString())
                            onImeOptionText()
                        }
                    }
                }
            }
        }
    }

    @Test fun enterPanelImeDone() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onEnterText(Random.nextString()).onImeOptionEnter()
                                onEnterText(Random.nextString()).onImeOptionEnter()
                            }
                            onAssertAll()

                            enterPanel { onEnterText(text = " ").onImeOptionEnter() }
                            onAssertAll()
                        }
                    }
                }
            }
        }
    }

}