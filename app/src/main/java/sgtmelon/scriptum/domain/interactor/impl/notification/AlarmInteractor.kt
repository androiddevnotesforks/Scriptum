package sgtmelon.scriptum.domain.interactor.impl.notification

import sgtmelon.extension.getCalendarWithAdd
import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.runMain
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel
import java.util.*

/**
 * Interactor for [IAlarmViewModel].
 */
class AlarmInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val alarmRepo: IAlarmRepo,
    private val noteRepo: INoteRepo,
    @RunPrivate var callback: IAlarmBridge?
) : ParentInteractor(),
    IAlarmInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Repeat override val repeat: Int get() = preferenceRepo.repeat

    override val volume: Int get() = preferenceRepo.volume

    override val volumeIncrease: Boolean get() = preferenceRepo.volumeIncrease


    override suspend fun getModel(id: Long): NoteItem? {
        /**
         * Delete before return noteModel for hide alarm icon.
         */
        alarmRepo.delete(id)

        return noteRepo.getItem(id, isOptimal = true)
    }

    override suspend fun setupRepeat(
        noteItem: NoteItem,
        valueArray: IntArray,
        @Repeat repeat: Int
    ) {
        val minute = valueArray.getOrNull(repeat) ?: return
        val calendar = getCalendarWithAdd(minute)

        checkDateExist(calendar)

        alarmRepo.insertOrUpdate(noteItem, calendar.getText())

        runMain { callback?.setAlarm(noteItem.id, calendar, showToast = false) }
    }

    @RunPrivate suspend fun checkDateExist(calendar: Calendar) {
        val dateList = alarmRepo.getList().map { it.alarm.date }

        while (dateList.contains(calendar.getText())) {
            calendar.add(Calendar.MINUTE, 1)
        }
    }
}