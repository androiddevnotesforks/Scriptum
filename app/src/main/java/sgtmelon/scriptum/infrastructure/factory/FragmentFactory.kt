package sgtmelon.scriptum.infrastructure.factory

import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.extension.getFragmentByTag
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment
import sgtmelon.scriptum.infrastructure.develop.screen.develop.DevelopFragment
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment

/**
 * Factory for create/get fragments.
 */
object FragmentFactory {

    class Note(private val fm: FragmentManager) {

        fun getTextNote(): TextNoteFragment? = fm.getFragmentByTag(Tag.TEXT)

        fun getRollNote(): RollNoteFragment? = fm.getFragmentByTag(Tag.ROLL)

        object Tag {
            private const val PREFIX = "FRAGMENT_NOTE"
            const val TEXT = "${PREFIX}_TEXT"
            const val ROLL = "${PREFIX}_ROLL"
        }
    }

    class Main(private val fm: FragmentManager) {

        fun getRank(): RankFragment = fm.getFragmentByTag(Tag.RANK) ?: RankFragment()

        fun getNotes(): NotesFragment = fm.getFragmentByTag(Tag.NOTES) ?: NotesFragment()

        fun getBin(): BinFragment = fm.getFragmentByTag(Tag.BIN) ?: BinFragment()

        object Tag {
            private const val PREFIX = "FRAGMENT_MAIN"
            const val RANK = "${PREFIX}_RANK"
            const val NOTES = "${PREFIX}_NOTES"
            const val BIN = "${PREFIX}_BIN"
        }
    }

    class Preference(private val fm: FragmentManager) {

        fun get(screen: PreferenceScreen?): Pair<ParentPreferenceFragment, String>? {
            if (screen == null) return null

            val tag = getTag(screen)
            val fragment: ParentPreferenceFragment = fm.getFragmentByTag(tag) ?: when (screen) {
                PreferenceScreen.PREFERENCE -> PreferenceFragment()
                PreferenceScreen.BACKUP -> BackupPreferenceFragment()
                PreferenceScreen.NOTE -> NotePreferenceFragment()
                PreferenceScreen.ALARM -> AlarmPreferenceFragment()
                PreferenceScreen.HELP -> HelpPreferenceFragment()
                PreferenceScreen.DEVELOP -> DevelopFragment()
                PreferenceScreen.SERVICE -> ServiceDevelopFragment()
            }

            return fragment to tag
        }

        private fun getTag(screen: PreferenceScreen): String {
            return when (screen) {
                PreferenceScreen.PREFERENCE -> Tag.PREF
                PreferenceScreen.BACKUP -> Tag.BACKUP
                PreferenceScreen.NOTE -> Tag.NOTE
                PreferenceScreen.ALARM -> Tag.ALARM
                PreferenceScreen.HELP -> Tag.HELP
                PreferenceScreen.DEVELOP -> Tag.DEVELOP
                PreferenceScreen.SERVICE -> Tag.SERVICE
            }
        }

        object Tag {
            private const val PREFIX = "FRAGMENT_PREFERENCE"
            const val PREF = "${PREFIX}_PREF"
            const val BACKUP = "${PREFIX}_BACKUP"
            const val NOTE = "${PREFIX}_NOTE"
            const val ALARM = "${PREFIX}_ALARM"
            const val HELP = "${PREFIX}_HELP"
            const val DEVELOP = "${PREFIX}_DEVELOP"
            const val SERVICE = "${PREFIX}_SERVICE"
        }
    }
}