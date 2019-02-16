package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.view.fragment.main.BinFragment
import sgtmelon.scriptum.office.annot.def.BinDef
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [BinFragment]
 */
class BinViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    fun loadData(@BinDef bin: Int): List<NoteRepo> {
        val db = RoomDb.provideDb(context)
        listNoteRepo.clear()
        listNoteRepo.addAll(db.daoNote().get(context, bin))
        db.close()

        return listNoteRepo
    }

    fun onClear(): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().clearBin()
        db.close()

        listNoteRepo.clear()

        return listNoteRepo
    }

    fun getNoteItem(p: Int): NoteItem = listNoteRepo[p].noteItem

    fun onMenuRestore(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().update(getNoteItem(p).id, TimeUtils.getTime(context), false)
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

    fun onMenuClear(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().delete(getNoteItem(p).id)
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

}