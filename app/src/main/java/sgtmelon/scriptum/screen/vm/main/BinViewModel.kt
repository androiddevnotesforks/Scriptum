package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.utils.AppUtils.clearAndAdd
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.screen.callback.main.BinCallback
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: BinCallback

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    fun onUpdateData() {
        noteModelList.clearAndAdd(iRoomRepo.getNoteModelList(bin = true))

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

    fun onClickNote(p: Int) = with(noteModelList[p].noteItem) {
        callback.startNote(context.getNoteIntent(type, id))
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
        iRoomRepo.restoreNote(noteModelList[p].noteItem)

        return noteModelList.apply { removeAt(p) }
    }

    private fun clearItem(p: Int): MutableList<NoteModel> {
        iRoomRepo.clearNote(noteModelList[p].noteItem)

        return noteModelList.apply { removeAt(p) }
    }

}