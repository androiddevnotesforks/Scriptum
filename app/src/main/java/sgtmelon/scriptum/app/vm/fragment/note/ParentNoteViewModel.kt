package sgtmelon.scriptum.app.vm.fragment.note

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.callback.NoteCallback
import java.util.ArrayList

abstract class ParentNoteViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    lateinit var noteRepo: NoteRepo

    lateinit var noteCallback: NoteCallback
    lateinit var inputControl: InputControl

    fun getNoteColor(): Int = noteRepo.noteItem.color

    abstract fun onConvertDialog()

    fun onColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check
    }

    fun getRankDialogName() : Array<String> {
        val db = RoomDb.provideDb(context)
        val name: Array<String> = db.daoRank().name
        db.close()

        return name
    }

    fun onRankDialog(check: BooleanArray) {
        val db = RoomDb.provideDb(context)
        val id: Array<Long> = db.daoRank().id
        db.close()

        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        for (i in id.indices) {
            if (check[i]) {
                rankId.add(id[i])
                rankPs.add(i.toLong())
            }
        }

        val noteItem = noteRepo.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs
    }

    fun onMenuRank(): BooleanArray {
        val db = RoomDb.provideDb(context)
        val check = db.daoRank().getCheck(noteRepo.noteItem.rankId)
        db.close()

        return check;
    }

    fun onMenuBind() {
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.update(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        noteRepo.noteItem = noteItem
        noteCallback.viewModel.noteRepo = noteRepo
    }

}