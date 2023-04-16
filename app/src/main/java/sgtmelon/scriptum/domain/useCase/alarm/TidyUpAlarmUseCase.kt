package sgtmelon.scriptum.domain.useCase.alarm

import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.domain.model.result.TidyUpResult

class TidyUpAlarmUseCase(val repository: AlarmRepo) {

    suspend operator fun invoke(): Flow<TidyUpResult> = flowOnBack {
        repository.getList().forEach {
            val noteId = it.note.id
            val calendar = it.alarm.date.toCalendar()

            if (calendar.isBeforeNow) {
                emit(TidyUpResult.Cancel(noteId))
                repository.delete(noteId)
            } else {
                emit(TidyUpResult.Update(noteId, calendar))
            }
        }
    }
}