package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.repository.room.callback.DevelopRepo
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource

/**
 * Repository which work with all data and only for developer screen.
 */
class DevelopRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val rollDataSource: RollDataSource,
    private val rollVisibleDataSource: RollVisibleDataSource,
    private val rankDataSource: RankDataSource,
    private val alarmDataSource: AlarmDataSource
) : DevelopRepo {

    override suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note> {
        return noteDataSource.getList(isBin).map { PrintItem.Note(it) }
    }

    override suspend fun getPrintRollList(): List<PrintItem.Roll> {
        return rollDataSource.getList().map { PrintItem.Roll(it) }
    }

    override suspend fun getPrintVisibleList(): List<PrintItem.Visible> {
        return rollVisibleDataSource.getList().map { PrintItem.Visible(it) }
    }

    override suspend fun getPrintRankList(): List<PrintItem.Rank> {
        return rankDataSource.getList().map { PrintItem.Rank(it) }
    }

    override suspend fun getPrintAlarmList(): List<PrintItem.Alarm> {
        return alarmDataSource.getList().map { PrintItem.Alarm(it) }
    }

    override suspend fun getRandomNoteId(): Long {
        return noteDataSource.getList(isBin = false).random().id
    }
}