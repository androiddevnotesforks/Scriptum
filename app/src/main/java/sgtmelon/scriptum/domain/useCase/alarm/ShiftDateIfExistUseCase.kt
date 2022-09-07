package sgtmelon.scriptum.domain.useCase.alarm

import java.util.Calendar

interface ShiftDateIfExistUseCase {

    suspend operator fun invoke(calendar: Calendar)
}