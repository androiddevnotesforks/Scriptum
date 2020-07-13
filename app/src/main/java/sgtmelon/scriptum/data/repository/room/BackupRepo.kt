package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult

/**
 * Repository of [RoomDb] which work with backup data.
 */
class BackupRepo(
        override val roomProvider: RoomProvider,
        private val noteConverter: NoteConverter,
        private val rollConverter: RollConverter,
        private val rankConverter: RankConverter,
        private val alarmConverter: AlarmConverter
) : IBackupRepo,
        IRoomWork {

    override suspend fun insertData(parserResult: ParserResult,
                                    importSkip: Boolean): ImportResult = takeFromRoom {
        val noteList = parserResult.noteList.toMutableList()
        val rollList = parserResult.rollList.toMutableList()
        val rollVisibleList = parserResult.rollVisibleList.toMutableList()
        val rankList = parserResult.rankList.toMutableList()
        val alarmList = parserResult.alarmList.toMutableList()

        val removeNoteList = if (importSkip) {
            getRemoveNoteList(noteList, rollList, this)
        } else {
            emptyList()
        }

        TODO()

        return@takeFromRoom if (importSkip) {
            ImportResult.Skip(removeNoteList.size)
        } else {
            ImportResult.Simple
        }
    }

    @RunPrivate suspend fun getRemoveNoteList(noteList: List<NoteEntity>, rollList: List<RollEntity>,
                                              roomDb: RoomDb): List<NoteEntity> {
        val removeList = mutableListOf<NoteEntity>()

        val existNoteList = roomDb.noteDao.get(bin = false)
        val existRollNoteList = existNoteList.filter { it.type == NoteType.ROLL }

        for (item in noteList) {
            when(item.type) {
                NoteType.TEXT -> {
                    if (existNoteList.any { item.name == it.name && item.text == it.text }) {
                        removeList.add(item)
                    }
                }
                NoteType.ROLL -> {
                    val itemRollList = rollList.filter { item.id == it.noteId }

                    for (existItem in existRollNoteList.filter { item.name == it.name }) {
                        val existRollList = roomDb.rollDao.get(existItem.id)

                        if (itemRollList.size != existRollList.size) continue

                        var needAdd = false
                        itemRollList.forEach {

                        }
                    }
                }
            }
        }

        return removeList
    }

}