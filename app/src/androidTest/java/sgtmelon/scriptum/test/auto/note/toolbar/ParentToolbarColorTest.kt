package sgtmelon.scriptum.test.auto.note.toolbar

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.test.IColorTest
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

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
                    addDialog {
                        val noteItem = data.createNote()
                        when(noteItem.type) {
                            NoteType.TEXT -> {
                                createText(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(color).onClickAccept() }
                                        onEnterText(data.uniqueString)
                                        onSave()
                                    }
                                    toolbar { onClickBack() }
                                }
                            }
                            NoteType.ROLL -> {
                                createRoll(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(color).onClickAccept() }
                                        enterPanel { onAddRoll(data.uniqueString) }
                                        onSave()
                                    }
                                    toolbar { onClickBack() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}