package sgtmelon.scriptum.app.screen.main.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.office.utils.TimeUtils.getTime


/**
 * ViewModel для [NotesFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: NotesCallback

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    fun onLoadData() {
        val list = iRoomRepo.getNoteRepoList(fromBin = false)

        listNoteRepo.clear()
        listNoteRepo.addAll(list)

        callback.notifyDataSetChanged(listNoteRepo)
        callback.bind()

        if (updateStatus) updateStatus = false
    }

    fun onClickNote(p: Int) {
        val noteItem = listNoteRepo[p].noteItem
        callback.startNote(context.getNoteIntent(noteItem.type, noteItem.id))
    }

    fun onShowOptionsDialog(p: Int) {
        val noteItem = listNoteRepo[p].noteItem
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

    fun onClickOptionsDialog(p: Int, which: Int) {
        val noteItem = listNoteRepo[p].noteItem

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

    private fun onMenuCheck(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        val checkText = noteItem.check
        val check = when (checkText[0] == checkText[1]) {
            true -> CheckDef.notDone
            false -> CheckDef.done
        }

        noteItem.change = context.getTime()
        noteItem.setText(when (check == CheckDef.notDone) {
            true -> 0
            false -> checkText[1]
        }, checkText[1])

        iRoomRepo.updateNoteItemCheck(noteItem, check)

        noteRepo.updateCheck(check)
        noteRepo.statusItem.updateNote(noteItem, true)

        return listNoteRepo
    }

    private fun onMenuBind(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.updateStatus(noteItem.isStatus)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)

        return listNoteRepo
    }

    private fun onMenuConvert(p: Int): MutableList<NoteRepo> { // TODO
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.change = context.getTime()

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

    private fun onMenuDelete(p: Int): MutableList<NoteRepo> {
        iRoomRepo.deleteNoteItem(listNoteRepo[p].noteItem.id)

        listNoteRepo[p].updateStatus(false)
        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

}