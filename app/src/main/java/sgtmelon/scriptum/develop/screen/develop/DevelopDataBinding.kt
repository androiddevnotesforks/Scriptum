package sgtmelon.scriptum.develop.screen.develop

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.findPreference

class DevelopDataBinding(val fragment: PreferenceFragmentCompat) {

    val printNoteButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_note)
    val printBinButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_bin)
    val printRollButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_roll)
    val printVisibleButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_visible)
    val printRankButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_rank)
    val printAlarmButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_alarm)
    val printKeyButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_key)
    val printFileButton: Preference? get() = fragment.findPreference(R.string.pref_key_print_file)

    val alarmButton: Preference? get() = fragment.findPreference(R.string.pref_key_screen_alarm)
    val eternalButton: Preference? get() = fragment.findPreference(R.string.pref_key_service_eternal)
    val resetButton: Preference? get() = fragment.findPreference(R.string.pref_key_other_reset)
}