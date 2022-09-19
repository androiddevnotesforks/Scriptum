package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmScreenState

interface IAlarmViewModel : NoteScreenReceiver.Callback {

    val state: LiveData<AlarmScreenState>

    val noteItem: LiveData<NoteItem>

    val alarmState: AlarmState

    fun setup(noteId: Long)

    fun postpone(repeat: Repeat?, timeArray: IntArray)
}