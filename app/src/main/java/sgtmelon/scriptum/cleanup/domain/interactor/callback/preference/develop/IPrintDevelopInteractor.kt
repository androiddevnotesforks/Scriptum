package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType

interface IPrintDevelopInteractor {

    suspend fun getList(type: PrintType): List<PrintItem>

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()
}