package sgtmelon.scriptum.develop.infrastructure.screen.service

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class ServiceDevelopBinding(fragment: PreferenceFragmentCompat) : PreferenceBinding(fragment) {

    val serviceRefreshButton: Preference? get() = findPreference(R.string.pref_key_service_refresh)
    val serviceRunButton: Preference? get() = findPreference(R.string.pref_key_service_run)
    val serviceKillButton: Preference? get() = findPreference(R.string.pref_key_service_kill)

    val notificationClearButton: Preference? get() = findPreference(R.string.pref_key_notification_clear)
    val alarmClearButton: Preference? get() = findPreference(R.string.pref_key_alarm_clear)
    val notifyNotesButton: Preference? get() = findPreference(R.string.pref_key_notify_notes)
    val notifyInfoButton: Preference? get() = findPreference(R.string.pref_key_notify_info)
    val notifyAlarmButton: Preference? get() = findPreference(R.string.pref_key_notify_alarm)

}