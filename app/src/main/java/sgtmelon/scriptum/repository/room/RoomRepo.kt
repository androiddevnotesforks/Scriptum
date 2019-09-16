package sgtmelon.scriptum.repository.room

import android.content.Context
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Репозиторий обработки данных [RoomDb]
 *
 * @param context для открытия [RoomDb] и получения данных из [PreferenceRepo]
 */
class RoomRepo(override val context: Context) : IRoomRepo, IRoomWork {

    // TODO #RELEASE2 убрать отсюда методы связанные с rank в RankRepo
    // TODO #RELEASE2 убрать throws
    // TODO #RELEASE2 Разнести по отдельным repo

    // TODO подумай, как лучше убрать от сюда iPreferenceRepo
    private val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    override fun getNoteModelList(bin: Boolean): MutableList<NoteModel> {
        val sortType = iPreferenceRepo.sort

        val list = ArrayList<NoteModel>().apply {
            inRoom {
                val rankIdVisibleList = iRankDao.getIdVisibleList()

                when (sortType) {
                    Sort.CHANGE -> iNoteDao.getByChange(bin)
                    Sort.CREATE -> iNoteDao.getByCreate(bin)
                    Sort.RANK -> iNoteDao.getByRank(bin)
                    else -> iNoteDao.getByColor(bin)
                }.forEach {
                    val bindControl = BindControl(context)

                    if (!bin && it.isNotVisible(rankIdVisibleList)) {
                        bindControl.cancel(it.id.toInt())
                    } else {
                        if (NotesViewModel.updateStatus) {
                            val noteModel = NoteModel(it, BindRepo(context).getRollList(it.id))
                            bindControl.notify(noteModel, rankIdVisibleList)
                        }

                        add(NoteModel(
                                it, iRollDao.getView(it.id),
                                alarmEntity = iAlarmDao[it.id] ?: AlarmEntity(noteId = it.id))
                        )
                    }
                }
            }
        }

        if (sortType != Sort.RANK) return list

        if (list.any { it.noteEntity.rankId != NoteEntity.ND_RANK_ID }) {
            while (list.first().noteEntity.rankId == NoteEntity.ND_RANK_ID) {
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
        val noteList = iNoteDao.getByChange(true).apply {
            forEach { clearRankConnection(iRankDao, it) }
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
        clearRankConnection(iRankDao, noteModel.noteEntity)
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
                text = iRollDao[id].joinToString(separator = "\n") { it.text }
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

        updateRank(noteEntity)
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean) = with(noteModel) {
        if (noteEntity.type != NoteType.ROLL) return@with

        //TODO !! Оптимизировать
        val rollListTemp = rollList.filterNot { it.text.isEmpty() }
        rollList.clear()
        rollList.addAll(rollListTemp)

        inRoom {
            if (isCreate) {
                noteEntity.id = iNoteDao.insert(noteEntity)
                alarmEntity.noteId = noteEntity.id

                /**
                 * Запись в пунктов в БД
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
                 * Удаление пунктов, которые swipe
                 */
                iRollDao.delete(noteEntity.id, idSaveList)
            }
        }

        updateRank(noteEntity)
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


    // TODO прибрать private

    /**
     * Добавление или удаление id заметки к категорииё
     */
    private fun updateRank(noteEntity: NoteEntity) = inRoom {
        val list = iRankDao.get()
        val check = calculateRankCheckArray(noteEntity, db = this)

        val id = noteEntity.id
        list.forEachIndexed { i, item ->
            if (check[i] && !item.noteId.contains(id)) {
                item.noteId.add(id)
            } else if (!check[i]) {
                item.noteId.remove(id)
            }
        }

        iRankDao.update(list)
    }

    /**
     * Удаление связи между Rank и Note
     *
     * @param rankDao передаётся таким образом, чтобы не закрыть db
     * @param noteEntity заметка, которая будет удалена
     */
    private fun clearRankConnection(rankDao: IRankDao, noteEntity: NoteEntity) {
        if (noteEntity.rankId == NoteEntity.ND_RANK_ID) return

        val rankEntity = rankDao[noteEntity.rankId].apply { noteId.remove(noteEntity.id) }
        rankDao.update(rankEntity)
    }

    private fun calculateRankCheckArray(noteEntity: NoteEntity, db: RoomDb): BooleanArray {
        val rankList = db.iRankDao.get()
        val check = BooleanArray(rankList.size)

        rankList.forEachIndexed { i, item -> check[i] = noteEntity.rankId == item.id }

        return check
    }

}