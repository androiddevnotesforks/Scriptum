package sgtmelon.scriptum.repository.room

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
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
 *
 * @author SerjantArbuz
 */
class RoomRepo(override val context: Context) : IRoomRepo, IRoomWork {

    // TODO #RELEASE2 убрать отсюда методы связанные с rank в RankRepo
    // TODO #RELEASE2 убрать throws

    private val iPreferenceRepo = PreferenceRepo(context) // TODO подумай, как лучше убрать от сюда iPreferenceRepo

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
                    val bindControl = BindControl(context, it)

                    if (!bin && it.isNotVisible(rankIdVisibleList)) {
                        bindControl.cancelBind()
                    } else {
                        if (it.isStatus && NotesViewModel.updateStatus) bindControl.notifyBind()

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

    override suspend fun deleteNote(noteEntity: NoteEntity) = inRoom {
        iAlarmDao.delete(noteEntity.id)

        iNoteDao.update(noteEntity.apply {
            change = context.getTime()
            isBin = true
            isStatus = false
        })
    }

    override suspend fun restoreNote(noteEntity: NoteEntity) = inRoom {
        iNoteDao.update(noteEntity.apply {
            change = context.getTime()
            isBin = false
        })
    }

    override suspend fun clearNote(noteEntity: NoteEntity) = inRoom {
        clearRankConnection(iRankDao, noteEntity)
        iNoteDao.delete(noteEntity)
    }

    override fun getRankIdVisibleList() = ArrayList<Long>().apply {
        inRoom { addAll(iRankDao.getIdVisibleList()) }
    }

    override fun getRankCount(): Boolean {
        val count: Int

        openRoom().apply { count = iRankDao.getCount() }.close()

        return count == 0
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        val noteModel: NoteModel

        openRoom().apply {
            noteModel = NoteModel(
                    noteEntity = iNoteDao[id] ?: NoteEntity(),
                    rollList = iRollDao[id],
                    alarmEntity = iAlarmDao[id] ?: AlarmEntity(noteId = id))
        }.close()

        return noteModel
    }

    override fun getRankDialogItemArray(): Array<String> = ArrayList<String>().apply {
        add(context.getString(R.string.dialog_item_rank))
        inRoom { addAll(iRankDao.getNameList()) }
    }.toTypedArray()

    override fun convertToRoll(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteEntity.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

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
                change = context.getTime()
                type = NoteType.ROLL
                setCompleteText(check = 0, size = rollList.size)
            }

            iNoteDao.update(noteEntity)
        }
    }

    override fun convertToText(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        inRoom {
            noteEntity.apply {
                change = context.getTime()
                type = NoteType.TEXT
                text = iRollDao[id].joinToString(separator = "\n") { it.text }
            }

            iNoteDao.update(noteEntity)
            iRollDao.delete(noteEntity.id)

            rollList.clear()
        }
    }

    override fun getRollListString(noteEntity: NoteEntity) = StringBuilder().apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        inRoom {
            append(iRollDao[noteEntity.id].joinToString(separator = "\n") { it.text })
        }
    }.toString()

    override fun getRollStatusString(noteEntity: NoteEntity) = StringBuilder().apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        inRoom {
            append(iRollDao[noteEntity.id]
                    .joinToString(prefix = "${noteEntity.text}\n", separator = "\n") {
                        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
                    }
            )
        }
    }.toString()

    override fun getRankIdList() = ArrayList<Long>().apply {
        inRoom { addAll(iRankDao.getIdList()) }
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteEntity.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

        inRoom {
            if (isCreate) {
                noteEntity.id = iNoteDao.insert(noteEntity)
            } else {
                iNoteDao.update(noteEntity)
            }
        }

        updateRank(noteEntity)
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        //TODO !! Оптимизировать
        val rollListTemp = rollList.filterNot { it.text.isEmpty() }
        rollList.clear()
        rollList.addAll(rollListTemp)

        inRoom {
            if (isCreate) {
                noteEntity.id = iNoteDao.insert(noteEntity)

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

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}