package sgtmelon.scriptum.app.screen.main.bin

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteRepo
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

    private val noteRepoList: MutableList<NoteRepo> = ArrayList()

    fun onUpdateData() {
        val list = iRoomRepo.getNoteRepoList(fromBin = true)

        noteRepoList.clear()
        noteRepoList.addAll(list)

        callback.notifyDataSetChanged(noteRepoList)
        callback.notifyMenuClearBin()
        callback.bind()
    }

    fun onClickClearBin() {
        iRoomRepo.clearBin()

        noteRepoList.clear()

        callback.notifyDataSetChanged(noteRepoList)
        callback.notifyMenuClearBin()
        callback.bind()
    }

    fun onClickNote(p: Int) {
        val noteItem = noteRepoList[p].noteItem
        callback.startNote(context.getNoteIntent(noteItem.type, noteItem.id))
    }

    fun onShowOptionsDialog(p: Int) =
            callback.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)

    fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            OptionsDef.Bin.restore -> callback.notifyItemRemoved(p, restoreItem(p))
            OptionsDef.Bin.copy -> context.copyToClipboard(noteRepoList[p].noteItem)
            OptionsDef.Bin.clear -> callback.notifyItemRemoved(p, clearItem(p))
        }

        callback.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int): MutableList<NoteRepo> {
        iRoomRepo.restoreNoteItem(noteRepoList[p].noteItem.id)

        noteRepoList.removeAt(p)

        return noteRepoList
    }

    private fun clearItem(p: Int): MutableList<NoteRepo> {
        iRoomRepo.clearNoteItem(noteRepoList[p].noteItem.id)

        noteRepoList.removeAt(p)

        return noteRepoList
    }

}