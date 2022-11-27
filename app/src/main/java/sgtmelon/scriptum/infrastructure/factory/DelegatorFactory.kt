package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.media.AudioManager
import androidx.lifecycle.Lifecycle
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmDelegator
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmDelegatorImpl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegator
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegatorImpl
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.ClipboardDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.PhoneAwakeDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.VibratorDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.melody.MelodyPlayDelegator

class DelegatorFactory(private val context: Context, private val lifecycle: Lifecycle?) {

    private var _bind: BindDelegator? = null
    val bind get() = _bind ?: BindDelegatorImpl(context).also { _bind = it }

    private var _alarm: AlarmDelegator? = null
    val alarm get() = _alarm ?: AlarmDelegatorImpl(context, toast).also { _alarm = it }

    private var _phoneAwake: PhoneAwakeDelegator? = null
    val phoneAwake get() = _phoneAwake ?: PhoneAwakeDelegator(context).also { _phoneAwake = it }

    private var _vibrator: VibratorDelegator? = null
    val vibrator get() = _vibrator ?: VibratorDelegator(context).also { _vibrator = it }

    private var _broadcast: BroadcastDelegator? = null
    val broadcast get() = _broadcast ?: BroadcastDelegator(context).also { _broadcast = it }

    private var _toast: ToastDelegator? = null
    val toast get() = _toast ?: ToastDelegator(lifecycle).also { _toast = it }

    private var _clipboard: ClipboardDelegator? = null
    val clipboard get() = _clipboard ?: ClipboardDelegator(context, toast).also { _clipboard = it }

    private var _alarmPlay: MelodyPlayDelegator? = null
    val alarmPlay: MelodyPlayDelegator
        get() = _alarmPlay ?: getPlayer(AudioManager.STREAM_ALARM).also { _alarmPlay = it }

    private var _musicPlay: MelodyPlayDelegator? = null
    val musicPlay: MelodyPlayDelegator
        get() = _musicPlay ?: getPlayer(AudioManager.STREAM_MUSIC).also { _musicPlay = it }

    private fun getPlayer(streamType: Int): MelodyPlayDelegator {
        return MelodyPlayDelegator(context, lifecycle, streamType)
    }
}