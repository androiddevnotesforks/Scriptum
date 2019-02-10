package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.fragment.BinFragment
import sgtmelon.scriptum.office.annot.def.BinDef
import sgtmelon.scriptum.office.utils.HelpUtils
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

    fun getId(p: Int) = listNoteRepo[p].noteItem.id

    fun onMenuRestore(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().update(getId(p), TimeUtils.getTime(context), false)
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

    fun onMenuCopy(p: Int) = HelpUtils.optionsCopy(context, listNoteRepo[p].noteItem)

    fun onMenuClear(p: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        db.daoNote().delete(getId(p))
        db.close()

        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

}