package sgtmelon.scriptum.app.ui.main.bin

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [BinFragment]
 */
class BinViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    lateinit var callback: BinCallback

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    fun onLoadData(): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        listNoteRepo.clear()
        listNoteRepo.addAll(db.daoNote().get(context, true))
        db.close()

        callback.notifyDataSetChanged(listNoteRepo)
        callback.notifyMenuClearBin()
        callback.bind()

        return listNoteRepo
    }

    fun clearBin() {
        val db = RoomDb.provideDb(context)
        db.daoNote().clearBin()
        db.close()

        listNoteRepo.clear()

        callback.notifyDataSetChanged(listNoteRepo)
        callback.notifyMenuClearBin()
        callback.bind()
    }

    fun onClickDialog(p: Int, which: Int) {
        when (which) {
            OptionsDef.Bin.restore -> callback.notifyItemRemoved(restoreItem(p), p)
            OptionsDef.Bin.copy -> context.copyToClipboard(getNoteItem(p))
            OptionsDef.Bin.clear -> callback.notifyItemRemoved(clearItem(p), p)
        }

        callback.notifyMenuClearBin()
    }

    fun getNoteItem(p: Int): NoteItem = listNoteRepo[p].noteItem

    private fun restoreItem(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().update(getNoteItem(p).id, TimeUtils.getTime(context), false)
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

    private fun clearItem(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().delete(getNoteItem(p).id)
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

}