package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel

/**
 * ViewModel for [RankFragment]
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private val iInteractor: IRankInteractor by lazy { RankInteractor(context) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val itemList: MutableList<RankItem> = ArrayList()
    private val nameList: List<String> get() = itemList.map { it.name.toUpperCase() }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    override fun onUpdateData() {
        viewModelScope.launch {
            val count = iInteractor.getCount()

            if (count == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) callback?.showProgress()
                itemList.clearAndAdd(iInteractor.getList())
            }

            callback?.apply {
                notifyList(itemList)
                onBindingList()
            }
        }
    }

    override fun onUpdateToolbar() {
        callback?.apply {
            val enterName = getEnterText()
            val clearName = enterName.clearSpace().toUpperCase()

            onBindingToolbar(
                    isClearEnable = enterName.isNotEmpty(),
                    isAddEnable = clearName.isNotEmpty() && !nameList.contains(clearName)
            )
        }
    }

    override fun onShowRenameDialog(p: Int) {
        callback?.showRenameDialog(p, itemList[p].name, nameList)
    }

    override fun onRenameDialog(p: Int, name: String) {
        val item = itemList[p].apply { this.name = name }

        viewModelScope.launch { iInteractor.update(item) }

        onUpdateToolbar()
        callback?.notifyList(itemList)
    }


    override fun onClickEnterCancel() = callback?.clearEnter() ?: ""

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.toUpperCase() ?: ""

        if (name.isEmpty()) return false

        if (!nameList.contains(name)) {
            onClickEnterAdd(simpleClick = true)
        }

        return true
    }

    override fun onClickEnterAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace() ?: ""

        if (name.isEmpty()) return

        val p = if (simpleClick) itemList.size else 0

        viewModelScope.launch {
            itemList.add(p, iInteractor.insert(name))

            val noteIdList = itemList.correctPositions()
            iInteractor.updatePosition(itemList, noteIdList)

            callback?.scrollToItem(simpleClick, p, itemList)
        }
    }

    override fun onClickVisible(p: Int) {
        val item = itemList[p].apply { isVisible = !isVisible }

        callback?.setList(itemList)

        viewModelScope.launch {
            iInteractor.update(item)
            iBindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onLongClickVisible(p: Int) {
        val startAnim = BooleanArray(itemList.size)

        itemList.forEachIndexed { i, item ->
            if (i == p) {
                if (!item.isVisible) {
                    item.isVisible = true
                    startAnim[i] = true
                }
            } else {
                if (item.isVisible) {
                    item.isVisible = false
                    startAnim[i] = true
                }
            }
        }

        callback?.notifyList(itemList, startAnim)

        viewModelScope.launch {
            iInteractor.update(itemList)
            iBindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onClickCancel(p: Int) {
        val item = itemList.removeAt(p)
        val noteIdList = itemList.correctPositions()

        callback?.notifyList(itemList)

        viewModelScope.launch {
            iInteractor.delete(item)
            iInteractor.updatePosition(itemList, noteIdList)
            iBindInteractor.notifyNoteBind(callback)
        }
    }


    /**
     * Calls when user hold rank card and move it between another cards.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        itemList.move(from, to)

        callback?.notifyList(itemList, from, to)

        return true
    }

    /**
     * Only after user cancel hold need update positions.
     */
    override fun onTouchMoveResult() {
        val noteIdList = itemList.correctPositions()
        callback?.setList(itemList)

        viewModelScope.launch { iInteractor.updatePosition(itemList, noteIdList) }
    }


    companion object {
        /**
         * Return list of [NoteItem.id] which need update
         */
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
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