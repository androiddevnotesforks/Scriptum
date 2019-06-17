package sgtmelon.scriptum.repository.room

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankItem
import sgtmelon.scriptum.room.entity.RollItem
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Репозиторий обработки данных [RoomDb]
 *
 * @param context для открытия [RoomDb] и получения данных из [PreferenceRepo]
 *
 * @author SerjantArbuz
 */
class RoomRepo(private val context: Context) : IRoomRepo {

    // TODO #RELEASE добавить к noteModel - alarmEntity

    private val iPreferenceRepo = PreferenceRepo(context) // TODO подумай, как лучше убрать от сюда iPreferenceRepo

    private fun openRoom() = RoomDb.getInstance(context)

    private fun getNoteListQuery(bin: Boolean) = SimpleSQLiteQuery(
            "SELECT * FROM ${DbData.Note.TABLE}" +
                    " WHERE ${DbData.Note.BIN} = ${BoolConverter().toInt(bin)}" +
                    " ORDER BY ${DbData.Note.orders[iPreferenceRepo.sort]}")

    override fun getNoteModelList(bin: Boolean) = ArrayList<NoteModel>().apply {
        openRoom().apply {
            val rankIdVisibleList = getRankDao().rankIdVisibleList

            getNoteDao()[getNoteListQuery(bin)].forEach {
                val bindControl = BindControl(context, it)

                if (it.isNotVisible(rankIdVisibleList)) {
                    bindControl.cancelBind()
                } else {
                    if (it.isStatus && NotesViewModel.updateStatus) bindControl.notifyBind()

                    add(NoteModel(it, getRollDao().getView(it.id)))
                }
            }
        }.close()
    }

    override suspend fun clearBin() = openRoom().apply {
        val rankIdVisibleList = getRankDao().rankIdVisibleList

        val noteList = ArrayList<NoteEntity>().apply {
            getNoteDao()[true].forEach { if (it.isVisible(rankIdVisibleList)) add(it) }
            forEach { clearRankConnection(getRankDao(), it) }
        }

        getNoteDao().delete(noteList)
    }.close()

    override suspend fun deleteNote(noteEntity: NoteEntity) = openRoom().apply {
        getNoteDao().update(noteEntity.apply {
            change = context.getTime()
            isBin = true
            isStatus = false
        })
    }.close()

    override suspend fun restoreNote(noteEntity: NoteEntity) = openRoom().apply {
        getNoteDao().update(noteEntity.apply {
            change = context.getTime()
            isBin = false
        })
    }.close()

    override suspend fun clearNote(noteEntity: NoteEntity) = openRoom().apply {
        clearRankConnection(getRankDao(), noteEntity)
        getNoteDao().delete(noteEntity)
    }.close()

    override fun getRankIdVisibleList() = ArrayList<Long>().apply {
        openRoom().apply { addAll(getRankDao().rankIdVisibleList) }.close()
    }

    override fun getRankCount(): Boolean {
        val count: Int

        openRoom().apply { count = getRankDao().count }.close()

        return count == 0
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        val noteModel: NoteModel

        openRoom().apply { noteModel = NoteModel(getNoteDao()[id], getRollDao()[id]) }.close()

        return noteModel
    }

    override fun getRankNameList() = ArrayList<String>().apply {
        openRoom().apply { addAll(getRankDao().name) }.close()
    }

    override fun getRankCheckArray(noteEntity: NoteEntity): BooleanArray {
        val array: BooleanArray
        openRoom().apply { array = calculateRankCheckArray(noteEntity, db = this) }.close()
        return array
    }

    override fun convertToRoll(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteEntity.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

        openRoom().apply {
            rollList.clear()

            var p = 0
            noteEntity.splitTextForRoll().forEach {
                if (it.isNotEmpty()) rollList.add(RollItem().apply {
                    noteId = noteEntity.id
                    position = p++
                    text = it
                    id = getRollDao().insert(rollItem = this)
                })
            }

            noteEntity.apply {
                change = context.getTime()
                type = NoteType.ROLL
                setCompleteText(check = 0, size = rollList.size)
            }

            getNoteDao().update(noteEntity)
        }.close()
    }

    override fun convertToText(noteModel: NoteModel) = noteModel.apply {
        if (noteModel.noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            noteEntity.apply {
                change = context.getTime()
                type = NoteType.TEXT
                text = getRollDao()[id].joinToString(separator = "\n") { it.text }
            }

            getNoteDao().update(noteEntity)
            getRollDao().delete(noteEntity.id)

            rollList.clear()
        }.close()
    }

