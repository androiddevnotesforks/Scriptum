package sgtmelon.scriptum.develop.infrastructure.screen.develop

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class DevelopBinding(fragment: PreferenceFragmentCompat) : PreferenceBinding(fragment) {

    val printNoteButton: Preference? get() = findPreference(R.string.pref_key_print_note)
    val printBinButton: Preference? get() = findPreference(R.string.pref_key_print_bin)
    val printRollButton: Preference? get() = findPreference(R.string.pref_key_print_roll)
    val printVisibleButton: Preference? get() = findPreference(R.string.pref_key_print_visible)
    val printRankButton: Preference? get() = findPreference(R.string.pref_key_print_rank)
    val printAlarmButton: Preference? get() = findPreference(R.string.pref_key_print_alarm)
    val printKeyButton: Preference? get() = findPreference(R.string.pref_key_print_key)
    val printFileButton: Preference? get() = findPreference(R.string.pref_key_print_file)

    val alarmButton: Preference? get() = findPreference(R.string.pref_key_screen_alarm)
    val eternalButton: Preference? get() = findPreference(R.string.pref_key_service_eternal)
    val resetButton: Preference? get() = findPreference(R.string.pref_key_other_reset)
}