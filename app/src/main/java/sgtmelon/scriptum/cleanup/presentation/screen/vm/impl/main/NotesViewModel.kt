package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.launch
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.runBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.validRemoveAt
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options.Notes as Options

/**
 * ViewModel for [INotesFragment].
 */
class NotesViewModel(
    callback: INotesFragment,
    private val preferencesRepo: PreferencesRepo,
    private val interactor: INotesInteractor,
    private val getList: GetNoteListUseCase,
    private val sortList: SortNoteListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val getNotificationDateList: GetNotificationDateListUseCase
) : ParentViewModel<INotesFragment>(callback),
    INotesViewModel {

    @RunPrivate val itemList: MutableList<NoteItem> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.setupToolbar()
        callback?.setupRecycler()
        callback?.setupDialog()

        callback?.prepareForLoad()
    }


    override fun onUpdateData() {
        getIdling().start(IdlingTag.Notes.LOAD_DATA)

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
                    callback?.hideEmptyInfo()
                    callback?.showProgress()
                }

                runBack { itemList.clearAdd(getList(isBin = false)) }
            }

            callback?.apply {
                val isListHide = runBack { interactor.isListHide() }

                notifyList(itemList)
                setupBinding(isListHide)
                onBindingList()
            }

            getIdling().stop(IdlingTag.Notes.LOAD_DATA)
        }
    }

    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    override fun onShowOptionsDialog(item: NoteItem, p: Int) {
        val callback = callback ?: return

        val title = item.name.ifEmpty { callback.getString(R.string.hint_text_name) }

        val itemArray: Array<String> = callback.getStringArray(
            when (item) {
                is NoteItem.Text -> R.array.dialog_menu_text
                is NoteItem.Roll -> R.array.dialog_menu_roll
            }
        )

        itemArray[Options.NOTIFICATION] = if (item.haveAlarm()) {
            callback.getString(R.string.dialog_menu_notification_update)
        } else {
            callback.getString(R.string.dialog_menu_notification_set)
        }

        itemArray[Options.BIND] = if (item.isStatus) {
            callback.getString(R.string.dialog_menu_status_unbind)
        } else {
            callback.getString(R.string.dialog_menu_status_bind)
        }

        callback.showOptionsDialog(title, itemArray, p)
    }


    override fun onResultOptionsDialog(p: Int, @Options which: Int) {
        when (which) {
            Options.NOTIFICATION -> onMenuNotification(p)
            Options.BIND -> onMenuBind(p)
            Options.CONVERT -> onMenuConvert(p)
            Options.COPY -> onMenuCopy(p)
            Options.DELETE -> onMenuDelete(p)
        }
    }

    @RunPrivate fun onMenuNotification(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        callback?.showDateDialog(item.alarmDate.toCalendar(), item.haveAlarm(), p)
    }

    @RunPrivate fun onMenuBind(p: Int) {
        val item = itemList.getOrNull(p)?.switchStatus() ?: return

        callback?.notifyList(itemList)

        viewModelScope.launch {
            runBack { updateNote(item) }

            callback?.sendNotifyNotesBroadcast()
        }
    }

    @RunPrivate fun onMenuConvert(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            itemList[p] = runBack { interactor.convertNote(item) }

            val sortList = runBack { sortList(itemList, preferencesRepo.sort) }
            callback?.notifyList(itemList.clearAdd(sortList))

            callback?.sendNotifyNotesBroadcast()
        }
    }

    @RunPrivate fun onMenuCopy(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            val text = runBack { getCopyText(item) }
            callback?.copyClipboard(text)
        }
    }

    @RunPrivate fun onMenuDelete(p: Int) {
        val item = itemList.validRemoveAt(p) ?: return

        callback?.notifyList(itemList)

        viewModelScope.launch {
            runBack { deleteNote(item) }

            callback?.sendCancelAlarmBroadcast(item)
            callback?.sendCancelNoteBroadcast(item)
            callback?.sendNotifyInfoBroadcast()
        }
    }


    override fun onResultDateDialog(calendar: Calendar, p: Int) {
        viewModelScope.launch {
            val dateList = runBack { getNotificationDateList() }
            callback?.showTimeDialog(calendar, dateList, p)
        }
    }

    override fun onResultDateDialogClear(p: Int) {
        val item = itemList.getOrNull(p) ?: return

        item.clearAlarm()

        callback?.notifyList(itemList)

        viewModelScope.launch {
            runBack { deleteNotification(item) }

            callback?.sendCancelAlarmBroadcast(item)
            callback?.sendNotifyInfoBroadcast()
        }
    }

    override fun onResultTimeDialog(calendar: Calendar, p: Int) {
        if (calendar.isBeforeNow()) return

        val item = itemList.getOrNull(p) ?: return

        viewModelScope.launch {
            runBack { setNotification(item, calendar) }
            callback?.notifyList(itemList)

            callback?.sendSetAlarmBroadcast(item.id, calendar)
            callback?.sendNotifyInfoBroadcast()
        }
    }


    override fun onReceiveUnbindNote(noteId: Long) {
        val p = itemList.indexOfFirst { it.id == noteId }
        val noteItem = itemList.getOrNull(p) ?: return

        noteItem.isStatus = false
        callback?.notifyList(itemList)
    }
}