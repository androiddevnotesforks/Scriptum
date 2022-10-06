package sgtmelon.scriptum.infrastructure.screen.alarm

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

interface AlarmViewModel : UnbindNoteReceiver.Callback {

    val state: LiveData<AlarmScreenState>

    val noteItem: LiveData<NoteItem>

    val alarmState: AlarmState

    fun setup(noteId: Long)

    fun postpone(repeat: Repeat?, timeArray: IntArray)
}