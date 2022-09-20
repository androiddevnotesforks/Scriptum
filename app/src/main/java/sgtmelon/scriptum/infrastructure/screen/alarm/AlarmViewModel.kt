package sgtmelon.scriptum.infrastructure.screen.alarm

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.state.AlarmState

interface AlarmViewModel : NoteScreenReceiver.Callback {

    val state: LiveData<AlarmScreenState>

    val noteItem: LiveData<NoteItem>

    val alarmState: AlarmState

    fun setup(noteId: Long)

    fun postpone(repeat: Repeat?, timeArray: IntArray)
}