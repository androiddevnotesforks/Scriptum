package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.view.fragment.main.NotesFragment
import sgtmelon.scriptum.office.annot.def.BinDef
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.def.TypeNoteDef
import sgtmelon.scriptum.office.utils.HelpUtils
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [NotesFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    fun loadData(@BinDef bin: Int): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        listNoteRepo.clear()
        listNoteRepo.addAll(db.daoNote().get(context, bin))
        db.close()

        return listNoteRepo
    }

    fun getId(p: Int) = listNoteRepo[p].noteItem.id

    fun getNoteItem(p: Int): NoteItem = listNoteRepo[p].noteItem

    fun onMenuCheck(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        val checkText = noteItem.check
        val check = when (checkText[0] == checkText[1]) {
            true -> CheckDef.notDone
            false -> CheckDef.done
        }

        noteItem.change = TimeUtils.getTime(context)
        noteItem.setText(when (check == CheckDef.notDone) {
            true -> 0
            false -> checkText[1]
        }, checkText[1])

        val db = RoomDb.provideDb(context)
        db.daoRoll().update(noteItem.getId(), check)
        db.daoNote().update(noteItem)
        db.close()

        noteRepo.update(check)
        noteRepo.statusItem.updateNote(noteItem, true)

        return listNoteRepo
    }

    fun onMenuBind(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.update(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        return listNoteRepo
    }

    fun onMenuConvert(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.change = TimeUtils.getTime(context)

        val db = RoomDb.provideDb(context)
        when (noteItem.type) {
            TypeNoteDef.text -> {
                val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

                noteItem.type = TypeNoteDef.roll
                noteItem.setText(0, listRoll.size)

                db.daoNote().update(noteItem)

                noteRepo.listRoll = listRoll
            }
            TypeNoteDef.roll -> {
                noteItem.type = TypeNoteDef.text
                noteItem.text = db.daoRoll().getText(noteItem.id)

                db.daoNote().update(noteItem)
                db.daoRoll().delete(noteItem.id)

                noteRepo.listRoll = ArrayList()
            }
        }
        db.close()

        noteRepo.statusItem.updateNote(noteItem, true)

        return listNoteRepo
    }

    fun onMenuCopy(p: Int) = HelpUtils.optionsCopy(context, listNoteRepo[p].noteItem)

    fun onMenuDelete(p: Int): MutableList<NoteRepo> {
        val noteItem = listNoteRepo[p].noteItem

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, TimeUtils.getTime(context), true)
        if (noteItem.isStatus) db.daoNote().update(noteItem.id, false)
        db.close()

        listNoteRepo[p].update(false)
        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

}