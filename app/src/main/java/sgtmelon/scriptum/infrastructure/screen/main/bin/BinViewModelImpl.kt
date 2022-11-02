package sgtmelon.scriptum.infrastructure.screen.main.bin

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.onBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.clearAdd
import sgtmelon.scriptum.infrastructure.utils.removeAtOrNull
import sgtmelon.test.idling.getIdling

class BinViewModelImpl(
    lifecycle: Lifecycle,
    private val getList: GetNoteListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearBin: ClearBinUseCase,
    private val clearNote: ClearNoteUseCase
) : ViewModel(),
    DefaultLifecycleObserver,
    BinViewModel {

    init {
        lifecycle.addObserver(this)
    }

    // TODO check how it work (after rotation)
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.i("HERE", "onUpdate!")
        viewModelScope.launchBack { fetchList() }
    }

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    /**
     * There is no reason check current [showList] state for skip identical values (like it done
     * for [NotificationsViewModelImpl.showList]). Because here we only can remove items from
     * list, without ability to undo this action.
     */
    private fun notifyShowList() {
        showList.postValue(if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List)
    }

    override val itemList: MutableLiveData<List<NoteItem>> = MutableLiveData()

    /** This list needed because don't want put mutable list inside liveData. */
    private val _itemList: MutableList<NoteItem> = mutableListOf()

    private suspend fun fetchList() {
        getIdling().start(IdlingTag.Bin.LOAD_DATA)

        showList.postValue(ShowListState.Loading)
        _itemList.clearAdd(getList(isBin = true))
        itemList.postValue(_itemList)
        notifyShowList()

        getIdling().stop(IdlingTag.Bin.LOAD_DATA)
    }

    override fun clearRecyclerBin() {
        viewModelScope.launchBack { clearBin() }

        _itemList.clear()
        itemList.postValue(_itemList)
        notifyShowList()
    }

    override fun restoreNote(p: Int) {
        val item = _itemList.removeAtOrNull(p) ?: return

        viewModelScope.launchBack { restoreNote(item) }

        itemList.postValue(_itemList)
        notifyShowList()
    }

    override fun getCopyText(p: Int): Flow<String> = flow {
        val item = _itemList.getOrNull(p) ?: return@flow
        emit(getCopyText(item))
    }.onBack()

    override fun clearNote(p: Int) {
        val item = _itemList.removeAtOrNull(p) ?: return

        viewModelScope.launchBack { clearNote(item) }

        itemList.postValue(_itemList)
        notifyShowList()
    }

    //region Cleanup


    //    @RunPrivate val itemList: MutableList<NoteItem> = ArrayList()
    //
    //    override fun onSetup(bundle: Bundle?) {
    //        callback?.setupToolbar()
    //        callback?.setupRecycler()
    //        callback?.setupDialog()
    //
    //        callback?.prepareForLoad()
    //    }


    //    override fun onUpdateData() {
    //        getIdling().start(IdlingTag.Bin.LOAD_DATA)
    //
    //        fun updateList() = callback?.apply {
    //            notifyList(itemList)
    //            notifyMenuClearBin()
    //            onBindingList()
    //        }
    //
    //        /**
    //         * If was rotation need show list. After that fetch updates.
    //         */
    //        if (itemList.isNotEmpty()) updateList()
    //
    //        viewModelScope.launch {
    //            val count = runBack { interactor.getCount() }
    //
    //            if (count == 0) {
    //                itemList.clear()
    //            } else {
    //                if (itemList.isEmpty()) {
    //                    callback?.hideEmptyInfo()
    //                    callback?.showProgress()
    //                }
    //
    //                runBack { itemList.clearAdd(getList(isBin = true)) }
    //            }
    //
    //            updateList()
    //
    //            getIdling().stop(IdlingTag.Bin.LOAD_DATA)
    //        }
    //    }
    //
    //    override fun onClickClearBin() {
    //        viewModelScope.launchBack { clearBin() }
    //
    //        itemList.clear()
    //
    //        callback?.apply {
    //            notifyList(itemList)
    //            notifyMenuClearBin()
    //            onBindingList()
    //        }
    //    }
    //
    //    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    //    override fun onShowOptionsDialog(item: NoteItem, p: Int) {
    //        val callback = callback ?: return
    //
    //        val title = item.name.ifEmpty { callback.getString(R.string.empty_note_name) }
    //        val itemArray = callback.getStringArray(R.array.dialog_menu_bin)
    //
    //        callback.showOptionsDialog(title, itemArray, p)
    //    }
    //
    //    override fun onResultOptionsDialog(p: Int, @Options which: Int) {
    //        when (which) {
    //            Options.RESTORE -> onMenuRestore(p)
    //            Options.COPY -> onMenuCopy(p)
    //            Options.CLEAR -> onMenuClear(p)
    //        }
    //    }
    //
    //    @RunPrivate fun onMenuRestore(p: Int) {
    //        val item = itemList.removeAtOrNull(p) ?: return
    //
    //        viewModelScope.launchBack { restoreNote(item) }
    //
    //        callback?.notifyList(itemList)
    //        callback?.notifyMenuClearBin()
    //    }
    //
    //    @RunPrivate fun onMenuCopy(p: Int) {
    //        val item = itemList.getOrNull(p) ?: return
    //
    //        viewModelScope.launch {
    //            val text = runBack { getCopyText(item) }
    //            callback?.copyClipboard(text)
    //        }
    //    }
    //
    //    @RunPrivate fun onMenuClear(p: Int) {
    //        val item = itemList.removeAtOrNull(p) ?: return
    //
    //        viewModelScope.launchBack { clearNote(item) }
    //
    //        callback?.notifyList(itemList)
    //        callback?.notifyMenuClearBin()
    //    }
    //
    //endregion

}