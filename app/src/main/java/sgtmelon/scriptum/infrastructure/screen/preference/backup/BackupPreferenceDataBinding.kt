package sgtmelon.scriptum.infrastructure.screen.preference.backup

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.findPreference

class BackupPreferenceDataBinding(val fragment: PreferenceFragmentCompat) {

    val exportButton: Preference? get() = fragment.findPreference(R.string.pref_key_backup_export)
    val importButton: Preference? get() = fragment.findPreference(R.string.pref_key_backup_import)

}