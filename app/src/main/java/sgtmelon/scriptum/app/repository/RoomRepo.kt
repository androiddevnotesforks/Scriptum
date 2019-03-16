package sgtmelon.scriptum.app.repository

import android.content.Context
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class RoomRepo(private val context: Context) : IRoomRepo {

    // TODO !! заменить db на openRoom (добавить extension для возврата val)

    private lateinit var db: RoomDb

    // TODO think about itn
    private fun openRoom() = RoomDb.getInstance(context)

    /**
     * BinViewModel
     */

    override fun getNoteModelList(fromBin: Boolean): MutableList<NoteModel> {
        var list: MutableList<NoteModel> = ArrayList()

        openRoom().apply {
            list = daoNote().get(context, fromBin)
        }.close()

        return list
    }

    override fun clearBin() = openRoom().apply { daoNote().clearBin() }.close()

    override fun restoreNoteItem(id: Long) =
            openRoom().apply { daoNote().update(id, context.getTime(), false) }.close()

    override fun clearNoteItem(id: Long) = openRoom().apply { daoNote().delete(id) }.close()

    /**
     *
     */

    override fun getRankVisibleList(): List<Long> {
        var list: List<Long> = ArrayList()

        openRoom().apply {
            list = db.daoRank().rankVisible
        }.close()

        return list
    }

    override fun getNoteModel(id: Long): NoteModel {
        if (id == NoteData.Default.ID) throw NullPointerException("You try to get note with no id")

        db = RoomDb.getInstance(context)
        val noteModel = db.daoNote().get(context, id)
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

        db = RoomDb.getInstance(context)

        if (isCreate) {
            noteItem.id = db.daoNote().insert(noteItem)
        } else {
            db.daoNote().update(noteItem);
        }

        db.daoRank().update(noteItem.id, noteItem.rankId)
        db.close()

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

            val listSaveId = ArrayList<Long?>()

            listRoll.forEachIndexed { index, rollItem ->
                rollItem.position = index

                val id = rollItem.id
                if (id == null) {
                    rollItem.id = db.daoRoll().insert(rollItem)
                } else {
                    db.daoRoll().update(id, index, rollItem.text)
                }

                listSaveId.add(id)
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

    /**
     * NotesViewModel
     */


    override fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean) =
            openRoom().apply {
                daoRoll().updateAllCheck(noteItem.id, check)
                daoNote().update(noteItem)
            }.close()

    override fun updateNoteItemBind(id: Long, status: Boolean) =
            openRoom().apply { daoNote().update(id, status) }.close()

    override fun updateNoteItem(noteItem: NoteItem) =
            openRoom().apply { daoNote().update(noteItem) }.close()

    override fun deleteNoteItem(id: Long) = openRoom().apply {
        daoNote().update(id, context.getTime(), true)
        daoNote().update(id, false)
    }.close()


    /**
     *
     */


    /**
     *
     */

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}