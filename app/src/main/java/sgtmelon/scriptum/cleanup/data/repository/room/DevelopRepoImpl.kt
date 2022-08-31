package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.DevelopRepo
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Repository of [Database] which work with all tables data and only for developer screen.
 */
class DevelopRepoImpl(
    override val roomProvider: RoomProvider
) : DevelopRepo,
    IRoomWork {

    override suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note> {
        return fromRoom { noteDao.getList(isBin).map { PrintItem.Note(it) } }
    }

    override suspend fun getPrintRollList(): List<PrintItem.Roll> {
        return fromRoom { rollDao.getList().map { PrintItem.Roll(it) } }
    }

    override suspend fun getPrintVisibleList(): List<PrintItem.Visible> {
        return fromRoom { rollVisibleDao.getList().map { PrintItem.Visible(it) } }
    }

    override suspend fun getPrintRankList(): List<PrintItem.Rank> {
        return fromRoom { rankDao.getList().map { PrintItem.Rank(it) } }
    }

    override suspend fun getPrintAlarmList(): List<PrintItem.Alarm> {
        return fromRoom { alarmDao.getList().map { PrintItem.Alarm(it) } }
    }

    override suspend fun getRandomNoteId(): Long = fromRoom {
        noteDao.getList(isBin = false).random().id
    }
}