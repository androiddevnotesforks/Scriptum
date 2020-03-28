package sgtmelon.scriptum.presentation.screen.vm.callback.notification

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.AlarmViewModel

/**
 * Interface for communication [AlarmActivity] with [AlarmViewModel]
 */
interface IAlarmViewModel : IParentViewModel, NoteReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onStart()

    fun onClickNote()

    fun onClickDisable()

    fun onClickRepeat()

    fun onResultRepeatDialog(@IdRes itemId: Int)

}