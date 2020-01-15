package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAddAll
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
        callback?.beforeLoad()

        /**
         * If was rotation need show list and after that check for updates.
         */
        if (itemList.isNotEmpty()) {
            callback?.apply {
                notifyList(itemList)
                onBindingList()
            }
        }

        viewModelScope.launch {
            val count = iInteractor.getCount()

            if (count == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) callback?.showProgress()
                itemList.clearAddAll(iInteractor.getList())
            }

            callback?.apply {
                notifyList(itemList)
                notifyMenuClearBin()
                onBindingList()
            }
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launch { iInteractor.clearBin() }

        itemList.clear()

        callback?.apply {
            notifyDataSetChanged(itemList)
            notifyMenuClearBin()
            onBindingList()
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
            Options.RESTORE -> restoreItem(p)
            Options.COPY -> viewModelScope.launch { iInteractor.copy(itemList[p]) }
            Options.CLEAR -> clearItem(p)
        }
    }

    private fun restoreItem(p: Int) {
        val item = itemList.removeAt(p)
        viewModelScope.launch { iInteractor.restoreNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

    private fun clearItem(p: Int) {
        val item = itemList.removeAt(p)
        viewModelScope.launch { iInteractor.clearNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

}