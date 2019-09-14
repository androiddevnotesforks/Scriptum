package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.copyToClipboard
import sgtmelon.scriptum.interactor.bin.BinInteractor
import sgtmelon.scriptum.interactor.bin.IBinInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel

/**
 * ViewModel for [BinFragment]
 */
class BinViewModel(application: Application) : ParentViewModel<IBinFragment>(application),
        IBinViewModel {

    private val iBinInteractor: IBinInteractor = BinInteractor(context)

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iBinInteractor.theme)
        }
    }

    override fun onUpdateData() {
        noteModelList.clearAndAdd(iBinInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launch { iBinInteractor.clearBin() }

        noteModelList.clear()

        callback?.apply {
            notifyDataSetChanged(noteModelList)
            notifyMenuClearBin()
            bind()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startActivity(NoteActivity.getInstance(context, noteModelList[p].noteEntity))
    }

    override fun onShowOptionsDialog(p: Int) {
        callback?.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.RESTORE -> callback?.notifyItemRemoved(p, restoreItem(p))
            Options.COPY -> context.copyToClipboard(noteModelList[p].noteEntity)
            Options.CLEAR -> callback?.notifyItemRemoved(p, clearItem(p))
        }

        callback?.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int) = noteModelList.apply {
        get(p).let { viewModelScope.launch { iBinInteractor.restoreNote(it) } }
        removeAt(p)
    }

    private fun clearItem(p: Int) = noteModelList.apply {
        get(p).let { viewModelScope.launch { iBinInteractor.clearNote(it) } }
        removeAt(p)
    }


    @IntDef(Options.RESTORE, Options.COPY, Options.CLEAR)
    private annotation class Options {
        companion object {
            const val RESTORE = 0
            const val COPY = 1
            const val CLEAR = 2
        }
    }

}