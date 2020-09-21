package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IRankFragment].
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private lateinit var interactor: IRankInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: IRankInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


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
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    override fun onUpdateData() {
        callback?.beforeLoad()

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
                    callback?.showProgress()
                }

                runBack { itemList.clearAdd(interactor.getList()) }
            }

            updateList()
        }
    }

    override fun onUpdateToolbar() {
        val enterName = callback?.getEnterText() ?: return
        val clearName = enterName.clearSpace().toUpperCase()

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

        viewModelScope.launchBack { interactor.update(item) }

        onUpdateToolbar()
        callback?.notifyItemChanged(itemList, p)
    }


    override fun onClickEnterCancel() {
        callback?.clearEnter()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.toUpperCase()

        if (name.isNullOrEmpty() || nameList.contains(name)) return false

        onClickEnterAdd(simpleClick = true)

        return true
    }

    override fun onClickEnterAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace()

        if (name.isNullOrEmpty() || nameList.contains(name.toUpperCase())) return

        callback?.dismissSnackbar()

        viewModelScope.launch {
            val item = runBack { interactor.insert(name) } ?: return@launch
            val p = if (simpleClick) itemList.size else 0

            itemList.add(p, item)

            runBack { interactor.updatePosition(itemList, correctPositions(itemList)) }

            callback?.scrollToItem(itemList, p, simpleClick)
        }
    }

    override fun onClickVisible(p: Int) {
        val item = itemList.getOrNull(p)?.switchVisible() ?: return

        callback?.setList(itemList)

        viewModelScope.launchBack {
            interactor.update(item)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onLongClickVisible(p: Int) {
        if (p !in itemList.indices) return

        val animationArray = switchVisible(itemList, p)

        callback?.notifyDataSetChanged(itemList, animationArray)

        viewModelScope.launchBack {
            interactor.update(itemList)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return
        val noteIdList = correctPositions(itemList)

        /**
         * Save item for snackbar undo action.
         */
        cancelList.add(Pair(p, item))

        callback?.notifyItemRemoved(itemList, p)
        callback?.showSnackbar(interactor.theme)

        viewModelScope.launchBack {
            interactor.delete(item)
            interactor.updatePosition(itemList, noteIdList)
            bindInteractor.notifyNoteBind(callback)
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

        val pair = cancelList.removeAtOrNull(index = cancelList.lastIndex) ?: return
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
             * Show snackbar for next item undo.
             */
            if (cancelList.isNotEmpty()) {
                showSnackbar(interactor.theme)
            }
        }

        /**
         * After insert don't need update item in list (due to item already have id).
         */
        viewModelScope.launch {
            runBack {
                interactor.insert(item)
                interactor.updatePosition(itemList, correctPositions(itemList))
            }

            callback?.setList(itemList)
        }
    }

    override fun onSnackbarDismiss() = cancelList.clear()


    override fun onReceiveUnbindNote(id: Long) {
        viewModelScope.launch {
            for (item in itemList) {
                if (!item.noteId.contains(id)) continue

                item.hasBind = runBack { interactor.getBind(item.noteId) }
            }

            callback?.notifyList(itemList)
        }
    }


    override fun onTouchAction(inAction: Boolean) {
        inTouchAction = inAction

        if (inAction) {
            callback?.dismissSnackbar()
        }

        callback?.openState?.value = inAction
    }

    override fun onTouchGetDrag(): Boolean {
        val value = callback?.openState?.value != true

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

        viewModelScope.launchBack { interactor.updatePosition(itemList, noteIdList) }
    }


    @RunPrivate
    fun getNameList(list: List<RankItem>): List<String> = list.map { it.name.toUpperCase() }

    /**
     * Switch visible for all list. Make visible only item with position equal [p].
     * Other items make invisible.
     *
     * [p] - position of long click.
     *
     * Return array with information about item icon animation (need start or not).
     */
    @RunPrivate
    fun switchVisible(list: List<RankItem>, p: Int): BooleanArray {
        val animationArray = BooleanArray(list.size)

        for ((i , item) in list.withIndex()) {
            if (i == p) {
                if (!item.isVisible) {
                    item.isVisible = true
                    animationArray[i] = true
                }
            } else {
                if (item.isVisible) {
                    item.isVisible = false
                    animationArray[i] = true
                }
            }
        }

        return animationArray
    }

    /**
     * Return list of [NoteItem.id] which need update.
     */
    @RunPrivate
    fun correctPositions(list: List<RankItem>): List<Long> {
        val noteIdSet = mutableSetOf<Long>()

        for ((i , item) in list.withIndex()) {
            /**
             * If [RankItem.position] incorrect (out of order) when update it.
             */
            if (item.position != i) {
                item.position = i

                /**
                 * Add id to [Set] of [NoteItem.id] where need update [NoteItem.rankPs].
                 */
                for (it in item.noteId) {
                    noteIdSet.add(it)
                }
            }
        }

        return noteIdSet.toList()
    }

}