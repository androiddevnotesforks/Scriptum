package sgtmelon.scriptum.ui.cases.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.note.updateComplete
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
                    val item = db.createNote(type)

                    when (item) {
                        is NoteItem.Text -> createText(item) { work(item, value) }
                        is NoteItem.Roll -> createRoll(item) { work(item, value) }
                    }

                    assertItem(item)
                }
            }
        }
    }

    private fun TextNoteScreen.work(item: NoteItem.Text, value: Color) {
        val text = nextString()

        controlPanel {
            onColor { select(value).apply() }
            onEnterText(text)
            onSave()
        }
        toolbar { clickBack() }

        item.color = value
        item.text = text
    }

    private fun RollNoteScreen.work(item: NoteItem.Roll, value: Color) {
        val text = nextString()

        controlPanel {
            onColor { select(value).apply() }
            enterPanel { onAdd(text) }
            onSave()
        }
        toolbar { clickBack() }

        item.color = value
        item.list.add(RollItem(position = 0, text = text))
        item.updateComplete()
    }

}