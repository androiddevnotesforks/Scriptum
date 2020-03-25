package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel

/**
 * ViewModel for [RankFragment].
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private lateinit var interactor: IRankInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: IRankInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    @VisibleForTesting
    val itemList: MutableList<RankItem> = ArrayList()

    private val nameList: List<String> get() = itemList.getNameList()

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
        callback?.showRenameDialog(p, item.name, nameList)
    }

    override fun onResultRenameDialog(p: Int, name: String) {
        val item = itemList.getOrNull(p)?.apply { this.name = name } ?: return

        viewModelScope.launch { interactor.update(item) }

        onUpdateToolbar()
        callback?.notifyItemChanged(itemList, p)
    }


    override fun onClickEnterCancel() {
        callback?.clearEnter()
    }

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.toUpperCase()

        if (name.isNullOrEmpty()) return false

        if (!nameList.contains(name)) {
            onClickEnterAdd(simpleClick = true)
            return true
        }

        return false
    }

    override fun onClickEnterAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace()

        if (name.isNullOrEmpty()) return

        val p = if (simpleClick) itemList.size else 0

        viewModelScope.launch {
            itemList.add(p, interactor.insert(name))

            val noteIdList = itemList.correctPositions()
            interactor.updatePosition(itemList, noteIdList)

            callback?.scrollToItem(itemList, p, simpleClick)
        }
    }

    override fun onClickVisible(p: Int) {
        val item = itemList.getOrNull(p)?.switchVisible() ?: return

        callback?.setList(itemList)

        viewModelScope.launch {
            interactor.update(item)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onLongClickVisible(p: Int) {
        if (p !in itemList.indices) return

        val animationArray = itemList.switchVisible(p)

        callback?.notifyDataSetChanged(itemList, animationArray)

        viewModelScope.launch {
            interactor.update(itemList)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return
        val noteIdList = itemList.correctPositions()

        callback?.notifyItemRemoved(itemList, p)

        viewModelScope.launch {
            interactor.delete(item)
            interactor.updatePosition(itemList, noteIdList)
            bindInteractor.notifyNoteBind(callback)
        }
    }


    /**
     * Calls when user start make drag.
     */
    override fun onTouchDrag() = callback?.openState?.value != true

    /**
     * Calls when user hold rank card and move it between another cards.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        itemList.move(from, to)

        callback?.notifyItemMoved(itemList, from, to)

        return true
    }

    /**
     * Only after user cancel hold need update positions.
     */
    override fun onTouchMoveResult() {
        callback?.openState?.clear()

        val noteIdList = itemList.correctPositions()
        callback?.setList(itemList)

        viewModelScope.launch { interactor.updatePosition(itemList, noteIdList) }
    }


    companion object {
        @VisibleForTesting
        fun List<RankItem>.getNameList(): List<String> = map { it.name.toUpperCase() }

        /**
         * Switch visible for all list. Make visible only item with position equal [p].
         * Other items make invisible.
         *
         * [p] - position of long click.
         *
         * Return array with information about item icon animation (need start or not).
         */
        @VisibleForTesting
        fun List<RankItem>.switchVisible(p: Int): BooleanArray {
            val animationArray = BooleanArray(size)

            forEachIndexed { i, item ->
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
        @VisibleForTesting
        fun List<RankItem>.correctPositions(): List<Long> {
            val noteIdSet = mutableSetOf<Long>()

            forEachIndexed { i, item ->
                /**
                 * If [RankItem.position] incorrect (out of order) when update it.
                 */
                if (item.position != i) {
                    item.position = i

                    /**
                     * Add id to [Set] of [NoteItem.id] where need update [NoteItem.rankPs].
                     */
                    item.noteId.forEach { noteIdSet.add(it) }
                }
            }

            return noteIdSet.toList()
        }
    }

}