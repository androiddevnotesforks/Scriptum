package sgtmelon.scriptum.app.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.screen.main.notes.NotesViewModel
import sgtmelon.scriptum.office.annot.DbAnn
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class RoomRepo(private val context: Context) : IRoomRepo {

    private val preference = Preference(context)

    // TODO !! заменить db на openRoom (добавить extension для возврата val)

    private lateinit var db: RoomDb

    // TODO think about itn
    private fun openRoom() = RoomDb.getInstance(context)

    /**
     *
     */

    override fun getNoteModelList(fromBin: Boolean): MutableList<NoteModel> {
        val list = ArrayList<NoteModel>()

        openRoom().apply {
            val rankVisible = daoRank().rankVisible

            daoNote().get(getNoteListQuery(preference.sortNoteOrder, fromBin)).forEach {
                val rollList = daoRoll().getView(it.id)
                val statusItem = StatusItem(context, it, notify = false)

                val noteModel = NoteModel(it, rollList, statusItem)

                if (it.rankId.isNotEmpty() && !rankVisible.contains(it.rankId[0])) {
                    statusItem.cancelNote()
                } else {
                    if (it.isStatus && NotesViewModel.updateStatus) {
                        statusItem.notifyNote()
                    }

                    noteModel.statusItem = statusItem
                    list.add(noteModel)
                }
            }
        }.close()

        return list
    }

    private fun getNoteListQuery(sortOrder: String, fromBin: Boolean) = SimpleSQLiteQuery(
            "SELECT * FROM " + DbAnn.Note.TABLE +
                    " WHERE " + DbAnn.Note.BIN + " = " + BoolConverter().toInt(fromBin) +
                    " ORDER BY " + sortOrder)

    override fun clearBin() = openRoom().apply {
        val noteList = daoNote().get(true)

        noteList.forEach {
            if (it.rankId.isNotEmpty()) {
                clearRank(it.id, it.rankId)
            }
        }

        daoNote().delete(noteList)
    }.close()

    override fun restoreNoteItem(id: Long) =
            openRoom().apply { daoNote().update(id, context.getTime(), false) }.close()

    /**
     * Удаление заметки с отчисткой категории
     */
    override fun clearNoteItem(id: Long) = openRoom().apply {
        with(daoNote().get(id)) {
            if (rankId.isNotEmpty()) {
                clearRank(id, rankId)
            }

            daoNote().delete(noteItem = this)
        }
    }.close()

    /**
     * [noteId] - Id заметки, которую надо убрать из категории
     * [rankIdList] - Массив из id категорий, принадлежащих заметке
     */
    private fun clearRank(noteId: Long, rankIdList: List<Long>) = openRoom().apply {
        with(daoRank().get(rankIdList)) {
            forEach { it.noteId.remove(noteId) }
            daoRank().update(rankList = this)
        }
    }.close()

    override fun getRankVisibleList(): List<Long> {
        val list = ArrayList<Long>()

        openRoom().apply { list.addAll(daoRank().rankVisible) }.close()

        return list
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        db = RoomDb.getInstance(context)
        val noteItem = db.daoNote().get(id)
        val rollList = db.daoRoll().get(id)

        val noteModel = NoteModel(noteItem, rollList, StatusItem(context, noteItem, notify = false))
        db.close()

        return noteModel
    }

    override fun getRankDialogName(): Array<String> {
        db = RoomDb.getInstance(context)
        val array = db.daoRank().name
        db.close()

        return array
    }

    override fun getRankCheck(rankId: List<Long>): BooleanArray {
        db = RoomDb.getInstance(context)
        val array = db.daoRank().getCheck(rankId)
        db.close()

        return array
    }

    override fun convertToRoll(noteItem: NoteItem) = openRoom().apply {
        val size = daoRoll().insert(noteItem.id, noteItem.text).size

        noteItem.apply {
            change = context.getTime()
            type = NoteType.ROLL
            setRollText(0, size)
        }
    }.close()

    override fun convertToText(noteItem: NoteItem) = openRoom().apply {
        noteItem.apply {
            change = context.getTime()
            type = NoteType.TEXT
            text = daoRoll().getText(noteItem.id)
        }

        daoNote().update(noteItem)
        daoRoll().delete(noteItem.id)
    }.close()

    override fun getRankId(): Array<Long> {
        db = RoomDb.getInstance(context)
        val array: Array<Long> = db.daoRank().id
        db.close()

        return array
    }

    override fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel {
        val noteItem = noteModel.noteItem

        openRoom().apply {
            if (isCreate) {
                noteItem.id = daoNote().insert(noteItem)
            } else {
                daoNote().update(noteItem)
            }

            daoRank().update(noteItem.id, noteItem.rankId)
        }.close()

        return noteModel
    }

    override fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel {
        val noteItem = noteModel.noteItem
        val listRoll = noteModel.listRoll

        db = RoomDb.getInstance(context)

        if (isCreate) {
            noteItem.id = db.daoNote().insert(noteItem)

            /**
             * Запись в пунктов в БД
             */
            listRoll.forEachIndexed { index, rollItem ->
                rollItem.apply {
                    noteId = noteItem.id
                    position = index
                    id = db.daoRoll().insert(rollItem)
                }
            }
        } else {
            db.daoNote().update(noteItem)

            val listSaveId = ArrayList<Long>()

            listRoll.forEachIndexed { index, rollItem ->
                rollItem.position = index

                val id = rollItem.id
                if (id == null) {
                    rollItem.id = db.daoRoll().insert(rollItem)
                } else {
                    db.daoRoll().update(id, index, rollItem.text)
                }

                rollItem.id?.let { listSaveId.add(it) }
            }

            /**
             * Удаление пунктов, которые swipe
             */
            db.daoRoll().delete(noteItem.id, listSaveId)
        }

        db.daoRank().update(noteItem.id, noteItem.rankId)
        db.close()

        return noteModel
    }

    override fun insertRank(p: Int, rankItem: RankItem): Long {
        db = RoomDb.getInstance(context)
        val id = db.daoRank().insert(rankItem)
        if (p != 0) db.daoRank().update(p)
        db.close()

        return id
    }

    override fun updateRollCheck(rollItem: RollItem, noteItem: NoteItem) { // TODO переделать
        rollItem.id?.let { id ->
            openRoom().apply {
                daoRoll().update(id, rollItem.isCheck)
                daoNote().update(noteItem)
            }.close()
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, isAll: Boolean) =
            openRoom().apply {
                daoRoll().updateAllCheck(noteItem.id, isAll)
                daoNote().update(noteItem)
            }.close()

    override fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean) =
            openRoom().apply {
                daoRoll().updateAllCheck(noteItem.id, check)
                daoNote().update(noteItem)
            }.close()

    override fun updateNoteItemBind(id: Long, status: Boolean) =
            openRoom().apply { daoNote().update(id, status) }.close()

    override fun updateNoteItem(noteItem: NoteItem) =
            openRoom().apply { daoNote().update(noteItem) }.close()

    /**
     * Обновление элементов списка в статус баре
     */
    override fun updateStatus() = openRoom().apply {
        val rankVisible = daoRank().rankVisible

        daoNote().get(getNoteListQuery(preference.sortNoteOrder, fromBin = false)).forEach {
            val statusItem = StatusItem(context, it, notify = false)

            if (it.rankId.isNotEmpty() && !rankVisible.contains(it.rankId[0])) {
                statusItem.cancelNote()
            } else if (it.isStatus) {
                statusItem.notifyNote()
            }
        }
    }.close()

    override fun deleteNoteItem(id: Long) = openRoom().apply {
        daoNote().update(id, context.getTime(), true)
        daoNote().update(id, false)
    }.close()

    override fun deleteRank(name: String, p: Int) {
        openRoom().apply {
            with(daoRank().get(name)) {
                if (noteId.isNotEmpty()) {
                    val noteList = daoNote().getNote(noteId)

                    /**
                     * Убирает из списков ненужную категорию по id
                     */
                    noteList.forEach {
                        val index = it.rankId.indexOf(id)

                        it.rankId.removeAt(index)
                        it.rankPs.removeAt(index)
                    }

                    daoNote().updateNote(noteList)
                }

                daoRank().delete(rankItem = this)
            }

            daoRank().update(p)
            updateStatus()
        }.close()
    }


    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}