package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.findPreference

class AlarmPreferenceDataBinding(val fragment: PreferenceFragmentCompat) {

    val signalButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm_signal)
    val repeatButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm_repeat)
    val melodyButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm_melody)
    val increaseButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm_increase)
    val volumeButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm_volume)
}