package sgtmelon.scriptum.repository.room

import android.content.Context
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.RankConverter
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Repository of [RoomDb]
 *
 * @param context for open [RoomDb] and get data from [PreferenceRepo]
 */
class RoomRepo(override val context: Context) : IRoomRepo, IRoomWork {

    // TODO #RELEASE2 remove to RankRepo funcs which related with rank
    // TODO #RELEASE2 cut to small repos

    // TODO think, how remove it
    private val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    private val rankConverter = RankConverter()

    override fun getNoteModelList(bin: Boolean): MutableList<NoteModel> {
        val sortType = iPreferenceRepo.sort

        val list = ArrayList<NoteModel>().apply {
            inRoom {
                val rankIdVisibleList = iRankDao.getIdVisibleList()
                var list = when (sortType) {
                    Sort.CHANGE -> iNoteDao.getByChange(bin)
                    Sort.CREATE -> iNoteDao.getByCreate(bin)
                    Sort.RANK -> iNoteDao.getByRank(bin)
                    else -> iNoteDao.getByColor(bin)
                }

                /**
                 * Notes must be showed in list if [bin] != true even if rank not visible.
                 */
                if (!bin) list = list.filter { it.isVisible(rankIdVisibleList) }

                list.forEach {
                    add(NoteModel(
                            it, iRollDao.getView(it.id),
                            alarmEntity = iAlarmDao[it.id] ?: AlarmEntity(noteId = it.id)
                    ))
                }
            }
        }

        if (sortType != Sort.RANK) return list

        /**
         * TODO упростить (заметки без категорий стоят в самом конце, а не в начале, функция исправляет это)
         */
        if (list.any { it.noteEntity.rankId != DbData.Note.Default.RANK_ID }) {
            while (list.first().noteEntity.rankId == DbData.Note.Default.RANK_ID) {
                val noteModel = list.first()
                list.removeAt(0)
                list.add(noteModel)
            }
        }

        return list
    }

    override fun isListHide(bin: Boolean): Boolean {
        var isListHide = false

        inRoom {
            val rankIdVisibleList = iRankDao.getIdVisibleList()

            iNoteDao.getByChange(bin).forEach {
                if (it.isNotVisible(rankIdVisibleList)) {
                    isListHide = true
                    // TODO break
                }
            }
        }

        return isListHide
    }

    override suspend fun clearBin() = inRoom {
        val noteList = iNoteDao.getByChange(bin = true).apply {
            forEach { iRankDao.clearConnection(it) }
        }

        iNoteDao.delete(noteList)
    }

    override suspend fun deleteNote(noteModel: NoteModel) = inRoom {
        iAlarmDao.delete(noteModel.noteEntity.id)

        iNoteDao.update(noteModel.noteEntity.apply {
            change = getTime()
            isBin = true
            isStatus = false
        })
    }

    override suspend fun restoreNote(noteModel: NoteModel) = inRoom {
        iNoteDao.update(noteModel.noteEntity.apply {
            change = getTime()
            isBin = false
        })
    }

    override suspend fun clearNote(noteModel: NoteModel) = inRoom {
        iRankDao.clearConnection(noteModel.noteEntity)
        iNoteDao.delete(noteModel.noteEntity)
    }


    override fun getRankIdVisibleList() = ArrayList<Long>().apply {
        inRoom { addAll(iRankDao.getIdVisibleList()) }
    }

    override fun isRankEmpty(): Boolean {
        val count: Int

        openRoom().apply { count = iRankDao.getCount() }.close()

        return count == 0
    }

    override fun getNoteModel(id: Long): NoteModel? {
        if (id == NoteData.Default.ID) return null

        val noteModel: NoteModel?

        openRoom().apply {
            val noteEntity = iNoteDao[id]
            val alarmEntity = iAlarmDao[id] ?: AlarmEntity(noteId = id)

            noteModel = if (noteEntity != null) {
                NoteModel(noteEntity, iRollDao[id], alarmEntity)
            } else {
                null
            }
        }.close()

        return noteModel
    }

    override fun getRankDialogItemArray(): Array<String> = ArrayList<String>().apply {
        add(context.getString(R.string.dialog_item_rank))
        inRoom { addAll(iRankDao.getNameList()) }
    }.toTypedArray()

