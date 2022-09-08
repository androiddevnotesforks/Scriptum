package sgtmelon.scriptum.domain.useCase.alarm

import java.util.Calendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.data.repository.database.AlarmRepo

class ShiftDateIfExistUseCaseImpl(val repository: AlarmRepo) : ShiftDateIfExistUseCase {

    override suspend operator fun invoke(calendar: Calendar) {
        val dateList = repository.getDateList()

        while (dateList.contains(calendar.getText())) {
            calendar.add(Calendar.MINUTE, 1)
        }
    }
}