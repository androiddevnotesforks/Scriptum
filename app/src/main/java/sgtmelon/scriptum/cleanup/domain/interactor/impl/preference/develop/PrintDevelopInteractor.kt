package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop

import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.data.repository.database.DevelopRepo

class PrintDevelopInteractor(private val repository: DevelopRepo) : IPrintDevelopInteractor {

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