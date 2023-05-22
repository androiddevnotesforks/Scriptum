package sgtmelon.scriptum.develop.infrastructure.screen.service

import android.content.Context
import android.content.IntentFilter
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.develop.infrastructure.receiver.DevelopScreenReceiver
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.service.EternalService
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of service preferences.
 */
class ServiceDevelopFragment : PreferenceFragment(),
    DotAnimation.Callback,
    DevelopScreenReceiver.Callback {

    override val xmlId: Int = R.xml.preference_service

    private val binding = ServiceDevelopBinding(fragment = this)

    @Inject lateinit var viewModel: ServiceDevelopViewModel

    private val dotAnimation = DotAnimation(lifecycle, DotAnimType.COUNT, callback = this)

    private val receiver = DevelopScreenReceiver[this]

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getServiceBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setup() {
        binding.apply {
            serviceRefreshButton?.setOnClickListener { viewModel.startPing() }
            serviceRunButton?.setOnClickListener { startService(it.context) }
            serviceKillButton?.setOnClickListener {
                system?.broadcast?.sendEternalKill()
                viewModel.startPing()
            }

            system?.apply {
                notificationClearButton?.setOnClickListener { broadcast.sendClearBind() }
                alarmClearButton?.setOnClickListener { broadcast.sendClearAlarm() }
                notifyNotesButton?.setOnClickListener { broadcast.sendNotifyNotesBind() }
                notifyInfoButton?.setOnClickListener { broadcast.sendNotifyInfoBind() }
                notifyAlarmButton?.setOnClickListener { broadcast.sendTidyUpAlarm() }
            }
        }
    }

    override fun setupObservers() {
        viewModel.pingState.observe(this) { observePingState(it) }
    }

    private fun startService(context: Context) {
        EternalService.start(context)
        viewModel.startPing()
    }

    private fun observePingState(state: ServicePingState) {
        when (state) {
            ServicePingState.PREPARE -> onServicePrepare()
            ServicePingState.PING -> system?.broadcast?.sendEternalPing()
            ServicePingState.SUCCESS -> onServicePong(isSuccess = true)
            ServicePingState.NO_RESPONSE -> onServicePong(isSuccess = false)
        }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        context?.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.DEVELOP))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        context?.unregisterReceiver(receiver)
    }

    //endregion

    override fun onDotAnimationUpdate(text: String) {
        binding.serviceRefreshButton?.summary = text
    }

    override fun onReceiveEternalServicePong() {
        viewModel.interruptPing()

        /** Move this toast here, because after rotation it will appears. */
        system?.toast?.show(context, R.string.toast_dev_service_run)
    }

    private fun onServicePrepare() = with(binding) {
        dotAnimation.start(context, R.string.pref_summary_eternal_search)

        serviceRefreshButton?.isEnabled = false
        serviceRunButton?.isEnabled = false
        serviceKillButton?.isEnabled = false
    }

    private fun onServicePong(isSuccess: Boolean) = with(binding) {
        dotAnimation.stop()

        serviceRefreshButton?.summary = getString(R.string.pref_summary_eternal_refresh)
        serviceRefreshButton?.isEnabled = true
        serviceRunButton?.isEnabled = !isSuccess
        serviceKillButton?.isEnabled = isSuccess

        if (!isSuccess) {
            system?.toast?.show(context, R.string.toast_dev_service_fail)
        }
    }
}