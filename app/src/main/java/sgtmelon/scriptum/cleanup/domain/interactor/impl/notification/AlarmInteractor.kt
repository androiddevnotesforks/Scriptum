package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import java.util.Calendar
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interactor for [IAlarmViewModel].
 */
class AlarmInteractor(
    private val alarmRepo: AlarmRepo,
    private val noteRepo: NoteRepo
) : ParentInteractor(),
    IAlarmInteractor {

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
        repeat: Repeat
    ): Calendar? {
        val minute = valueArray.getOrNull(repeat.ordinal) ?: return null
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