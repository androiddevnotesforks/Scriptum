package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.office.utils.clearAndAdd
import sgtmelon.scriptum.office.utils.copyToClipboard
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

    fun onSetup() = callback.apply {
        setupToolbar()
        setupRecycler(preference.theme)
    }

    fun onUpdateData() {
        noteModelList.clearAndAdd(iRoomRepo.getNoteModelList(bin = true))

        callback.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    fun onClickClearBin() {
        viewModelScope.launch { iRoomRepo.clearBin() }

        noteModelList.clear()

        callback.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    fun onClickNote(p: Int) = with(noteModelList[p].noteItem) {
        callback.startActivity(context.getNoteIntent(type, id))
    }

    fun onShowOptionsDialog(p: Int) =
            callback.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)

    fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            OptionsBin.restore -> callback.notifyItemRemoved(p, restoreItem(p))
            OptionsBin.copy -> context.copyToClipboard(noteModelList[p].noteItem)
            OptionsBin.clear -> callback.notifyItemRemoved(p, clearItem(p))
        }

        callback.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int) = noteModelList.apply {
        get(p).noteItem.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        removeAt(p)
    }

    private fun clearItem(p: Int) = noteModelList.apply {
        get(p).noteItem.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
        removeAt(p)
    }

    companion object {

        object OptionsBin {
            const val restore = 0
            const val copy = 1
            const val clear = 2
        }

    }

}