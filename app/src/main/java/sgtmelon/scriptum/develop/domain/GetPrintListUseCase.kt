package sgtmelon.scriptum.develop.domain

import sgtmelon.scriptum.develop.data.DevelopRepo
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType

class GetPrintListUseCase(private val repository: DevelopRepo) {

    suspend operator fun invoke(type: PrintType): List<PrintItem> = when (type) {
        PrintType.NOTE -> repository.getPrintNoteList(isBin = false)
        PrintType.BIN -> repository.getPrintNoteList(isBin = true)
        PrintType.ROLL -> repository.getPrintRollList()
        PrintType.VISIBLE -> repository.getPrintVisibleList()
        PrintType.RANK -> repository.getPrintRankList()
        PrintType.ALARM -> repository.getPrintAlarmList()
        PrintType.KEY -> repository.getPrintPreferenceList()
        PrintType.FILE -> repository.getPrintFileList()
    }
}