    override fun getRollListString(noteEntity: NoteEntity) = StringBuilder().apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            append(getRollDao()[noteEntity.id].joinToString(separator = "\n") { it.text })
        }.close()
    }.toString()

    override fun getRollStatusString(noteEntity: NoteEntity) = StringBuilder().apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply {
            append(getRollDao()[noteEntity.id]
                    .joinToString(prefix = "${noteEntity.text}\n", separator = "\n") {
                        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
                    }
            )
        }.close()
    }.toString()

    override fun getRankIdList() = ArrayList<Long>().apply {
        openRoom().apply { addAll(getRankDao().id) }.close()
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteEntity.type != NoteType.TEXT)
            throw ClassCastException("This method only for TEXT type")

        openRoom().apply {
            with(getNoteDao()) {
                if (isCreate) noteEntity.id = insert(noteEntity) else update(noteEntity)
            }
        }.close()

        updateRank(noteEntity)
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean) = noteModel.apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        //TODO !! Оптимизировать
        val rollListTemp = rollList.filterNot { it.text.isEmpty() }
        rollList.clear()
        rollList.addAll(rollListTemp)

        openRoom().apply {
            if (isCreate) {
                noteEntity.id = getNoteDao().insert(noteEntity)

                /**
                 * Запись в пунктов в БД
                 */
                rollList.forEachIndexed { i, item ->
                    item.apply {
                        noteId = noteEntity.id
                        position = i
                    }.id = getRollDao().insert(item)
                }
            } else {
                getNoteDao().update(noteEntity)

                val idSaveList = ArrayList<Long>()

                rollList.forEachIndexed { i, item ->
                    item.position = i

                    val id = item.id
                    if (id == null) {
                        item.id = getRollDao().insert(item)
                    } else {
                        getRollDao().update(id, i, item.text)
                    }

                    item.id?.let { idSaveList.add(it) }
                }

                /**
                 * Удаление пунктов, которые swipe
                 */
                getRollDao().delete(noteEntity.id, idSaveList)
            }
        }.close()

        updateRank(noteEntity)
    }

    override fun insertRank(p: Int, rankItem: RankItem): Long {
        val id: Long

        openRoom().apply {
            id = getRankDao().insert(rankItem)
            if (p != 0) updateRankPosition(p, db = this)
        }.close()

        return id
    }

    override fun updateRollCheck(noteEntity: NoteEntity, rollItem: RollItem) {
        rollItem.id?.let {
            openRoom().apply {
                getRollDao().update(it, rollItem.isCheck)
                getNoteDao().update(noteEntity)
            }.close()
        }
    }

    override fun updateRollCheck(noteEntity: NoteEntity, check: Boolean) =
            openRoom().apply {
                getRollDao().updateAllCheck(noteEntity.id, check)
                getNoteDao().update(noteEntity)
            }.close()

    override fun updateNote(noteEntity: NoteEntity) =
            openRoom().apply { getNoteDao().update(noteEntity) }.close()

    override suspend fun notifyStatusBar() = openRoom().apply {
        val rankIdVisibleList = getRankDao().rankIdVisibleList

        getNoteDao()[getNoteListQuery(bin = false)].forEach {
            BindControl(context, it).updateBind(rankIdVisibleList)
        }
    }.close()

    override fun deleteRank(name: String, p: Int) = openRoom().apply {
        val rankItem = getRankDao()[name]

        if (rankItem.noteId.isNotEmpty()) {
            val noteList = getNoteDao()[rankItem.noteId]

            /**
             * Убирает из списков ненужную категорию по id
             */
            noteList.forEach {
                val index = it.rankId.indexOf(rankItem.id)

                it.rankId.removeAt(index)
                it.rankPs.removeAt(index)
            }

            getNoteDao().update(noteList)
        }

        getRankDao().delete(rankItem)

        updateRankPosition(p, db = this)
    }.close()

    override fun getRankModel() = RankModel(getCompleteRankList())

    override fun updateRank(dragFrom: Int, dragTo: Int): MutableList<RankItem> { // TODO оптимизировать
        val startFirst = dragFrom < dragTo

        val iStart = if (startFirst) dragFrom else dragTo
        val iEnd = if (startFirst) dragTo else dragFrom
        val iAdd = if (startFirst) -1 else 1

        val rankList = getCompleteRankList()
        val noteIdList = ArrayList<Long>()

        for (i in iStart..iEnd) {
            val rankItem = rankList[i]
            rankItem.noteId.forEach { if (noteIdList.contains(it)) noteIdList.add(it) }

            val start = i == dragFrom
            val end = i == dragTo

            val newPosition = if (start) dragTo else i + iAdd
            rankItem.position = newPosition

            if (if (startFirst) end else start) {
                rankList.removeAt(i)
                rankList.add(newPosition, rankItem)
            } else {
                rankList[i] = rankItem
            }
        }

        rankList.sortBy { it.position }

        openRoom().apply {
            getRankDao().update(rankList)
            updateNoteRankPosition(noteIdList, rankList, db = this)
        }.close()

        return rankList
    }

    override fun updateRank(rankItem: RankItem) =
            openRoom().apply { getRankDao().update(rankItem) }.close()

    override fun updateRank(rankList: List<RankItem>) =
            openRoom().apply { getRankDao().update(rankList) }.close()

    // TODO прибрать private

    private fun getCompleteRankList() = ArrayList<RankItem>().apply {
        openRoom().apply {
            addAll(getRankDao().simple)
            forEach {
                it.textCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.TEXT))
                it.rollCount = getNoteDao().getCount(it.noteId, NoteTypeConverter().toInt(NoteType.ROLL))
            }
        }.close()
    }

    /**
     * @param fromPosition - Позиция удаления категории
     */
    private fun updateRankPosition(fromPosition: Int, db: RoomDb) = with(db) {
        val rankList = getRankDao().simple
        val noteIdList = ArrayList<Long>()

        for (i in fromPosition until rankList.size) {
            rankList[i].apply {
                noteId.forEach { if (!noteIdList.contains(it)) noteIdList.add(it) }
                position = i
            }
        }

        getRankDao().update(rankList)
        updateNoteRankPosition(noteIdList, rankList, db = this)
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    private fun updateNoteRankPosition(noteIdList: List<Long>, rankList: List<RankItem>, db: RoomDb) =
            with(db.getNoteDao()) {
                val noteList = get(noteIdList)

                noteList.forEach { item ->
                    val newIdList = ArrayList<Long>()
                    val newPsList = ArrayList<Long>()

                    rankList.forEach {
                        if (item.rankId.contains(it.id)) {
                            newIdList.add(it.id)
                            newPsList.add(it.position.toLong())
                        }
                    }

                    item.rankId = newIdList
                    item.rankPs = newPsList
                }

                update(noteList)
            }

    /**
     * Добавление или удаление id заметки к категорииё
     */
    private fun updateRank(noteEntity: NoteEntity) = openRoom().apply {
        val list = getRankDao().simple
        val check = calculateRankCheckArray(noteEntity, db = this)

        val id = noteEntity.id
        list.forEachIndexed { i, item ->
            if (check[i] && !item.noteId.contains(id)) {
                item.noteId.add(id)
            } else if (!check[i]) {
                item.noteId.remove(id)
            }
        }

        getRankDao().update(list)
    }.close()

    /**
     * Удаление связи между Rank и Note
     *
     * @param rankDao передаётся таким образом, чтобы не закрыть db
     * @param noteEntity заметка, которая будет удалена
     */
    private fun clearRankConnection(rankDao: RankDao, noteEntity: NoteEntity) {
        if (noteEntity.rankId.isEmpty()) return

        rankDao[noteEntity.rankId].apply {
            forEach { it.noteId.remove(noteEntity.id) }
            rankDao.update(list = this)
        }
    }

    private fun calculateRankCheckArray(noteEntity: NoteEntity, db: RoomDb): BooleanArray {
        val rankList = db.getRankDao().simple
        val check = BooleanArray(rankList.size)

        rankList.forEachIndexed { i, item -> check[i] = noteEntity.rankId.contains(item.id) }

        return check
    }

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)

        fun NoteEntity.isVisible(rankIdVisibleList: List<Long>) =
                rankId.isEmpty() || rankIdVisibleList.contains(rankId[0])

        fun NoteEntity.isNotVisible(rankIdVisibleList: List<Long>) =
                rankId.isNotEmpty() && !rankIdVisibleList.contains(rankId[0])
    }

}