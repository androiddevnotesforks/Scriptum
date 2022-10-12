package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlin.math.max
import kotlinx.coroutines.launch
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.clearSpace
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.extension.validRemoveAt
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Snackbar
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [IRankFragment].
 */
class RankViewModel(
    callback: IRankFragment,
    private val interactor: IRankInteractor,
    private val getList: GetRankListUseCase,
    private val insertRank: InsertRankUseCase,
    private val deleteRank: DeleteRankUseCase,
    private val updateRank: UpdateRankUseCase,
    private val correctPositions: CorrectPositionsUseCase
) : ParentViewModel<IRankFragment>(callback),
        IRankViewModel {

    @RunPrivate val itemList: MutableList<RankItem> = mutableListOf()
    @RunPrivate val cancelList: MutableList<Pair<Int, RankItem>> = mutableListOf()

    private val nameList: List<String> get() = getNameList(itemList)

    /**
     * Variable for control drag state. TRUE - if drag state, FALSE - otherwise.
     */
    @RunPrivate var inTouchAction = false

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupDialog()

        callback?.prepareForLoad()

        if (bundle != null) {
            restoreSnackbar(bundle)
        }
    }

    /**
     * Restore saved snackbar data inside [onSaveData].
     */
    @RunPrivate fun restoreSnackbar(bundle: Bundle) {
        val positionArray = bundle.getIntArray(Snackbar.Intent.POSITIONS) ?: return
        val itemArray = bundle.getStringArray(Snackbar.Intent.ITEMS) ?: return

        /**
         * itemArray.isNotEmpty is implied.
         */
        if (positionArray.isNotEmpty() && positionArray.size == itemArray.size) {
            for (i in positionArray.indices) {
                val p = positionArray.getOrNull(i) ?: continue
                val json = itemArray.getOrNull(i) ?: continue
                val item = RankItem[json] ?: continue

                cancelList.add(Pair(p, item))
            }

            callback?.showSnackbar()
        }
    }


    /**
     * Save snackbar data and restore it inside [restoreSnackbar].
     */
    override fun onSaveData(bundle: Bundle) {
        val positionArray = cancelList.map { it.first }.toIntArray()
        val itemArray = cancelList.map { it.second.toJson() }.toTypedArray()

        bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray)
        bundle.putStringArray(Snackbar.Intent.ITEMS, itemArray)

        cancelList.clear()
    }

    override fun onUpdateData() {
        getIdling().start(IdlingTag.Rank.LOAD_DATA)

        fun updateList() = callback?.apply {
            notifyList(itemList)
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

                runBack { itemList.clearAdd(getList()) }
            }

            updateList()

            getIdling().stop(IdlingTag.Rank.LOAD_DATA)
        }
    }

    override fun onUpdateToolbar() {
        val enterName = callback?.getEnterText() ?: return
        val clearName = enterName.clearSpace().uppercase()

        callback?.onBindingToolbar(
                isClearEnable = enterName.isNotEmpty(),
                isAddEnable = clearName.isNotEmpty() && !nameList.contains(clearName)
        )
    }

    override fun onShowRenameDialog(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        callback?.dismissSnackbar()
        callback?.showRenameDialog(p, item.name, nameList)
    }

    override fun onResultRenameDialog(p: Int, name: String) {
        val item = itemList.getOrNull(p)?.apply { this.name = name } ?: return

        viewModelScope.launchBack { updateRank(item) }

        onUpdateToolbar()
        callback?.notifyList(itemList)
    }


    override fun onClickEnterCancel() {
        callback?.clearEnter()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.uppercase()

        if (name.isNullOrEmpty() || nameList.contains(name)) return false

        onClickEnterAdd(addToBottom = true)

        return true
    }

    override fun onClickEnterAdd(addToBottom: Boolean) {
        val name = callback?.clearEnter()?.clearSpace()

        if (name.isNullOrEmpty() || nameList.contains(name.uppercase())) return

        callback?.hideKeyboard()
        callback?.dismissSnackbar()

        viewModelScope.launch {
            val item = runBack { insertRank(name) } ?: return@launch
            val p = if (addToBottom) itemList.size else 0

            itemList.add(p, item)

            runBack { interactor.updatePositions(itemList, correctPositions(itemList)) }

            callback?.scrollToItem(itemList, p, addToBottom)
        }
    }

    override fun onClickVisible(p: Int) {
        val item = itemList.getOrNull(p)?.switchVisible() ?: return

        callback?.setList(itemList)

        viewModelScope.launch {
            runBack { updateRank(item) }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return
        val noteIdList = correctPositions(itemList)

        /**
         * Save item for snackbar undo action.
         */
        cancelList.add(Pair(p, item))

        callback?.notifyList(itemList)
        callback?.showSnackbar()

        viewModelScope.launch {
            launchBack {
                deleteRank(item)
                interactor.updatePositions(itemList, noteIdList)
            }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    override fun onItemAnimationFinished() {
        callback?.onBindingList()

        /**
         * Need prevent clear openState if item is currently dragging.
         */
        if (!inTouchAction) {
            callback?.openState?.clear()
        }
    }


    override fun onSnackbarAction() {
        if (cancelList.isEmpty()) return

        val pair = cancelList.validRemoveAt(index = cancelList.lastIndex) ?: return
        val item = pair.second

        /**
         * Check item position correct, just in case.
         * List size after adding item, will be last index.
         */
        val isCorrect = pair.first in itemList.indices
        val position = if (isCorrect) pair.first else itemList.size
        itemList.add(position, item)

        callback?.apply {
            notifyItemInsertedScroll(itemList, position)

            /**
             * If list was empty need hide information and show list.
             */
            if (itemList.size == 1) {
                onBindingList()
            }

            /**
             * Show snackbar for next item undo remove.
             */
            if (cancelList.isNotEmpty()) {
                showSnackbar()
            }
        }

        /**
         * After insert don't need update item in list (due to item already have id).
         */
        viewModelScope.launch {
            runBack {
                insertRank(item)
                interactor.updatePositions(itemList, correctPositions(itemList))
            }

            callback?.setList(itemList)
            callback?.sendNotifyNotesBroadcast()
        }
    }

    override fun onSnackbarDismiss() = cancelList.clear()


    override fun onReceiveUnbindNote(noteId: Long) {
        viewModelScope.launch {
            for (item in itemList) {
                if (!item.noteId.contains(noteId)) continue

                /**
                 * Decrement [RankItem.bindCount] without using interactor.
                 */
                item.bindCount = max(a = 0, b = item.bindCount - 1)

                /**
                 * Note may have only one category and it mean what we can use break.
                 */
                break
            }

            callback?.notifyList(itemList)
        }
    }


    override fun onTouchAction(inAction: Boolean) {
        inTouchAction = inAction

        if (inAction) {
            callback?.dismissSnackbar()
        }

        callback?.openState?.isBlocked = inAction
    }

    override fun onTouchGetDrag(): Boolean {
        val value = callback?.openState?.isBlocked != true

        if (value) callback?.hideKeyboard()

        return value
    }

    override fun onTouchMove(from: Int, to: Int): Boolean {
        itemList.move(from, to)

        callback?.notifyItemMoved(itemList, from, to)
        callback?.hideKeyboard()

        return true
    }

    override fun onTouchMoveResult() {
        callback?.openState?.clear()

        val noteIdList = correctPositions(itemList)
        callback?.setList(itemList)

        viewModelScope.launchBack { interactor.updatePositions(itemList, noteIdList) }
    }


    // TODO #REFACTOR join with variable
    @RunPrivate
    fun getNameList(list: List<RankItem>): List<String> = list.map { it.name.uppercase() }
}