package sgtmelon.scriptum.infrastructure.screen.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.onBack
import sgtmelon.extensions.runBack
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.removeAtOrNull
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
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

class NotesViewModelImpl(
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
) : ViewModel(),
    NotesViewModel {

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

    override fun updateData() {
        TODO("Not yet implemented")
    }

    override fun getNotificationData(p: Int): Flow<Pair<Calendar, Boolean>> = flow {
        val item = _itemList.getOrNull(p) ?: return@flow
        emit(value = item.alarmDate.toCalendar() to item.haveAlarm())
    }.onBack()

    override fun getExistDateList(): Flow<List<String>> = flow<List<String>> {
        getNotificationDateList()
    }.onBack()

    override fun deleteNotification(p: Int) {
        TODO("Not yet implemented")
    }

    override fun getCopyText(p: Int): Flow<String> = flow {
        val item = _itemList.getOrNull(p) ?: return@flow
        emit(getCopyText(item))
    }.onBack()

    //    override fun onResultDateDialogClear(p: Int) {
    //        val item = itemList.getOrNull(p) ?: return
    //
    //        item.clearAlarm()
    //
    //        callback?.notifyList(itemList)
    //
    //        viewModelScope.launch {
    //            runBack { deleteNotification(item) }
    //
    //            callback?.sendCancelAlarmBroadcast(item)
    //            callback?.sendNotifyInfoBroadcast()
    //        }
    //    }


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

    // TODO
    //    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    //    override fun onShowOptionsDialog(item: NoteItem, p: Int) {
    //        val callback = callback ?: return
    //
    //        val title = item.name.ifEmpty { callback.getString(R.string.empty_note_name) }
    //
    //        val itemArray: Array<String> = callback.getStringArray(
    //            when (item) {
    //                is NoteItem.Text -> R.array.dialog_menu_text
    //                is NoteItem.Roll -> R.array.dialog_menu_roll
    //            }
    //        )
    //
    //        itemArray[Options.NOTIFICATION] = if (item.haveAlarm()) {
    //            callback.getString(R.string.dialog_menu_notification_update)
    //        } else {
    //            callback.getString(R.string.dialog_menu_notification_set)
    //        }
    //
    //        itemArray[Options.BIND] = if (item.isStatus) {
    //            callback.getString(R.string.dialog_menu_status_unbind)
    //        } else {
    //            callback.getString(R.string.dialog_menu_status_bind)
    //        }
    //
    //        callback.showOptionsDialog(title, itemArray, p)
    //    }
    //
    //
    //    override fun onResultOptionsDialog(p: Int, @Options which: Int) {
    //        when (which) {
    //            Options.NOTIFICATION -> onMenuNotification(p)
    //            Options.BIND -> onMenuBind(p)
    //            Options.CONVERT -> onMenuConvert(p)
    //            Options.COPY -> onMenuCopy(p)
    //            Options.DELETE -> onMenuDelete(p)
    //        }
    //    }
    //
    //    @RunPrivate fun onMenuNotification(p: Int) {
    //        val item = itemList.getOrNull(p) ?: return
    //
    //        callback?.showDateDialog(item.alarmDate.toCalendar(), item.haveAlarm(), p)
    //    }

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
        val item = itemList.removeAtOrNull(p) ?: return

        callback?.notifyList(itemList)

        viewModelScope.launch {
            runBack { deleteNote(item) }

            callback?.sendCancelAlarmBroadcast(item)
            callback?.sendCancelNoteBroadcast(item)
            callback?.sendNotifyInfoBroadcast()
        }
    }
    //
    //
    //    override fun onResultDateDialog(calendar: Calendar, p: Int) {
    //        viewModelScope.launch {
    //            val dateList = runBack { getNotificationDateList() }
    //            callback?.showTimeDialog(calendar, dateList, p)
    //        }
    //    }
    //
    //    override fun onResultDateDialogClear(p: Int) {
    //        val item = itemList.getOrNull(p) ?: return
    //
    //        item.clearAlarm()
    //
    //        callback?.notifyList(itemList)
    //
    //        viewModelScope.launch {
    //            runBack { deleteNotification(item) }
    //
    //            callback?.sendCancelAlarmBroadcast(item)
    //            callback?.sendNotifyInfoBroadcast()
    //        }
    //    }

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

    //endregion
}