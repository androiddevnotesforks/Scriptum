package sgtmelon.scriptum.develop.infrastructure.screen.service

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.develop.infrastructure.receiver.DevelopScreenReceiver
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.service.EternalService
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimationImpl
import sgtmelon.textDotAnim.DotText
import javax.inject.Inject

/**
 * Fragment of service preferences.
 */
class ServiceDevelopFragment : PreferenceFragment<ServiceDevelopBinding>(),
    DotAnimationImpl.Callback,
    DevelopScreenReceiver.Callback {

    override val xmlId: Int = R.xml.preference_service

    override fun createBinding(): ServiceDevelopBinding = ServiceDevelopBinding(fragment = this)

    @Inject lateinit var viewModel: ServiceDevelopViewModel

    private val dotAnimation = DotAnimationImpl[lifecycle, DotAnimType.COUNT, this]

    private val receiver = DevelopScreenReceiver[this]
    override val receiverFilter = ReceiverFilter.DEVELOP
    override val receiverList get() = listOf(receiver)

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getServiceDevBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        binding?.apply {
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

    //endregion

    override fun onDotAnimationUpdate(text: DotText) {
        binding?.serviceRefreshButton?.summary = text.value
    }

    override fun onReceiveEternalServicePong() {
        viewModel.interruptPing()

        /** Move this toast here, because after rotation it will appears. */
        system?.toast?.show(context, R.string.toast_dev_service_run)
    }

    private fun onServicePrepare() {
        dotAnimation.start(context, R.string.pref_summary_eternal_search)

        binding?.serviceRefreshButton?.isEnabled = false
        binding?.serviceRunButton?.isEnabled = false
        binding?.serviceKillButton?.isEnabled = false
    }

    private fun onServicePong(isSuccess: Boolean) {
        dotAnimation.stop()

        binding?.serviceRefreshButton?.summary = getString(R.string.pref_summary_eternal_refresh)
        binding?.serviceRefreshButton?.isEnabled = true
        binding?.serviceRunButton?.isEnabled = !isSuccess
        binding?.serviceKillButton?.isEnabled = isSuccess

        if (!isSuccess) {
            system?.toast?.show(context, R.string.toast_dev_service_fail)
        }
    }
}