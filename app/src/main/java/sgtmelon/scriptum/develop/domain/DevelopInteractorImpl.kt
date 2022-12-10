package sgtmelon.scriptum.develop.domain

import sgtmelon.scriptum.develop.data.DevelopRepo
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType

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