package sgtmelon.scriptum.app.repository

import android.content.Context
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class RoomRepo(private val context: Context) : IRoomRepo {

    private lateinit var db: RoomDb

    /**
     * BinViewModel
     */

    override fun getNoteRepoList(fromBin: Boolean): MutableList<NoteRepo> {
        db = RoomDb.provideDb(context)
        val list = db.daoNote().get(context, fromBin)
        db.close()

        return list
    }

    override fun clearBin() {
        db = RoomDb.provideDb(context)
        db.daoNote().clearBin()
        db.close()
    }

    override fun restoreNoteItem(id: Long) {
        db = RoomDb.provideDb(context)
        db.daoNote().update(id, context.getTime(), false)
        db.close()
    }

    override fun clearNoteItem(id: Long) {
        db = RoomDb.provideDb(context)
        db.daoNote().delete(id)
        db.close()
    }

    /**
     *
     */

    override fun getRankVisibleList(): List<Long> {
        db = RoomDb.provideDb(context)
        val list = db.daoRank().rankVisible
        db.close()

        return list
    }

    override fun getNoteRepo(id: Long): NoteRepo {
        db = RoomDb.provideDb(context)
        val noteRepo = db.daoNote().get(context, id)
        db.close()

        return noteRepo
    }

    override fun getRankDialogName(): Array<String> {
        db = RoomDb.provideDb(context)
        val array = db.daoRank().name
        db.close()

        return array
    }


    override fun getRankCheck(rankId: List<Long>): BooleanArray {
        db = RoomDb.provideDb(context)
        val array = db.daoRank().getCheck(rankId)
        db.close()

        return array
    }

    override fun convertToRoll(noteItem: NoteItem) {
        db = RoomDb.provideDb(context)

        val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)
        noteItem.change = context.getTime()
        noteItem.type = NoteType.ROLL
        noteItem.setText(0, listRoll.size)

        db.daoNote().update(noteItem)
        db.close()
    }

    override fun convertToText(noteItem: NoteItem) {
        db = RoomDb.provideDb(context)

        noteItem.change = context.getTime()
        noteItem.type = NoteType.TEXT
        noteItem.text = db.daoRoll().getText(noteItem.id)

        db.daoNote().update(noteItem)
        db.daoRoll().delete(noteItem.id)
        db.close()
    }

    override fun getRankId(): Array<Long> {
        db = RoomDb.provideDb(context)
        val array: Array<Long> = db.daoRank().id
        db.close()

        return array
    }

    override fun saveTextNote(noteItem: NoteItem, isCreate: Boolean): Long? {
        db = RoomDb.provideDb(context)

        val id = when (isCreate) {
            true -> db.daoNote().insert(noteItem)
            false -> {
                db.daoNote().update(noteItem);
                null
            }
        }

        db.daoRank().update(noteItem.id, noteItem.rankId)
        db.close()

        return id
    }

    override fun saveRollNote(noteRepo: NoteRepo): NoteRepo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertRank(p: Int, rankItem: RankItem): Long {
        db = RoomDb.provideDb(context)
        val id = db.daoRank().insert(rankItem)
        if (p != 0) db.daoRank().update(p)
        db.close()

        return id
    }

    override fun updateRollCheck(rollItem: RollItem, noteItem: NoteItem) { // TODO переделать
        rollItem.id?.let {
            db = RoomDb.provideDb(context)
            db.daoRoll().update(it, rollItem.isCheck)
            db.daoNote().update(noteItem)
            db.close()
        }
    }

    override fun updateRollCheck(noteItem: NoteItem, isAll: Boolean) {
        db = RoomDb.provideDb(context)
        db.daoRoll().updateAllCheck(noteItem.id, isAll)
        db.daoNote().update(noteItem)
        db.close()
    }

    /**
     * NotesViewModel
     */


    override fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean) {
        db = RoomDb.provideDb(context)
        db.daoRoll().updateAllCheck(noteItem.id, check)
        db.daoNote().update(noteItem)
        db.close()
    }

    override fun updateNoteItemBind(id: Long, status: Boolean) {
        db = RoomDb.provideDb(context)
        db.daoNote().update(id, status)
        db.close()
    }

    override fun updateNoteItem(noteItem: NoteItem) {
        db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem)
        db.close()
    }

    override fun deleteNoteItem(id: Long) {
        db = RoomDb.provideDb(context)
        db.daoNote().update(id, context.getTime(), true)
        db.daoNote().update(id, false)
        db.close()
    }


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