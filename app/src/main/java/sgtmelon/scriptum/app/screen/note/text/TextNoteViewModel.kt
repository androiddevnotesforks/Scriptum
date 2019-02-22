package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.screen.note.ParentNoteViewModel
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : ParentNoteViewModel(application) {

    override fun onConvertDialog() {
        val noteItem = noteRepo.noteItem

        val db = RoomDb.provideDb(context)
        val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

        noteItem.change = TimeUtils.getTime(context)
        noteItem.type = NoteType.ROLL
        noteItem.setText(0, listRoll.size)

        db.daoNote().update(noteItem)
        db.close()

        noteRepo.listRoll = listRoll

        noteCallback.viewModel.noteRepo = noteRepo
        noteCallback.setupFragment(false)
    }

}