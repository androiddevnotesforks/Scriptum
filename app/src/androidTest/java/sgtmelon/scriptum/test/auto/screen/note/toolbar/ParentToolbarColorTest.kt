package sgtmelon.scriptum.test.auto.screen.note.toolbar

import sgtmelon.extension.nextString
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Parent class for tests of [NoteActivity] toolbar color with different themes
 */
abstract class ParentToolbarColorTest(@Theme private val theme: Int) : ParentUiTest(), IColorTest {

    override fun startTest(@Color value: Int) {
        setupTheme(theme)
        preferenceRepo.defaultColor = Color.list.filter { it != value }.random()

        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        when (val noteItem = data.createNote()) {
                            is NoteItem.Text -> {
                                createText(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(value).onClickApply() }
                                        onEnterText(nextString())
                                        onSave()
                                    }
                                    toolbar { onClickBack() }
                                }
                            }
                            is NoteItem.Roll -> {
                                createRoll(noteItem) {
                                    controlPanel {
                                        onColor { onClickItem(value).onClickApply() }
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