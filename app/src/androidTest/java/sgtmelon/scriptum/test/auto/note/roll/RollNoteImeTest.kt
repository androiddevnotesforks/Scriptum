package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

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
                            enterPanel { onAdd(data.uniqueString).onEnterText(data.uniqueString) }
                            toolbar { onEnterName(data.uniqueString).onImeOptionName() }
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
                            enterPanel { onAdd(data.uniqueString).onEnterText(data.uniqueString) }
                            onEnterText(data.uniqueString)
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
                                onEnterText(data.uniqueString).onImeOptionEnter()
                                onEnterText(data.uniqueString).onImeOptionEnter()
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