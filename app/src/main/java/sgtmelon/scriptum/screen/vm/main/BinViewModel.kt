package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.model.annotation.Options.Bin as Options

/**
 * ViewModel for [BinFragment]
 */
class BinViewModel(application: Application) : ParentViewModel<IBinFragment>(application),
        IBinViewModel {

    private val iInteractor: IBinInteractor by lazy { BinInteractor(context, callback) }

    private val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            notifyMenuClearBin()
            bindList()
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launch { iInteractor.clearBin() }

        itemList.clear()

        callback?.apply {
            notifyDataSetChanged(itemList)
            notifyMenuClearBin()
            bindList()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(itemList[p])
    }

    override fun onShowOptionsDialog(p: Int) {
        callback?.showOptionsDialog(context.resources.getStringArray(R.array.dialog_menu_bin), p)
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.RESTORE -> callback?.notifyItemRemoved(p, restoreItem(p))
            Options.COPY -> viewModelScope.launch { iInteractor.copy(itemList[p]) }
            Options.CLEAR -> callback?.notifyItemRemoved(p, clearItem(p))
        }

        callback?.notifyMenuClearBin()
    }

    private fun restoreItem(p: Int) = itemList.apply {
        get(p).let { viewModelScope.launch { iInteractor.restoreNote(it) } }
        removeAt(p)
    }

    private fun clearItem(p: Int) = itemList.apply {
        get(p).let { viewModelScope.launch { iInteractor.clearNote(it) } }
        removeAt(p)
    }

}