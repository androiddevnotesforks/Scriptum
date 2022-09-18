package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification

import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interface for communication [IAlarmActivity] with [AlarmViewModel].
 */
interface IAlarmViewModel : IParentViewModel, NoteScreenReceiver.Callback {

    fun onStart()

    fun finishWithRepeat(repeat: Repeat? = null)

}