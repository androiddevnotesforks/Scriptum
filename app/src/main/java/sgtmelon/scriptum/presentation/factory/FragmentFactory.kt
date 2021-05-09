package sgtmelon.scriptum.presentation.factory

import androidx.annotation.StringDef
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.*
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceFragment

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

        fun getFragment(screen: PreferenceScreen?): ParentPreferenceFragment? {
            return when (screen) {
                PreferenceScreen.PREFERENCE -> getPreferenceFragment()
                PreferenceScreen.BACKUP -> getBackupFragment()
                PreferenceScreen.NOTE -> getNoteFragment()
                PreferenceScreen.ALARM -> getAlarmFragment()
                PreferenceScreen.HELP -> getHelpFragment()
                PreferenceScreen.DEVELOP -> getDevelopFragment()
                PreferenceScreen.SERVICE -> getServiceFragment()
                null -> null
            }
        }

        fun getTag(screen: PreferenceScreen?): String? {
            return when (screen) {
                PreferenceScreen.PREFERENCE -> Tag.PREF
                PreferenceScreen.BACKUP -> Tag.BACKUP
                PreferenceScreen.NOTE -> Tag.NOTE
                PreferenceScreen.ALARM -> Tag.ALARM
                PreferenceScreen.HELP -> Tag.HELP
                PreferenceScreen.DEVELOP -> Tag.DEVELOP
                PreferenceScreen.SERVICE -> Tag.SERVICE
                null -> null
            }
        }

        private fun getPreferenceFragment(): PreferenceFragment {
            return fm.findFragmentByTag(Tag.PREF) as? PreferenceFragment ?: PreferenceFragment()
        }

        private fun getBackupFragment(): BackupPrefFragment {
            return fm.findFragmentByTag(Tag.BACKUP) as? BackupPrefFragment ?: BackupPrefFragment()
        }

        private fun getNoteFragment(): NotePrefFragment {
            return fm.findFragmentByTag(Tag.NOTE) as? NotePrefFragment ?: NotePrefFragment()
        }

        private fun getAlarmFragment(): AlarmPrefFragment {
            return fm.findFragmentByTag(Tag.ALARM) as? AlarmPrefFragment ?: AlarmPrefFragment()
        }

        private fun getHelpFragment(): HelpPrefFragment {
            return fm.findFragmentByTag(Tag.HELP) as? HelpPrefFragment ?: HelpPrefFragment()
        }

        private fun getDevelopFragment(): DevelopFragment {
            return fm.findFragmentByTag(Tag.DEVELOP) as? DevelopFragment ?: DevelopFragment()
        }

        private fun getServiceFragment(): ServiceFragment {
            return fm.findFragmentByTag(Tag.SERVICE) as? ServiceFragment ?: ServiceFragment()
        }

        @StringDef(Tag.PREF, Tag.BACKUP, Tag.NOTE, Tag.ALARM, Tag.HELP, Tag.DEVELOP, Tag.SERVICE)
        annotation class Tag {
            companion object {
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
}