    override fun convertToRoll(noteModel: NoteModel) = with(noteModel) {
        if (noteEntity.type != NoteType.TEXT) return

        inRoom {
            rollList.clear()

            var p = 0
            noteEntity.splitTextForRoll().forEach {
                if (it.isNotEmpty()) rollList.add(RollEntity().apply {
                    noteId = noteEntity.id
                    position = p++
                    text = it
                    id = iRollDao.insert(rollEntity = this)
                })
            }

            noteEntity.apply {
                change = getTime()
                type = NoteType.ROLL
                setCompleteText(check = 0, size = rollList.size)
            }

            iNoteDao.update(noteEntity)
        }
    }

    override fun convertToText(noteModel: NoteModel) = with(noteModel) {
        if (noteEntity.type != NoteType.ROLL) return

        inRoom {
            rollList.clear()

            noteEntity.apply {
                change = getTime()
                type = NoteType.TEXT
                setText(iRollDao[id])
            }

            iNoteDao.update(noteEntity)
            iRollDao.delete(noteEntity.id)
        }
    }

    override suspend fun getCopyText(noteEntity: NoteEntity) = StringBuilder().apply {
        if (noteEntity.name.isNotEmpty()) {
            append(noteEntity.name).append("\n")
        }

        when (noteEntity.type) {
            NoteType.TEXT -> append(noteEntity.text)
            NoteType.ROLL -> inRoom {
                append(iRollDao[noteEntity.id].joinToString(separator = "\n") { it.text })
            }
        }
    }.toString()

    override fun getRankIdList() = ArrayList<Long>().apply {
        inRoom { addAll(iRankDao.getIdList()) }
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean) = with(noteModel) {
        if (noteEntity.type != NoteType.TEXT) return@with

        inRoom {
            if (isCreate) {
                noteEntity.id = iNoteDao.insert(noteEntity)
                alarmEntity.noteId = noteEntity.id
            } else {
                iNoteDao.update(noteEntity)
            }
        }
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean) = with(noteModel) {
        if (noteEntity.type != NoteType.ROLL) return@with

        //TODO !! Refactor
        val rollListTemp = rollList.filterNot { it.text.isEmpty() }
        rollList.clear()
        rollList.addAll(rollListTemp)

        inRoom {
            if (isCreate) {
                noteEntity.id = iNoteDao.insert(noteEntity)
                alarmEntity.noteId = noteEntity.id

                /**
                 * Write roll to db
                 */
                rollList.forEachIndexed { i, item ->
                    item.apply {
                        noteId = noteEntity.id
                        position = i
                    }.id = iRollDao.insert(item)
                }
            } else {
                iNoteDao.update(noteEntity)

                val idSaveList = ArrayList<Long>()

                rollList.forEachIndexed { i, item ->
                    item.position = i

                    val id = item.id
                    if (id == null) {
                        item.id = iRollDao.insert(item)
                    } else {
                        iRollDao.update(id, i, item.text)
                    }

                    item.id?.let { idSaveList.add(it) }
                }

                /**
                 * Remove swiped rolls
                 */
                iRollDao.delete(noteEntity.id, idSaveList)
            }
        }
    }

    override fun updateRollCheck(noteEntity: NoteEntity, rollEntity: RollEntity) {
        rollEntity.id?.let {
            inRoom {
                iRollDao.update(it, rollEntity.isCheck)
                iNoteDao.update(noteEntity)
            }
        }
    }

    override fun updateRollCheck(noteEntity: NoteEntity, check: Boolean) = inRoom {
        iRollDao.updateAllCheck(noteEntity.id, check)
        iNoteDao.update(noteEntity)
    }

    override fun updateNote(noteEntity: NoteEntity) = inRoom { iNoteDao.update(noteEntity) }

    /**
     * Remove relation between [RankItem] and [NoteEntity] which will be delete
     */
    private fun IRankDao.clearConnection(noteEntity: NoteEntity) {
        if (noteEntity.rankId == DbData.Note.Default.RANK_ID) return

        val rankItem = get(noteEntity.rankId)?.apply {
            noteId.remove(noteEntity.id)
        } ?: return

        update(rankConverter.toEntity(rankItem))
    }

}