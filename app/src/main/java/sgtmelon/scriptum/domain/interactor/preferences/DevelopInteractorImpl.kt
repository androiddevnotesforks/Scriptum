package sgtmelon.scriptum.domain.interactor.preferences

import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.infrastructure.model.item.PrintItem

class DevelopInteractorImpl(private val repository: DevelopRepo) : DevelopInteractor {

    override suspend fun getList(type: PrintType): List<PrintItem> = when (type) {
        PrintType.NOTE -> repository.getPrintNoteList(isBin = false)
        PrintType.BIN -> repository.getPrintNoteList(isBin = true)
        PrintType.ROLL -> repository.getPrintRollList()
        PrintType.VISIBLE -> repository.getPrintVisibleList()
        PrintType.RANK -> repository.getPrintRankList()
        PrintType.ALARM -> repository.getPrintAlarmList()
        PrintType.KEY -> repository.getPrintPreferenceList()
        PrintType.FILE -> repository.getPrintFileList()
    }

    override suspend fun getRandomNoteId(): Long = repository.getRandomNoteId()

    override fun resetPreferences() = repository.resetPreferences()
}