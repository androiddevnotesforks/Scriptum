package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.toolbar

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.ColorCase
import sgtmelon.test.common.nextString

/**
 * Parent class for tests of [NoteActivity] toolbar color with different themes
 */
abstract class ParentToolbarColorTest(private val theme: ThemeDisplayed) : ParentUiTest(),
    ColorCase {

    override fun startTest(value: Color) {
        setupTheme(theme)
        preferencesRepo.defaultColor = Color.values().filter { it != value }.random()

        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        when (val noteItem = db.createNote()) {
                            is NoteItem.Text -> {
                                createText(noteItem) {
                                    controlPanel {
                                        onColor { select(value).apply() }
                                        onEnterText(nextString())
                                        onSave()
                                    }
                                    toolbar { clickBack() }
                                }
                            }
                            is NoteItem.Roll -> {
                                createRoll(noteItem) {
                                    controlPanel {
                                        onColor { select(value).apply() }
                                        enterPanel { onAdd(nextString()) }
                                        onSave()
                                    }
                                    toolbar { clickBack() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}