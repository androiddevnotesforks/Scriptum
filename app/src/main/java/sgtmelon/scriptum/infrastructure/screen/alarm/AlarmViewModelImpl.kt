package sgtmelon.scriptum.infrastructure.screen.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.screen.alarm.state.ScreenState
import sgtmelon.scriptum.infrastructure.utils.extensions.record

class AlarmViewModelImpl(
    private val noteId: Long,
    private val preferencesRepo: PreferencesRepo,
    private val noteRepo: NoteRepo,
    private val getMelodyList: GetMelodyListUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val shiftDateIfExist: ShiftDateIfExistUseCase
) : ViewModel(),
    AlarmViewModel {

    override val state: MutableLiveData<ScreenState> = MutableLiveData()

    override val noteItem: MutableLiveData<NoteItem> = MutableLiveData()

    override val alarmState: AlarmState get() = preferencesRepo.alarmState

    init {
        viewModelScope.launchBack { fetchData(noteId) }
    }

    private suspend fun fetchData(noteId: Long) {
        /**
         * Delete before setting [noteItem]. This is need for hide alarm icon and
         * decrement statusBar notification info count (about future alarms).
         */
        deleteNotification(noteId)

        val item = noteRepo.getItem(noteId, isOptimal = true)
        if (item != null) {
            noteItem.postValue(item)
        } else {
            state.postValue(ScreenState.Close)
            return
        }

        val melodyUri = if (preferencesRepo.signalState.isMelody) {
            preferencesRepo.getMelodyUri(getMelodyList())
        } else {
            null
        }

        state.postValue(ScreenState.Setup(item, melodyUri))
    }

    override fun postpone(repeat: Repeat?, timeArray: IntArray) {
        val actualRepeat = repeat ?: preferencesRepo.repeat

        val noteItem = noteItem.value
        val minute = timeArray.getOrNull(actualRepeat.ordinal)

        if (noteItem == null || minute == null) {
            NullPointerException(
                "noteItem=${noteItem == null} | minute=${minute == null} | repeat=$actualRepeat"
            ).record()
            state.postValue(ScreenState.Close)
            return
        }

        viewModelScope.launchBack {
            val calendar = getClearCalendar(minute)

            shiftDateIfExist(calendar)
            setNotification(noteItem, calendar)
            state.postValue(ScreenState.Postpone(noteId, actualRepeat, calendar))
        }
    }

    /**
     * Calls on note notification cancel (unbind) from statusBar. This needed for update
     * bind indicator.
     */
    override fun onReceiveUnbindNote(noteId: Long) {
        val noteItem = noteItem.value ?: return

        if (!noteItem.isStatus || noteItem.id != noteId) return

        noteItem.isStatus = false
        this.noteItem.postValue(noteItem)
    }
}