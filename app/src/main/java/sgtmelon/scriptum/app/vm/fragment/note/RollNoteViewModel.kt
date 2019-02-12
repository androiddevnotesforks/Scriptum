package sgtmelon.scriptum.app.vm.fragment.note

import android.app.Application
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.view.fragment.note.RollNoteFragment
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : ParentNoteViewModel(application) {

    override fun onConvertDialog() {
        val db = RoomDb.provideDb(context)
        val noteItem = noteRepo.noteItem

        noteItem.change = TimeUtils.getTime(context)
        noteItem.type = NoteType.TEXT
        noteItem.text = db.daoRoll().getText(noteItem.id)

        db.daoNote().update(noteItem)
        db.daoRoll().delete(noteItem.id)
        db.close()

        noteCallback.viewModel.noteRepo = noteRepo
        noteCallback.setupFragment(false)
    }

    fun onMenuCheck(isAll: Boolean): NoteItem {
        val key: Int = if (isAll) CheckDef.notDone else CheckDef.done

        val size: Int = noteRepo.listRoll.size
        val check: Int = if (isAll) 0 else size

        noteRepo.update(key)

        val noteItem = noteRepo.noteItem
        noteItem.change = TimeUtils.getTime(context)
        noteItem.setText(check, size)

        val db = RoomDb.provideDb(context)
        db.daoRoll().update(noteItem.id, key)
        db.daoNote().update(noteItem)
        db.close()

        noteCallback.viewModel.noteRepo = noteRepo

        return noteItem
    }

}
