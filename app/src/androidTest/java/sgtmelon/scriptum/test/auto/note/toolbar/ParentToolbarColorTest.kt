package sgtmelon.scriptum.test.auto.note.toolbar

import sgtmelon.extension.nextString
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.test.IColorTest
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Parent class for tests of [NoteActivity] toolbar color with different themes
 */
abstract class ParentToolbarColorTest(@Theme private val theme: Int) : ParentUiTest(), IColorTest {

    override fun startTest(@Color color: Int) {
        preferenceRepo.theme = theme
        preferenceRepo.defaultColor = Color.list.filter { it != color }.random()

        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        when(val noteItem = data.createNote()) {
                            is NoteItem.Text -> {
                                createText(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(color).onClickApply() }
                                        onEnterText(nextString())
                                        onSave()
                                    }
                                    toolbar { onClickBack() }
                                }
                            }
                            is NoteItem.Roll -> {
                                createRoll(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(color).onClickApply() }
                                        enterPanel { onAdd(nextString()) }
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