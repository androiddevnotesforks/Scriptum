package sgtmelon.scriptum.develop.screen.print

import androidx.lifecycle.Lifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceBinding
import sgtmelon.scriptum.infrastructure.utils.findPreference

class ServiceDevelopDataBinding(
    lifecycle: Lifecycle,
    fragment: PreferenceFragmentCompat?
) : ParentPreferenceBinding(lifecycle, fragment) {

    val serviceRefreshButton: Preference? get() = fragment?.findPreference(R.string.pref_key_service_refresh)
    val serviceRunButton: Preference? get() = fragment?.findPreference(R.string.pref_key_service_run)
    val serviceKillButton: Preference? get() = fragment?.findPreference(R.string.pref_key_service_kill)

    val notificationClearButton: Preference? get() = fragment?.findPreference(R.string.pref_key_notification_clear)
    val alarmClearButton: Preference? get() = fragment?.findPreference(R.string.pref_key_alarm_clear)
    val notifyNotesButton: Preference? get() = fragment?.findPreference(R.string.pref_key_notify_notes)
    val notifyInfoButton: Preference? get() = fragment?.findPreference(R.string.pref_key_notify_info)
    val notifyAlarmButton: Preference? get() = fragment?.findPreference(R.string.pref_key_notify_alarm)

}