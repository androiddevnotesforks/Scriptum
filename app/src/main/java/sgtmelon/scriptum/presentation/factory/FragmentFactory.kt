package sgtmelon.scriptum.presentation.factory

import androidx.annotation.StringDef
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.extension.getFragmentByTag
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpPreferenceFragment

/**
 * Factory for create/get fragments
 */
object FragmentFactory {

    class Note(private val fm: FragmentManager) {

        fun getTextNoteFragment(): TextNoteFragment? = fm.getFragmentByTag(Tag.TEXT)

        fun getRollNoteFragment(): RollNoteFragment? = fm.getFragmentByTag(Tag.ROLL)

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

        fun getRankFragment(): RankFragment = fm.getFragmentByTag(Tag.RANK) ?: RankFragment()

        fun getNotesFragment(): NotesFragment = fm.getFragmentByTag(Tag.NOTES) ?: NotesFragment()

        fun getBinFragment(): BinFragment = fm.getFragmentByTag(Tag.BIN) ?: BinFragment()

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
            return fm.getFragmentByTag(Tag.PREF) ?: PreferenceFragment()
        }

        private fun getBackupFragment(): BackupPreferenceFragment {
            return fm.getFragmentByTag(Tag.BACKUP) ?: BackupPreferenceFragment()
        }

        private fun getNoteFragment(): NotePreferenceFragment {
            return fm.getFragmentByTag(Tag.NOTE) ?: NotePreferenceFragment()
        }

        private fun getAlarmFragment(): AlarmPreferenceFragment {
            return fm.getFragmentByTag(Tag.ALARM) ?: AlarmPreferenceFragment()
        }

        private fun getHelpFragment(): HelpPreferenceFragment {
            return fm.getFragmentByTag(Tag.HELP) ?: HelpPreferenceFragment()
        }

        private fun getDevelopFragment(): DevelopFragment {
            return fm.getFragmentByTag(Tag.DEVELOP) ?: DevelopFragment()
        }

        private fun getServiceFragment(): ServiceDevelopFragment {
            return fm.getFragmentByTag(Tag.SERVICE) ?: ServiceDevelopFragment()
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