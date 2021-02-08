package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import java.util.*
import kotlin.collections.ArrayList
import sgtmelon.scriptum.domain.model.annotation.Options.Notes as Options

/**
 * ViewModel for [INotesFragment].
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
    INotesViewModel {

    private lateinit var interactor: INotesInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: INotesInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    @RunPrivate val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupDialog()
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    override fun onUpdateData() {
        callback?.beforeLoad()

        /**
         * If was rotation need show list. After that fetch updates.
         */
        if (itemList.isNotEmpty()) {
            callback?.apply {
                notifyList(itemList)
                onBindingList()
            }
        }

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

            callback?.apply {
                val isListHide = runBack { interactor.isListHide() }

                notifyList(itemList)
                setupBinding(isListHide)
                onBindingList()
            }
        }
    }


    override fun onClickNote(p: Int) {
        callback?.openNoteScreen(item = itemList.getOrNull(p) ?: return)
    }

    override fun onShowOptionsDialog(p: Int) {
        val callback = callback ?: return
        val noteItem = itemList.getOrNull(p) ?: return

        val itemArray: Array<String> = callback.getStringArray(when (noteItem) {
            is NoteItem.Text -> R.array.dialog_menu_text
            is NoteItem.Roll -> R.array.dialog_menu_roll
        })

        itemArray[Options.NOTIFICATION] = if (noteItem.haveAlarm()) {
            callback.getString(R.string.dialog_menu_notification_update)
        } else {
            callback.getString(R.string.dialog_menu_notification_set)
        }

        itemArray[Options.BIND] = if (noteItem.isStatus) {
            callback.getString(R.string.dialog_menu_status_unbind)
        } else {
            callback.getString(R.string.dialog_menu_status_bind)
        }

        callback.showOptionsDialog(itemArray, p)
    }


    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.NOTIFICATION -> onMenuNotification(p)
            Options.BIND -> onMenuBind(p)
            Options.CONVERT -> onMenuConvert(p)
            Options.COPY -> onMenuCopy(p)
            Options.DELETE -> onMenuDelete(p)
        }
    }

    private fun onMenuNotification(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        callback?.showDateDialog(item.alarmDate.getCalendar(), item.haveAlarm(), p)
    }

    private fun onMenuBind(p: Int) {
        val item = itemList.getOrNull(p)?.switchStatus() ?: return

        callback?.notifyItemChanged(itemList, p)

        viewModelScope.launchBack { interactor.updateNote(item) }
    }

    private fun onMenuConvert(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            itemList[p] = runBack { interactor.convertNote(item) }

            val sortList = runBack { sortList(itemList, interactor.sort) }
            callback?.notifyList(itemList.clearAdd(sortList))
        }
    }

    private fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launchBack { interactor.copy(item) }
    }

    private fun onMenuDelete(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        callback?.notifyItemRemoved(itemList, p)

        viewModelScope.launchBack {
            interactor.deleteNote(item)
            bindInteractor.notifyInfoBind(callback)
        }
    }


    override fun onResultDateDialog(calendar: Calendar, p: Int) {
        viewModelScope.launch {
            val dateList = runBack { interactor.getDateList() }
            callback?.showTimeDialog(calendar, dateList, p)
        }
    }

    override fun onResultDateDialogClear(p: Int) {
        val noteItem = itemList.getOrNull(p) ?: return

        noteItem.clearAlarm()

        callback?.notifyItemChanged(itemList, p)

        viewModelScope.launchBack {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultTimeDialog(calendar: Calendar, p: Int) {
        if (calendar.beforeNow()) return

        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            runBack { interactor.setDate(item, calendar) }
            callback?.notifyItemChanged(itemList, p)
            runBack { bindInteractor.notifyInfoBind(callback) }
        }
    }


    override fun onReceiveUnbindNote(id: Long) {
        val p = itemList.indexOfFirst { it.id == id }
        val noteItem = itemList.getOrNull(p) ?: return

        noteItem.isStatus = false
        callback?.notifyItemChanged(itemList, p)
    }

    override fun onReceiveUpdateAlarm(id: Long) {
        val p = itemList.indexOfFirst { it.id == id }
        val noteItem = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            val notificationItem = runBack {
                interactor.getNotification(noteItem.id)
            } ?: return@launch

            noteItem.apply {
                alarmId = notificationItem.alarm.id
                alarmDate = notificationItem.alarm.date
            }

            callback?.notifyItemChanged(itemList, p)
        }
    }

    companion object {
        /**
         * Sort must be like in [INoteDao] queries.
         *
         * [1]  - Move to end of list;
         * [-1] - Move to start of list;
         * [0]  - Not move.
         */
        fun sortList(list: List<NoteItem>, @Sort sort: Int?): List<NoteItem> = when (sort) {
            Sort.CHANGE -> list.sortedByDescending { it.change.getCalendar().timeInMillis }
            Sort.CREATE -> list.sortedByDescending { it.create.getCalendar().timeInMillis }
            Sort.RANK -> list.sortedWith(Comparator<NoteItem> { o1, o2 ->
                return@Comparator when {
                    !o1.haveRank() && o2.haveRank() -> 1
                    o1.haveRank() && !o2.haveRank() -> -1
                    o1.rankPs > o2.rankPs -> 1
                    o1.rankPs < o2.rankPs -> -1
                    else -> 0
                }
            }.thenByDescending {
                it.create.getCalendar().timeInMillis
            })
            Sort.COLOR -> list.sortedWith(compareBy<NoteItem> {
                it.color
            }.thenByDescending {
                it.create.getCalendar().timeInMillis
            })
            else -> list
        }
    }

}