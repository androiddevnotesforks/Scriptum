package sgtmelon.scriptum.develop.domain

import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType

interface DevelopInteractor {

    suspend fun getList(type: PrintType): List<PrintItem>

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()
}