package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test keyboard ime click for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteImeTest : ParentUiTest() {

    @Test fun toolbarImeNext() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onAdd(nextString()).onEnterText(nextString())
                            }
                            toolbar { onEnterName(nextString()).onImeOptionName() }
                        }
                    }
                }
            }
        }
    }

    @Test fun itemImeNext() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onAdd(nextString()).onEnterText(nextString())
                            }
                            onEnterText(nextString())
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
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            enterPanel {
                                onEnterText(nextString()).onImeOptionEnter()
                                onEnterText(nextString()).onImeOptionEnter()
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