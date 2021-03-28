package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.DevelopRepo
import sgtmelon.scriptum.domain.model.item.PrintItem

/**
 * Interface for communicate with [DevelopRepo]
 */
interface IDevelopRepo {

    suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note>

    suspend fun getPrintRollList(): List<PrintItem.Roll>

    suspend fun getPrintVisibleList(): List<PrintItem.Visible>

    suspend fun getPrintRankList(): List<PrintItem.Rank>

    suspend fun getPrintAlarmList(): List<PrintItem.Alarm>

    suspend fun getRandomNoteId(): Long
}