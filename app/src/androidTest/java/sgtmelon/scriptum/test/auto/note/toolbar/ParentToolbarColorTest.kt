package sgtmelon.scriptum.test.auto.note.toolbar

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.test.IColorTest
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Parent class for tests of [NoteActivity] toolbar color with different themes
 */
abstract class ParentToolbarColorTest(@Theme private val theme: Int) : ParentUiTest(), IColorTest {

    override fun startTest(@Color color: Int) {
        iPreferenceRepo.theme = theme
        iPreferenceRepo.defaultColor = Color.list.filter { it != color }.random()

        launch {
            mainScreen {
                notesScreen(empty = true) {
                    val textNote = data.createText()

                    addDialog {
                        createText(textNote) {
                            controlPanel {
                                onColor { onClickItem(color).onClickAccept() }
                                onEnterText(data.uniqueString)
                                onSave()
                            }
                            toolbar { onClickBack() }
                        }
                    }
                    openNoteDialog(textNote) { onDelete() }

                    val rollNote = data.createRoll()

                    addDialog {
                        createRoll(rollNote) {
                            controlPanel {
                                onColor { onClickItem(color).onClickAccept() }
                                enterPanel { onAddRoll(data.uniqueString) }
                                onSave()
                            }
                            toolbar { onClickBack() }
                        }
                    }
                    openNoteDialog(rollNote) { onDelete() }
                }
            }
        }
    }

}