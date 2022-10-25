package sgtmelon.scriptum.develop.screen.service

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.service.EternalService
import sgtmelon.scriptum.develop.receiver.DevelopScreenReceiver
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of service preferences.
 */
class ServiceDevelopFragment : ParentPreferenceFragment(),
    DotAnimation.Callback,
    DevelopScreenReceiver.Callback {

    override val xmlId: Int = R.xml.preference_service

    private val binding = ServiceDevelopDataBinding(fragment = this)

    @Inject lateinit var viewModel: ServiceDevelopViewModel

    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    private val receiver = DevelopScreenReceiver[this]

    //region System

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.DEVELOP))
    }

    override fun inject(component: ScriptumComponent) {
        component.getServiceBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(receiver)
    }

    //endregion

    override fun setup() {
        binding.apply {
            serviceRefreshButton?.setOnClickListener { viewModel.startPing() }
            serviceRunButton?.setOnClickListener { startService(it.context) }
            serviceKillButton?.setOnClickListener {
                delegators.broadcast.sendEternalKill()
                viewModel.startPing()
            }

            delegators.apply {
                notificationClearButton?.setOnClickListener { broadcast.sendClearBind() }
                alarmClearButton?.setOnClickListener { broadcast.sendClearAlarm() }
                notifyNotesButton?.setOnClickListener { broadcast.sendNotifyNotesBind() }
                notifyInfoButton?.setOnClickListener { broadcast.sendNotifyInfoBind(count = null) }
                notifyAlarmButton?.setOnClickListener { broadcast.sendTidyUpAlarm() }
            }
        }
    }

    override fun setupObservers() {
        viewModel.pingState.observe(this) { onChangePingState(it) }
    }

    private fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EternalService.start(context)
            viewModel.startPing()
        } else {
            delegators.toast.show(context, R.string.toast_dev_low_api)
        }
    }

    private fun onChangePingState(state: ServicePingState) {
        when (state) {
            ServicePingState.PREPARE -> onServicePrepare()
            ServicePingState.PING -> delegators.broadcast.sendEternalPing()
            ServicePingState.SUCCESS -> onServicePong(isSuccess = true)
            ServicePingState.NO_RESPONSE -> onServicePong(isSuccess = false)
        }
    }

    override fun onDotAnimationUpdate(text: String) {
        binding.serviceRefreshButton?.summary = text
    }

    override fun onReceiveEternalServicePong() {
        viewModel.interruptPing()

        /** Move this toast here, because after rotation it will appears. */
        delegators.toast.show(context, R.string.toast_dev_service_run)
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
            delegators.toast.show(context, R.string.toast_dev_service_fail)
        }
    }
}