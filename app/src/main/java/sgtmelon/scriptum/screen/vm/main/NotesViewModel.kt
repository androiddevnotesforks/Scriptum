package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.NoteModel
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

    private val itemList: MutableList<NoteModel> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
            setupDialog()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            setupBinding(iInteractor.isListHide())
            bind()
        }
    }


    override fun onClickNote(p: Int) {
        callback?.startNoteActivity(itemList[p].noteEntity)
    }

    override fun onShowOptionsDialog(p: Int) {
        val noteModel = itemList[p]
        val noteEntity = noteModel.noteEntity

        val itemArray: Array<String> = context.resources.getStringArray(when (noteEntity.type) {
            NoteType.TEXT -> R.array.dialog_menu_text
            NoteType.ROLL -> R.array.dialog_menu_roll
        })

        itemArray[Options.NOTIFICATION] = if (noteModel.alarmEntity.date.isEmpty()) {
            context.getString(R.string.dialog_menu_notification_set)
        } else {
            context.getString(R.string.dialog_menu_notification_update)
        }

        itemArray[Options.BIND] = if (noteEntity.isStatus) {
            context.getString(R.string.dialog_menu_status_unbind)
        } else {
            context.getString(R.string.dialog_menu_status_bind)
        }

        callback?.showOptionsDialog(itemArray, p)
    }


    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.NOTIFICATION -> itemList[p].alarmEntity.date.let {
                callback?.showDateDialog(it.getCalendar(), it.isNotEmpty(), p)
            }
            Options.BIND -> callback?.notifyItemChanged(p, onMenuBind(p))
            Options.CONVERT -> callback?.notifyItemChanged(p, onMenuConvert(p))
            Options.COPY -> viewModelScope.launch { iInteractor.copy(itemList[p].noteEntity) }
            Options.DELETE -> callback?.notifyItemRemoved(p, onMenuDelete(p))
        }
    }

    private fun onMenuBind(p: Int) = itemList.apply {
        val noteEntity = get(p).noteEntity.apply { isStatus = !isStatus }

        viewModelScope.launch { iInteractor.updateNote(noteEntity) }
    }

    private fun onMenuConvert(p: Int) = itemList.apply {
        set(p, iInteractor.convert(get(p)))
    }

    private fun onMenuDelete(p: Int) = itemList.apply {
        val item = get(p)

        viewModelScope.launch {
            iInteractor.deleteNote(item)
            iBindInteractor.notifyInfoBind(callback)
        }

        removeAt(p)
    }

    override fun onResultDateDialog(calendar: Calendar, p: Int) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList(), p) }
    }

    override fun onResultDateDialogClear(p: Int) {
        val noteModel = itemList[p]

        viewModelScope.launch {
            iInteractor.clearDate(noteModel)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteModel.alarmEntity.clear()

        callback?.notifyItemChanged(p, itemList)
    }

    override fun onResultTimeDialog(calendar: Calendar, p: Int) {
        if (calendar.beforeNow()) return

        val noteModel = itemList[p]
        noteModel.alarmEntity.date = calendar.getString()

        viewModelScope.launch {
            iInteractor.setDate(noteModel, calendar)
            iBindInteractor.notifyInfoBind(callback)
        }

        callback?.notifyItemChanged(p, itemList)
    }


    /**
     * Calls on cancel note bind from status bar and need update UI
     */
    override fun onCancelNoteBind(id: Long) = itemList.forEachIndexed { i, it ->
        if (it.noteEntity.id == id) {
            it.noteEntity.isStatus = false
            callback?.notifyItemChanged(i, itemList)

            return@forEachIndexed
        }
    }

}