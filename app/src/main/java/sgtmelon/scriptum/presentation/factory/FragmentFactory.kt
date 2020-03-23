package sgtmelon.scriptum.presentation.factory

import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.presentation.screen.ui.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.main.RankFragment
import sgtmelon.scriptum.presentation.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.note.TextNoteFragment

/**
 * Factory for create/get fragments
 */
object FragmentFactory {

    class Note(private val fm: FragmentManager) {

        fun getTextNoteFragment(): TextNoteFragment? {
            return fm.findFragmentByTag(TEXT) as? TextNoteFragment
        }

        fun getRollNoteFragment(): RollNoteFragment? {
            return fm.findFragmentByTag(ROLL) as? RollNoteFragment
        }

        companion object {
            private const val PREFIX = "FRAGMENT_NOTE"

            const val TEXT = "${PREFIX}_TEXT"
            const val ROLL = "${PREFIX}_ROLL"
        }

    }

    class Main(private val fm: FragmentManager) {

        fun getRankFragment(): RankFragment {
            return fm.findFragmentByTag(RANK) as? RankFragment ?: RankFragment()
        }

        fun getNotesFragment(): NotesFragment {
            return fm.findFragmentByTag(NOTES) as? NotesFragment ?: NotesFragment()
        }

        fun getBinFragment(): BinFragment {
            return fm.findFragmentByTag(BIN) as? BinFragment ?: BinFragment()
        }

        companion object {
            private const val PREFIX = "FRAGMENT_MAIN"

            const val RANK = "${PREFIX}_RANK"
            const val NOTES = "${PREFIX}_NOTES"
            const val BIN = "${PREFIX}_BIN"
        }

    }

}