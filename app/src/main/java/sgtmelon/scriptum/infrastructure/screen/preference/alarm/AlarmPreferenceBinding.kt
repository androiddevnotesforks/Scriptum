package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class AlarmPreferenceBinding(fragment: PreferenceFragmentCompat) : PreferenceBinding(fragment) {

    val signalButton: Preference? get() = findPreference(R.string.pref_key_alarm_signal)
    val repeatButton: Preference? get() = findPreference(R.string.pref_key_alarm_repeat)
    val melodyButton: Preference? get() = findPreference(R.string.pref_key_alarm_melody)
    val increaseButton: Preference? get() = findPreference(R.string.pref_key_alarm_increase)
    val volumeButton: Preference? get() = findPreference(R.string.pref_key_alarm_volume)
}