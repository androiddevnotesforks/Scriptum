package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.fragment.BinFragment
import sgtmelon.scriptum.app.view.fragment.NotesFragment
import sgtmelon.scriptum.office.annot.def.BinDef

/**
 * ViewModel для [NotesFragment] и [BinFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    var listNoteRepo: List<NoteRepo> = ArrayList()

    fun loadData(@BinDef bin: Int): List<NoteRepo> {
        val context = getApplication<Application>().applicationContext

        val db = RoomDb.provideDb(context)
        listNoteRepo = db.daoNote().get(context, bin)
        db.close()

        return listNoteRepo
    }

}