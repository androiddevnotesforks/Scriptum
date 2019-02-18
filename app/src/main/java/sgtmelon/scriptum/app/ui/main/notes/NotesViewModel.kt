package sgtmelon.scriptum.app.ui.main.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [NotesFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    fun loadData(): MutableList<NoteRepo> {
        val db = RoomDb.provideDb(context)
        listNoteRepo.clear()
        listNoteRepo.addAll(db.daoNote().get(context, false))
        db.close()

        return listNoteRepo
    }

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
        db.daoRoll().update(noteItem.id, check)
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
            NoteType.TEXT -> {
                val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

                noteItem.type = NoteType.ROLL
                noteItem.setText(0, listRoll.size)

                db.daoNote().update(noteItem)

                noteRepo.listRoll = listRoll
            }
            NoteType.ROLL -> {
                noteItem.type = NoteType.TEXT
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