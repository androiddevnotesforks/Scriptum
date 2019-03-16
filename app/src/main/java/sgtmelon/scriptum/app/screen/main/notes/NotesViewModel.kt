package sgtmelon.scriptum.app.screen.main.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.office.utils.TimeUtils.getTime


/**
 * ViewModel для [NotesFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: NotesCallback

    private val listNoteModel: MutableList<NoteModel> = ArrayList()

    fun onUpdateData() {
        val list = iRoomRepo.getNoteRepoList(fromBin = false)

        listNoteModel.clear()
        listNoteModel.addAll(list)

        callback.notifyDataSetChanged(listNoteModel)
        callback.bind()

        if (updateStatus) updateStatus = false
    }

    fun onClickNote(p: Int) {
        val noteItem = listNoteModel[p].noteItem
        callback.startNote(context.getNoteIntent(noteItem.type, noteItem.id))
    }

    fun onShowOptionsDialog(p: Int) {
        val noteItem = listNoteModel[p].noteItem
        val itemArray: Array<String>

        when (noteItem.type) {
            NoteType.TEXT -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_text)

                itemArray[0] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }
            }
            NoteType.ROLL -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_roll)

                itemArray[0] = when (noteItem.isAllCheck) {
                    true -> context.getString(R.string.dialog_menu_check_zero)
                    false -> context.getString(R.string.dialog_menu_check_all)
                }

                itemArray[1] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }

            }
        }

        callback.showOptionsDialog(itemArray, p)
    }

    fun onResultOptionsDialog(p: Int, which: Int) {
        val noteItem = listNoteModel[p].noteItem

        when (noteItem.type) {
            NoteType.TEXT -> when (which) {
                OptionsDef.Text.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Text.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Text.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Text.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
            NoteType.ROLL -> when (which) {
                OptionsDef.Roll.check -> callback.notifyItemChanged(p, onMenuCheck(p))
                OptionsDef.Roll.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Roll.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Roll.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Roll.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
        }
    }

    private fun onMenuCheck(p: Int): MutableList<NoteModel> {
        val noteRepo = listNoteModel[p]
        val noteItem = noteRepo.noteItem

        val checkText = noteItem.check
        val isAll = checkText[0] == checkText[1]

        noteItem.change = context.getTime()
        noteItem.setRollText(if (isAll) 0 else checkText[1], checkText[1])

        iRoomRepo.updateNoteItemCheck(noteItem, !isAll)

        noteRepo.updateCheck(!isAll)
        noteRepo.statusItem.updateNote(noteItem)

        return listNoteModel
    }

    private fun onMenuBind(p: Int): MutableList<NoteModel> {
        val noteRepo = listNoteModel[p]
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.updateStatus(noteItem.isStatus)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)

        return listNoteModel
    }

    private fun onMenuConvert(p: Int): MutableList<NoteModel> { // TODO !! repo
        val noteRepo = listNoteModel[p]
        val noteItem = noteRepo.noteItem

        noteItem.change = context.getTime()

        when (noteItem.type) {
            NoteType.TEXT -> {
                val db = RoomDb.getInstance(context)

                val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

                noteItem.type = NoteType.ROLL
                noteItem.setRollText(0, listRoll.size)

                db.daoNote().update(noteItem)

                noteRepo.listRoll = listRoll

                db.close()
            }
            NoteType.ROLL -> {
                val db = RoomDb.getInstance(context)

                noteItem.type = NoteType.TEXT
                noteItem.text = db.daoRoll().getText(noteItem.id)

                db.daoNote().update(noteItem)
                db.daoRoll().delete(noteItem.id)

                noteRepo.listRoll = ArrayList()

                db.close()
            }
        }

        noteRepo.statusItem.updateNote(noteItem)

        return listNoteModel
    }

    private fun onMenuDelete(p: Int): MutableList<NoteModel> {
        iRoomRepo.deleteNoteItem(listNoteModel[p].noteItem.id)

        listNoteModel[p].updateStatus(false)
        listNoteModel.removeAt(p)

        return listNoteModel
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

}