package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import androidx.lifecycle.Lifecycle
import sgtmelon.scriptum.cleanup.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.PhoneAwakeDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.VibratorDelegator

class DelegatorFactory(private val context: Context, private val lifecycle: Lifecycle) {

    private var _phoneAwake: PhoneAwakeDelegator? = null
    val phoneAwake get() = _phoneAwake ?: PhoneAwakeDelegator(context).also { _phoneAwake = it }

    private var _vibrator: VibratorDelegator? = null
    val vibrator get() = _vibrator ?: VibratorDelegator(context).also { _vibrator = it }

    private var _broadcast: BroadcastDelegator? = null
    val broadcast get() = _broadcast ?: BroadcastDelegator(context).also { _broadcast = it }

    private var _toast: ToastDelegator? = null
    val toast get() = _toast ?: ToastDelegator(lifecycle).also { _toast = it }

    private var _clipboard: ClipboardControl? = null
    val clipboard get() = _clipboard ?: ClipboardControl(context, toast)

}