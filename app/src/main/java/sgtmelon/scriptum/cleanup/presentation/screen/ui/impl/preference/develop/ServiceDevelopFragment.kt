package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.service.EternalService
import sgtmelon.scriptum.infrastructure.develop.screen.print.ServiceDevelopDataBinding
import sgtmelon.scriptum.infrastructure.develop.screen.print.ServicePingState
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

    private val binding = ServiceDevelopDataBinding(lifecycle, fragment = this)

    @Inject lateinit var viewModel: IServiceDevelopViewModel

    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    private val receiver by lazy { DevelopScreenReceiver[this] }

    //region System

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()

        viewModel.pingState.observe(viewLifecycleOwner) { onChangePingState(it) }

        context?.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.DEVELOP))
    }

    override fun inject(component: ScriptumComponent) {
        component.getServiceBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(receiver)
    }

    //endregion

    private fun setup() = binding.apply {
        serviceRefreshButton?.setOnClickListener { viewModel.startPing() }
        serviceRunButton?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                EternalService.start(it.context)
                viewModel.startPing()
            } else {
                delegators.toast.show(it.context, R.string.toast_dev_low_api)
            }
        }
        serviceKillButton?.setOnClickListener {
            delegators.broadcast.sendEternalKill()
            viewModel.startPing()
        }

        with(delegators) {
            notificationClearButton?.setOnClickListener { broadcast.sendClearBind() }
            alarmClearButton?.setOnClickListener { broadcast.sendClearAlarm() }
            notifyNotesButton?.setOnClickListener { broadcast.sendNotifyNotesBind() }
            notifyInfoButton?.setOnClickListener { broadcast.sendNotifyInfoBind(count = null) }
            notifyAlarmButton?.setOnClickListener { broadcast.sendTidyUpAlarm() }
        }
    }

    private fun onChangePingState(state: ServicePingState) {
        val context = context ?: return

        when (state) {
            ServicePingState.PREPARE -> {
                binding.serviceRefreshButton?.isEnabled = false
                binding.serviceRunButton?.isEnabled = false
                binding.serviceKillButton?.isEnabled = false

                dotAnimation.start(context, R.string.pref_summary_eternal_search)
            }
            ServicePingState.PING -> delegators.broadcast.sendEternalPing()
            ServicePingState.NO_RESPONSE -> onServicePong(isSuccess = false)
        }
    }

    override fun onDotAnimationUpdate(text: CharSequence) {
        binding.serviceRefreshButton?.summary = text
    }

    override fun onReceiveEternalServicePong() {
        viewModel.cancelPing()
        onServicePong(isSuccess = true)
    }

    private fun onServicePong(isSuccess: Boolean) {
        val context = context ?: return

        dotAnimation.stop()

        binding.serviceRefreshButton?.summary = getString(R.string.pref_summary_eternal_refresh)
        binding.serviceRefreshButton?.isEnabled = true
        binding.serviceRunButton?.isEnabled = !isSuccess
        binding.serviceKillButton?.isEnabled = isSuccess

        val toastId = if (isSuccess) {
            R.string.toast_dev_service_run
        } else {
            R.string.toast_dev_service_fail
        }
        delegators.toast.show(context, toastId)
    }
}