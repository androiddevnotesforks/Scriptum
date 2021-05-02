package sgtmelon.scriptum.presentation.factory

import androidx.annotation.StringDef
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.DevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment

/**
 * Factory for create/get fragments
 */
object FragmentFactory {

    class Note(private val fm: FragmentManager) {

        fun getTextNoteFragment(): TextNoteFragment? {
            return fm.findFragmentByTag(Tag.TEXT) as? TextNoteFragment
        }

        fun getRollNoteFragment(): RollNoteFragment? {
            return fm.findFragmentByTag(Tag.ROLL) as? RollNoteFragment
        }

        @StringDef(Tag.TEXT, Tag.ROLL)
        annotation class Tag {
            companion object {
                private const val PREFIX = "FRAGMENT_NOTE"

                const val TEXT = "${PREFIX}_TEXT"
                const val ROLL = "${PREFIX}_ROLL"
            }
        }
    }

    class Main(private val fm: FragmentManager) {

        fun getRankFragment(): RankFragment {
            return fm.findFragmentByTag(Tag.RANK) as? RankFragment ?: RankFragment()
        }

        fun getNotesFragment(): NotesFragment {
            return fm.findFragmentByTag(Tag.NOTES) as? NotesFragment ?: NotesFragment()
        }

        fun getBinFragment(): BinFragment {
            return fm.findFragmentByTag(Tag.BIN) as? BinFragment ?: BinFragment()
        }

        @StringDef(Tag.RANK, Tag.NOTES, Tag.BIN)
        annotation class Tag {
            companion object {
                private const val PREFIX = "FRAGMENT_MAIN"

                const val RANK = "${PREFIX}_RANK"
                const val NOTES = "${PREFIX}_NOTES"
                const val BIN = "${PREFIX}_BIN"
            }
        }
    }

    class Preference(private val fm: FragmentManager) {

        fun getPreferenceFragment(): PreferenceFragment {
            return fm.findFragmentByTag(Tag.PREF) as? PreferenceFragment ?: PreferenceFragment()
        }

        fun getDevelopFragment(): DevelopFragment {
            return fm.findFragmentByTag(Tag.DEVELOP) as? DevelopFragment ?: DevelopFragment()
        }

        @StringDef(Tag.PREF, Tag.DEVELOP)
        annotation class Tag {
            companion object {
                private const val PREFIX = "FRAGMENT_PREFERENCE"

                const val PREF = "${PREFIX}_PREF"
                const val DEVELOP = "${PREFIX}_DEVELOP"
            }
        }
    }
}