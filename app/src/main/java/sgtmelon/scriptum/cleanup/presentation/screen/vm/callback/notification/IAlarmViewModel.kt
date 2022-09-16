package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification.AlarmViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interface for communication [IAlarmActivity] with [AlarmViewModel].
 */
interface IAlarmViewModel : IParentViewModel, NoteScreenReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onStart()

    fun onClickDisable()

    fun onClickRepeat()

    fun onResultRepeatDialog(@IdRes itemId: Int)

    fun finishWithRepeat(repeat: Repeat? = null)

}