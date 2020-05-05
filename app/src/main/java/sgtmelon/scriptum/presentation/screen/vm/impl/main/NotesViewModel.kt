package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.removeAtOrNull
import sgtmelon.scriptum.extension.sort
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import java.util.*
import kotlin.collections.ArrayList
import sgtmelon.scriptum.domain.model.annotation.Options.Notes as Options

/**
 * ViewModel for [NotesFragment].
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
        INotesViewModel {

    private lateinit var interactor: INotesInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: INotesInteractor, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }


    @VisibleForTesting
    val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(interactor.theme)
            setupDialog()
        }
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
            if (interactor.getCount() == 0) {
                itemList.clear()
            } else {
                if (itemList.isEmpty()) {
                    callback?.showProgress()
                }

                itemList.clearAddAll(interactor.getList())
            }

            callback?.apply {
                notifyList(itemList)
                setupBinding(interactor.isListHide())
                onBindingList()
            }
        }
    }


    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(item = itemList.getOrNull(p) ?: return)
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

        viewModelScope.launch { interactor.updateNote(item) }
    }

    private fun onMenuConvert(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            itemList[p] = interactor.convert(item)

            val sortList = itemList.sort(interactor.sort)
            callback?.notifyList(itemList.clearAddAll(sortList))
        }
    }

    private fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch { interactor.copy(item) }
    }

    private fun onMenuDelete(p: Int) {
        val item = itemList.removeAtOrNull(p) ?: return

        callback?.notifyItemRemoved(itemList, p)

        viewModelScope.launch {
            interactor.deleteNote(item)
            bindInteractor.notifyInfoBind(callback)
        }
    }


    override fun onResultDateDialog(calendar: Calendar, p: Int) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, interactor.getDateList(), p) }
    }

    override fun onResultDateDialogClear(p: Int) {
        val noteItem = itemList.getOrNull(p) ?: return

        noteItem.clearAlarm()

        callback?.notifyItemChanged(itemList, p)

        viewModelScope.launch {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultTimeDialog(calendar: Calendar, p: Int) {
        if (calendar.beforeNow()) return

        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            interactor.setDate(item, calendar)
            callback?.notifyItemChanged(itemList, p)

            bindInteractor.notifyInfoBind(callback)
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
            val notificationItem = interactor.getNotification(noteItem.id) ?: return@launch

            noteItem.apply {
                alarmId = notificationItem.alarm.id
                alarmDate = notificationItem.alarm.date
            }

            callback?.notifyItemChanged(itemList, p)
        }
    }

}