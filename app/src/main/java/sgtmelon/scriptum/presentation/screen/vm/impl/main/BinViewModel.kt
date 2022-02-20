package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.common.test.idling.impl.AppIdlingResource
import sgtmelon.scriptum.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.model.annotation.Options.Bin as Options

/**
 * ViewModel for [IBinFragment].
 */
class BinViewModel(application: Application) : ParentViewModel<IBinFragment>(application),
        IBinViewModel {

    private lateinit var interactor: IBinInteractor

    fun setInteractor(interactor: IBinInteractor) {
        this.interactor = interactor
    }


    @RunPrivate val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupDialog()

        callback?.prepareForLoad()
    }


    override fun onUpdateData() {
        AppIdlingResource.getInstance().startWork(IdlingTag.Bin.LOAD_DATA)

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
            val count = runBack { interactor.getCount() }

            if (count == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.hideEmptyInfo()
                    callback?.showProgress()
                }

                runBack { itemList.clearAdd(interactor.getList()) }
            }

            updateList()

            AppIdlingResource.getInstance().stopWork(IdlingTag.Bin.LOAD_DATA)
        }
    }

    override fun onClickClearBin() {
        viewModelScope.launchBack { interactor.clearBin() }

        itemList.clear()

        callback?.apply {
            notifyDataSetChanged(itemList)
            notifyMenuClearBin()
            onBindingList()
        }
    }

    override fun onClickNote(p: Int) {
        callback?.openNoteScreen(item = itemList.getOrNull(p) ?: return)
    }

    override fun onShowOptionsDialog(p: Int) {
        val callback = callback ?: return

        val item = itemList.getOrNull(p) ?: return
        val title = if (item.name.isNotEmpty()) {
            item.name
        } else {
            callback.getString(R.string.hint_text_name)
        }
        val itemArray = callback.getStringArray(R.array.dialog_menu_bin)

        callback.showOptionsDialog(title, itemArray, p)
    }

    override fun onResultOptionsDialog(p: Int, @Options which: Int) {
        when (which) {
            Options.RESTORE -> onMenuRestore(p)
            Options.COPY -> onMenuCopy(p)
            Options.CLEAR -> onMenuClear(p)
        }
    }

    @RunPrivate fun onMenuRestore(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        viewModelScope.launchBack { interactor.restoreNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

    @RunPrivate fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            val text = runBack { interactor.copy(item) }
            callback?.copyClipboard(text)
        }
    }

    @RunPrivate fun onMenuClear(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        viewModelScope.launchBack { interactor.clearNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

}