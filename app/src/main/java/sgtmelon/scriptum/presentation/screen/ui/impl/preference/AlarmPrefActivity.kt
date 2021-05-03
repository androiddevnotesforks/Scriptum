package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceActivity

/**
 * Screen for display [AlarmPrefFragment]
 */
class AlarmPrefActivity : ParentPreferenceActivity(
    R.layout.activity_preference_alarm,
    R.id.alarm_pref_parent_container,
    R.id.alarm_pref_fragment_container,
    R.string.pref_header_alarm
) {

    override val tag: String = FragmentFactory.Preference.Tag.ALARM

    override val fragment by lazy { fragmentFactory.getAlarmFragment() }

    companion object {
        operator fun get(context: Context) = Intent(context, AlarmPrefActivity::class.java)
    }
}