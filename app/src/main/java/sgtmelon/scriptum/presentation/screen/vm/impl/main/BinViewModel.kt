package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.domain.model.annotation.Options.Bin as Options

/**
 * ViewModel for [BinFragment].
 */
class BinViewModel(application: Application) : ParentViewModel<IBinFragment>(application),
        IBinViewModel {

    private lateinit var interactor: IBinInteractor

    fun setInteractor(interactor: IBinInteractor) {
        this.interactor = interactor
    }


    @VisibleForTesting
    val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler(interactor.theme)
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    override fun onUpdateData() {
        callback?.beforeLoad()

        fun updateList() = callback?.apply {
            notifyList(itemList)
            notifyMenuClearBin()
            onBindingList()
        }

        /**
         * If was rotation need show list. After that fetch updates.
         */
        if (itemList.isNotEmpty()) updateList()

        viewModelScope.launch {
            if (interactor.getCount() == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.showProgress()
                }

                itemList.clearAddAll(interactor.getList())
            }

            updateList()
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launch { interactor.clearBin() }

        itemList.clear()

        callback?.apply {
            notifyDataSetChanged(itemList)
            notifyMenuClearBin()
            onBindingList()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(item = itemList.getOrNull(p) ?: return)
    }

    override fun onShowOptionsDialog(p: Int) {
        val itemArray = callback?.getStringArray(R.array.dialog_menu_bin) ?: return

        callback?.showOptionsDialog(itemArray, p)
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.RESTORE -> onMenuRestore(p)
            Options.COPY -> onMenuCopy(p)
            Options.CLEAR -> onMenuClear(p)
        }
    }

    private fun onMenuRestore(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return

        viewModelScope.launch { interactor.restoreNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

    private fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch { interactor.copy(item) }
    }

    private fun onMenuClear(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return

        viewModelScope.launch { interactor.clearNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

}