package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel
import java.util.*
import kotlin.collections.ArrayList
import sgtmelon.scriptum.model.annotation.Options.Notes as Options

/**
 * ViewModel for [NotesFragment]
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
        INotesViewModel {

    private val iInteractor: INotesInteractor by lazy { NotesInteractor(context, callback) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
            setupDialog()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    override fun onUpdateData() {
        viewModelScope.launch {
            itemList.clearAndAdd(iInteractor.getList())

            callback?.apply {
                notifyDataSetChanged(itemList)
                setupBinding(iInteractor.isListHide())
                onBingingList()
            }
        }
    }


    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(itemList[p])
    }

    override fun onShowOptionsDialog(p: Int) {
        val noteItem = itemList[p]

        val itemArray: Array<String> = context.resources.getStringArray(when (noteItem.type) {
            NoteType.TEXT -> R.array.dialog_menu_text
            NoteType.ROLL -> R.array.dialog_menu_roll
        })

        itemArray[Options.NOTIFICATION] = if (noteItem.haveAlarm()) {
            context.getString(R.string.dialog_menu_notification_update)
        } else {
            context.getString(R.string.dialog_menu_notification_set)
        }

        itemArray[Options.BIND] = if (noteItem.isStatus) {
            context.getString(R.string.dialog_menu_status_unbind)
        } else {
            context.getString(R.string.dialog_menu_status_bind)
        }

        callback?.showOptionsDialog(itemArray, p)
    }


    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.NOTIFICATION -> itemList[p].let {
                callback?.showDateDialog(it.alarmDate.getCalendar(), it.haveAlarm(), p)
            }
            Options.BIND -> callback?.notifyItemChanged(p, onMenuBind(p))
            Options.CONVERT -> viewModelScope.launch {
                callback?.notifyItemChanged(p, onMenuConvert(p))
            }
            Options.COPY -> viewModelScope.launch { iInteractor.copy(itemList[p]) }
            Options.DELETE -> callback?.notifyItemRemoved(p, onMenuDelete(p))
        }
    }

    private fun onMenuBind(p: Int) = itemList.apply {
        val noteItem = get(p).apply { isStatus = !isStatus }

        viewModelScope.launch { iInteractor.updateNote(noteItem) }
    }

    private suspend fun onMenuConvert(p: Int) = itemList.apply { iInteractor.convert(get(p)) }

    private fun onMenuDelete(p: Int) = itemList.apply {
        val item = removeAt(p)

        viewModelScope.launch {
            iInteractor.deleteNote(item)
            iBindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultDateDialog(calendar: Calendar, p: Int) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList(), p) }
    }

    override fun onResultDateDialogClear(p: Int) {
        val noteItem = itemList[p]

        viewModelScope.launch {
            iInteractor.clearDate(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.notifyItemChanged(p, itemList)
    }

    override fun onResultTimeDialog(calendar: Calendar, p: Int) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            iInteractor.setDate(itemList[p], calendar)
            callback?.notifyItemChanged(p, itemList)

            iBindInteractor.notifyInfoBind(callback)
        }
    }


    /**
     * Calls on cancel note bind from status bar for update bind indicator
     */
    override fun onCancelNoteBind(id: Long) {
        val p = itemList.indexOfFirst { it.id == id }
        val noteItem = itemList.getOrNull(p) ?: return

        noteItem.isStatus = false
        callback?.notifyItemChanged(p, itemList)
    }

    /**
     * Calls after postpone for update alarm indicator.
     */
    override fun onUpdateAlarm(id: Long) {
        val p = itemList.indexOfFirst { it.id == id }
        val noteItem = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            val notificationItem = iInteractor.getNotification(noteItem.id) ?: return@launch

            noteItem.apply {
                alarmId = notificationItem.alarm.id
                alarmDate = notificationItem.alarm.date
            }

            callback?.notifyItemChanged(p, itemList)
        }
    }

}