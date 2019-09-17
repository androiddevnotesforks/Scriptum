package sgtmelon.scriptum.test.auto.note.text

import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [TextNoteFragment]
 */
//@RunWith(AndroidJUnit4::class)
class TextNotePanelTest : ParentUiTest() {

    private fun actionSaveOnCreate() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        onEnterText(text = "1")
                        onEnterText(text = "")
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(text = "123")
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    private fun actionSaveOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(text = "")
                        toolbar { onEnterName(data.uniqueString) }
                        onEnterText(text = "123")
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

}