package sgtmelon.scriptum.ui.cases.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.ui.cases.value.ColorCase
import sgtmelon.test.common.nextString

/**
 * Parent class for test [ParentNoteFragmentImpl] screen colors.
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
                    var item: NoteItem? = null

                    when (type) {
                        NoteType.TEXT -> createText({ db.createText() }) { item = work(value) }
                        NoteType.ROLL -> createRoll({ db.createRoll() }) { item = work(value) }
                    }

                    assertItem(item!!)
                }
            }
        }
    }

    private fun TextNoteScreen.work(value: Color): NoteItem {
        controlPanel { onColor { select(value).apply() } }
        onEnterText(nextString())
        controlPanel { onSave() }
        toolbar { clickBack() }
        return item
    }

    private fun RollNoteScreen.work(value: Color): NoteItem.Roll {
        controlPanel { onColor { select(value).apply() } }
        enterPanel { onAdd(nextString()) }
        controlPanel { onSave() }
        toolbar { clickBack() }
        return item
    }
}