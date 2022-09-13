package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IServiceDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.service.EternalService
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of service preferences.
 */
class ServiceDevelopFragment : ParentPreferenceFragment(),
    IServiceDevelopFragment,
    DotAnimation.Callback {

    @Inject internal lateinit var viewModel: IServiceDevelopViewModel

    private val refreshPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_refresh)) }
    private val runPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_run)) }
    private val killPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_kill)) }

    private val alarmClear by lazy { findPreference<Preference>(getString(R.string.pref_key_service_alarm_clear)) }
    private val notificationClear by lazy { findPreference<Preference>(getString(R.string.pref_key_service_notification_clear)) }
    private val notifyNotes by lazy { findPreference<Preference>(getString(R.string.pref_key_service_notify_notes)) }
    private val notifyInfo by lazy { findPreference<Preference>(getString(R.string.pref_key_service_notify_info)) }
    private val notifyAlarm by lazy { findPreference<Preference>(getString(R.string.pref_key_service_notify_alarm)) }

    private val broadcast by lazy { BroadcastDelegator(context) }
    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    private val receiver by lazy { DevelopScreenReceiver[viewModel] }

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_service, rootKey)

        ScriptumApplication.component.getServiceBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onSetup()
        context?.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.DEVELOP))
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
        context?.unregisterReceiver(receiver)
    }

    //endregion

    override fun setup() {
        refreshPreference?.setOnPreferenceClickListener {
            viewModel.onClickRefresh()
            return@setOnPreferenceClickListener true
        }
        runPreference?.setOnPreferenceClickListener {
            viewModel.onClickRun()
            return@setOnPreferenceClickListener true
        }
        killPreference?.setOnPreferenceClickListener {
            viewModel.onClickKill()
            return@setOnPreferenceClickListener true
        }

        alarmClear?.setOnPreferenceClickListener {
            broadcast.sendClearAlarm()
            return@setOnPreferenceClickListener true
        }
        notificationClear?.setOnPreferenceClickListener {
            broadcast.sendClearBind()
            return@setOnPreferenceClickListener true
        }
        notifyNotes?.setOnPreferenceClickListener {
            broadcast.sendNotifyNotesBind()
            return@setOnPreferenceClickListener true
        }
        notifyInfo?.setOnPreferenceClickListener {
            broadcast.sendNotifyInfoBind(count = null)
            return@setOnPreferenceClickListener true
        }
        notifyAlarm?.setOnPreferenceClickListener {
            broadcast.sendTidyUpAlarm()
            return@setOnPreferenceClickListener true
        }
    }

    override fun updateRefreshEnabled(isEnabled: Boolean) {
        refreshPreference?.isEnabled = isEnabled
    }

    override fun resetRefreshSummary() {
        refreshPreference?.summary = getString(R.string.pref_summary_eternal_refresh)
    }

    override fun updateRunEnabled(isEnabled: Boolean) {
        runPreference?.isEnabled = isEnabled
    }

    override fun updateKillEnabled(isEnabled: Boolean) {
        killPreference?.isEnabled = isEnabled
    }

    override fun startPingSummary() {
        val context = context ?: return

        dotAnimation.start(context, R.string.pref_summary_eternal_search)
    }

    override fun stopPingSummary() = dotAnimation.stop()

    override fun onDotAnimUpdate(text: CharSequence) {
        refreshPreference?.summary = text
    }

    override fun sendPingBroadcast() = broadcast.sendEternalPing()

    override fun sendKillBroadcast() = broadcast.sendEternalKill()

    override fun runService() {
        val context = context?.applicationContext ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EternalService.start(context)
        }
    }
}