package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.copyToClipboard
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.callback.main.IBinFragment
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel for [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinViewModel(application: Application) : ParentViewModel<IBinFragment>(application),
        IBinViewModel {

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    override fun onSetup() {
        callback?.apply {
            setupToolbar()
            setupRecycler(iPreferenceRepo.theme)
        }
    }

    override fun onUpdateData() {
        noteModelList.clearAndAdd(iRoomRepo.getNoteModelList(bin = true))

        callback?.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launch { iRoomRepo.clearBin() }

        noteModelList.clear()

        callback?.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    override fun onClickNote(p: Int) {
        with(noteModelList[p].noteEntity) {
            callback?.startActivity(NoteActivity.getInstance(context, type, id))
        }
    }

    override fun onShowOptionsDialog(p: Int) {
        callback?.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            OptionsBin.restore -> callback?.notifyItemRemoved(p, restoreItem(p))
            OptionsBin.copy -> context.copyToClipboard(noteModelList[p].noteEntity)
            OptionsBin.clear -> callback?.notifyItemRemoved(p, clearItem(p))
        }

        callback?.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int) = noteModelList.apply {
        get(p).noteEntity.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        removeAt(p)
    }

    private fun clearItem(p: Int) = noteModelList.apply {
        get(p).noteEntity.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
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