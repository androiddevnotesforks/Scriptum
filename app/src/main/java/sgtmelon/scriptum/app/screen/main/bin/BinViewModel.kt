package sgtmelon.scriptum.app.screen.main.bin

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.screen.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard

/**
 * ViewModel для [BinFragment]
 */
class BinViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: BinCallback

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    fun onUpdateData() {
        val list = iRoomRepo.getNoteRepoList(fromBin = true)

        noteModelList.clear()
        noteModelList.addAll(list)

        callback.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    fun onClickClearBin() {
        iRoomRepo.clearBin()

        noteModelList.clear()

        callback.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    fun onClickNote(p: Int) {
        val noteItem = noteModelList[p].noteItem
        callback.startNote(context.getNoteIntent(noteItem.type, noteItem.id))
    }

    fun onShowOptionsDialog(p: Int) =
            callback.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)

    fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            OptionsDef.Bin.restore -> callback.notifyItemRemoved(p, restoreItem(p))
            OptionsDef.Bin.copy -> context.copyToClipboard(noteModelList[p].noteItem)
            OptionsDef.Bin.clear -> callback.notifyItemRemoved(p, clearItem(p))
        }

        callback.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int): MutableList<NoteModel> {
        iRoomRepo.restoreNoteItem(noteModelList[p].noteItem.id)

        noteModelList.removeAt(p)
        return noteModelList
    }

    private fun clearItem(p: Int): MutableList<NoteModel> {
        iRoomRepo.clearNoteItem(noteModelList[p].noteItem.id)

        noteModelList.removeAt(p)
        return noteModelList
    }

}