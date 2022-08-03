package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

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