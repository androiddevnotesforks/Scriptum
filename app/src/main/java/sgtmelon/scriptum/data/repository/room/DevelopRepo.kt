package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.domain.model.item.PrintItem

/**
 * Repository of [RoomDb] which work with all tables data and only for developer screen.
 */
class DevelopRepo(
    override val roomProvider: RoomProvider
) : IDevelopRepo,
    IRoomWork {

    override suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note> {
        return takeFromRoom { noteDao.get(isBin).map { PrintItem.Note(it) } }
    }

    override suspend fun getPrintRollList(): List<PrintItem.Roll> {
        return takeFromRoom { rollDao.get().map { PrintItem.Roll(it) } }
    }

    override suspend fun getPrintVisibleList(): List<PrintItem.Visible> {
        return takeFromRoom { rollVisibleDao.get().map { PrintItem.Visible(it) } }
    }

    override suspend fun getPrintRankList(): List<PrintItem.Rank> {
        return takeFromRoom { rankDao.get().map { PrintItem.Rank(it) } }
    }

    override suspend fun getPrintAlarmList(): List<PrintItem.Alarm> {
        return takeFromRoom { alarmDao.get().map { PrintItem.Alarm(it) } }
    }

    override suspend fun getRandomNoteId(): Long = takeFromRoom {
        noteDao.get(bin = false).random().id
    }
}