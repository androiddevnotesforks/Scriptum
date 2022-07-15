package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import java.util.*

/**
 * Interactor for [IAlarmViewModel].
 */
class AlarmInteractor(
    private val preferenceRepo: AppPreferences,
    private val alarmRepo: IAlarmRepo,
    private val noteRepo: INoteRepo
) : ParentInteractor(),
    IAlarmInteractor {

    @Repeat override val repeat: Int get() = preferenceRepo.repeat

    override val volume: Int get() = preferenceRepo.volume

    override val volumeIncrease: Boolean get() = preferenceRepo.volumeIncrease


    override suspend fun getModel(id: Long): NoteItem? {
        /**
         * Delete before return noteModel. This is need for hide alarm icon and decrement
         * notification info count (next alarms count).
         */
        alarmRepo.delete(id)

        return noteRepo.getItem(id, isOptimal = true)
    }

    override suspend fun setupRepeat(
        item: NoteItem,
        valueArray: IntArray,
        @Repeat repeat: Int
    ): Calendar? {
        val minute = valueArray.getOrNull(repeat) ?: return null
        val calendar = getCalendarWithAdd(minute)

        checkDateExist(calendar)

        alarmRepo.insertOrUpdate(item, calendar.getText())

        return calendar
    }

    @RunPrivate suspend fun checkDateExist(calendar: Calendar) {
        val dateList = alarmRepo.getList().map { it.alarm.date }

        while (dateList.contains(calendar.getText())) {
            calendar.add(Calendar.MINUTE, 1)
        }
    }
}