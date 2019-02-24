package sgtmelon.scriptum.app.repository

import android.content.Context
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

class RoomRepo(private val context: Context) : IRoomRepo {

    private lateinit var db: RoomDb

    /**
     * BinViewModel
     */

    override fun getNoteRepoList(fromBin: Boolean): MutableList<NoteRepo> {
        db = RoomDb.provideDb(context)
        val noteRepoList: MutableList<NoteRepo> = db.daoNote().get(context, fromBin)
        db.close()

        return noteRepoList
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


    /**
     *
     */

    override fun deleteNoteItem(id: Long) {
        db = RoomDb.provideDb(context)
        db.daoNote().update(id, context.getTime(), true)
        db.close()
    }

    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}