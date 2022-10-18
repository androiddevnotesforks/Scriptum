package sgtmelon.scriptum.domain.interactor.preferences

import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType

interface DevelopInteractor {

    suspend fun getList(type: PrintType): List<PrintItem>

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()
}