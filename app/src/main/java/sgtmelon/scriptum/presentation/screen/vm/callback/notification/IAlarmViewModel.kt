package sgtmelon.scriptum.presentation.screen.vm.callback.notification

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.AlarmViewModel

/**
 * Interface for communication [IAlarmActivity] with [AlarmViewModel].
 */
interface IAlarmViewModel : IParentViewModel, NoteScreenReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onStart()

    fun onClickNote()

    fun onClickDisable()

    fun onClickRepeat()

    fun onResultRepeatDialog(@IdRes itemId: Int)

}