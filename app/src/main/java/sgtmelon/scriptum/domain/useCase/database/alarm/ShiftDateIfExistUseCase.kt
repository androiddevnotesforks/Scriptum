package sgtmelon.scriptum.domain.useCase.database.alarm

import java.util.Calendar

interface ShiftDateIfExistUseCase {

    suspend operator fun invoke(calendar: Calendar)
}