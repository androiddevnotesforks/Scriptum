package sgtmelon.scriptum.test.ui.auto.screen.note.toolbar

import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IColorTest

/**
 * Parent class for tests of [NoteActivity] toolbar color with different themes
 */
abstract class ParentToolbarColorTest(private val theme: ThemeDisplayed) : ParentUiTest(),
    IColorTest {

    override fun startTest(value: Color) {
        setupTheme(theme)
        preferencesRepo.defaultColor = Color.values().filter { it != value }.random()

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