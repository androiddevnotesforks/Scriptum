package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification

import android.os.Bundle
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

    fun finishWithRepeat(repeat: Repeat? = null)

}