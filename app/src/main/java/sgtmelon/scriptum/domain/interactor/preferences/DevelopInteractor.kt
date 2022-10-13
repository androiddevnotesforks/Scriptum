package sgtmelon.scriptum.domain.interactor.preferences

import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.infrastructure.develop.PrintItem

interface DevelopInteractor {

    suspend fun getList(type: PrintType): List<PrintItem>

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()
}