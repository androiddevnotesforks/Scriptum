package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test keyboard ime click for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteImeTest : ParentUiTest() {

    @Test fun toolbarImeNext() = db.createRoll().let {
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

    @Test fun itemImeNext() = db.createRoll().let {
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

    @Test fun enterPanelImeDone() = db.createRoll().let {
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