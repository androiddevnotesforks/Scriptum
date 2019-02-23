package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.TimeUtils
import java.util.*

/**
 * ViewModel для [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : AndroidViewModel(application), MenuCallback {

    private val context: Context = application.applicationContext

    lateinit var noteRepo: NoteRepo

    lateinit var noteCallback: NoteCallback
    lateinit var inputControl: InputControl

    fun getNoteColor(): Int = noteRepo.noteItem.color

    fun onConvertDialog() {
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
        noteRepo.updateStatus(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        noteRepo.noteItem = noteItem
        noteCallback.viewModel.noteRepo = noteRepo
    }

    override fun onRestoreClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRestoreOpenClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClearClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUndoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRedoClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRankClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onColorClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveClick(changeMode: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCheckClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConvertClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEditClick(mode: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}