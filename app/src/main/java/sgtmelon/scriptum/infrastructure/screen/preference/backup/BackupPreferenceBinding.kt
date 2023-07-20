package sgtmelon.scriptum.infrastructure.screen.preference.backup

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class BackupPreferenceBinding(fragment: PreferenceFragmentCompat): PreferenceBinding(fragment) {

    val exportButton: Preference? get() = findPreference(R.string.pref_key_backup_export)
    val importButton: Preference? get() = findPreference(R.string.pref_key_backup_import)
}