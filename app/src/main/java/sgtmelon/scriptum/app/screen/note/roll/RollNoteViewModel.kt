package sgtmelon.scriptum.app.screen.note.roll

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils
import java.util.*

/**
 * ViewModel для [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    lateinit var noteRepo: NoteRepo

    lateinit var noteCallback: NoteCallback
    lateinit var inputControl: InputControl

    fun getNoteColor(): Int = noteRepo.noteItem.color

    fun onConvertDialog() {
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

    fun onColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check
    }

    fun getRankDialogName(): Array<String> {
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
        noteRepo.updateStatus(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        noteRepo.noteItem = noteItem
        noteCallback.viewModel.noteRepo = noteRepo
    }

    fun onMenuCheck(isAll: Boolean): NoteItem {
        val key: Int = if (isAll) CheckDef.notDone else CheckDef.done

        val size: Int = noteRepo.listRoll.size
        val check: Int = if (isAll) 0 else size

        noteRepo.updateCheck(key)

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
