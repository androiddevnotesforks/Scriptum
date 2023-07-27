package sgtmelon.scriptum.source.cases.note

import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.test.common.nextString

/**
 * Parent class for test [ParentNoteFragment] screen colors.
 */
abstract class NoteUIColorTestCase(
    private val theme: ThemeDisplayed,
    private val type: NoteType
) : ParentUiTest(),
    ColorCase {

    override fun startTest(value: Color) {
        setupTheme(theme)
        preferencesRepo.defaultColor = Color.values().filter { it != value }.random()

        launchMain {
            openNotes(isEmpty = true) {
                openAddDialog {
                    when (type) {
                        NoteType.TEXT -> createText({ db.createText() }) { work(value) }
                        NoteType.ROLL -> createRoll({ db.createRoll() }) { work(value) }
                    }
                }
            }
        }
    }

    private fun TextNoteScreen.work(value: Color) {
        controlPanel { onColor { select(value).apply() } }
        onEnterText(nextString())
        controlPanel { onSave() }
    }

    private fun RollNoteScreen.work(value: Color) {
        controlPanel { onColor { select(value).apply() } }
        enterPanel { onAdd(nextString()) }
        controlPanel { onSave() }
    }
}