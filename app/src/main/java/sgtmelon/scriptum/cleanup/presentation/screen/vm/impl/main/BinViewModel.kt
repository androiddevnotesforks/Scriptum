package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.validRemoveAt
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.test.idling.getIdling
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options.Bin as Options

/**
 * ViewModel for [IBinFragment].
 */
class BinViewModel(
    callback: IBinFragment,
    private val interactor: IBinInteractor,
    private val getCopyText: GetCopyTextUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearNote: ClearNoteUseCase
) : ParentViewModel<IBinFragment>(callback),
        IBinViewModel {

    @RunPrivate val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupDialog()

        callback?.prepareForLoad()
    }


    override fun onUpdateData() {
        getIdling().start(IdlingTag.Bin.LOAD_DATA)

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

            getIdling().stop(IdlingTag.Bin.LOAD_DATA)
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
        val title = item.name.ifEmpty { callback.getString(R.string.hint_text_name) }
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

        viewModelScope.launchBack { restoreNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }

    @RunPrivate fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            val text = runBack { getCopyText(item) }
            callback?.copyClipboard(text)
        }
    }

    @RunPrivate fun onMenuClear(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        viewModelScope.launchBack { clearNote(item) }

        callback?.notifyItemRemoved(itemList, p)
        callback?.notifyMenuClearBin()
    }
}