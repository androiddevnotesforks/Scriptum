package sgtmelon.scriptum.screen.vm.callback.notification

import android.os.Bundle
import androidx.annotation.IdRes
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

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