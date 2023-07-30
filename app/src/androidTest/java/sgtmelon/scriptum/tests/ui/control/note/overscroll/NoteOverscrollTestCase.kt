package sgtmelon.scriptum.tests.ui.control.note.overscroll

import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.source.cases.value.ColorCase
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchRollNote
import sgtmelon.scriptum.source.ui.tests.launchTextNote
import sgtmelon.test.common.nextString

/**
 * Parent class for test overscroll color in [TextNoteFragment]/[RollNoteFragment]. Need run tests
 * in Android Q (API 29), check [TextNoteFragment.observeColor].
 */
abstract class NoteOverscrollTestCase(
    private val theme: ThemeDisplayed,
    private val type: NoteType
) : ParentUiTest(),
    ColorCase {

    override fun startTest(value: Color) {
        setupTheme(theme)
        preferencesRepo.defaultColor = value

        when (type) {
            NoteType.TEXT -> {
                val item = db.insertText(db.textNote.apply {
                    text = nextString(TEXT_SIZE)
                })

                launchTextNote(item = item) { scrollTo(Scroll.START, SCROLL_REPEAT) }
            }
            NoteType.ROLL -> {
                val item = db.insertRoll(list = db.getRollList(LIST_SIZE), isVisible = true)

                launchRollNote(item = item) { scrollTo(Scroll.START, SCROLL_REPEAT) }
            }
        }
    }

    private companion object {
        const val TEXT_SIZE = 100
        const val LIST_SIZE = 15
        const val SCROLL_REPEAT = 15
    }
}