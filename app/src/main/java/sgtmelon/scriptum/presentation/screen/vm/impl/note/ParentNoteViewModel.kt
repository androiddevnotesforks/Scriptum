package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IParentNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IParentNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel
import java.util.*

/**
 * Parent viewModel for [TextNoteViewModel] and [RollNoteViewModel].
 */
abstract class ParentNoteViewModel<N : NoteItem, C : IParentNoteFragment<N>, I : IParentNoteInteractor<N>>(
        application: Application
) : ParentViewModel<C>(application),
        IParentNoteViewModel {

    //region Variables

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var parentCallback: INoteConnector? = null
        private set

    fun setParentCallback(callback: INoteConnector?) {
        parentCallback = callback
    }

    protected lateinit var interactor: I
    protected lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: I, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var inputControl: IInputControl = InputControl()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val saveControl: ISaveControl by lazy {
        SaveControl(context, interactor.getSaveModel(), callback = this)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var id: Long = NoteData.Default.ID

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var color: Int = NoteData.Default.COLOR

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    lateinit var noteItem: N

    /**
     * Item for cash data before enter edit mode (for easy data restore).
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    lateinit var restoreItem: N

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var noteState = NoteState()

    /**
     * App doesn't have ranks if size == 1.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var rankDialogItemArray: Array<String> = arrayOf()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var iconState = IconState()

    //endregion

    protected fun isNoteInitialized(): Boolean = ::noteItem.isInitialized

    /**
     * If not cache data inside [restoreItem] it will cause bug with restore.
     *
     * Use example: restoreItem = noteItem.deepCopy().
     */
    abstract fun cacheData()

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        interactor.onDestroy()
        parentCallback = null
        saveControl.setSaveEvent(isWork = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(NoteData.Intent.ID, id)
            putInt(NoteData.Intent.COLOR, color)
        }
    }

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveEvent(isWork = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave()
            saveControl.setSaveEvent(isWork = false)
        }
    }


    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback?.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback?.finish()
        }
    }

    /**
     * FALSE - will call super.onBackPress()
     */
    override fun onPressBack(): Boolean {
        if (!noteState.isEdit) return false

        /**
         * If note can't be saved and activity will be closed.
         */
        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    /**
     * Function should describe restoring all data (in code and on screen) after changes
     * was canceled.
     */
    abstract fun onRestoreData(): Boolean

    //region Results of dialogs

    override fun onResultColorDialog(check: Int) {
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            tintToolbar(check)
        }
    }

    override fun onResultRankDialog(check: Int) {
        viewModelScope.launch {
            val rankId = interactor.getRankId(check)

            inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

            noteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(noteItem, inputControl.access)
                onBindingNote(noteItem)
            }
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch {
            callback?.showTimeDialog(calendar, interactor.getDateList())
        }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            interactor.clearDate(noteItem)
            bindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()
        cacheData()

        callback?.onBindingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            interactor.setDate(noteItem, calendar)
            cacheData()

            callback?.onBindingNote(noteItem)

            bindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            interactor.convertNote(noteItem)
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        noteItem.isStatus = false
        restoreItem.isStatus = false

        callback?.onBindingNote(noteItem)
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            interactor.restoreNote(noteItem)
            parentCallback?.finish()
        }
    }

    //endregion

    override fun onResultSaveControl() {
        callback?.showSaveToast(onMenuSave(changeMode = false))
    }

    override fun onInputTextChange() {
        callback?.onBindingInput(noteItem, inputControl.access)
    }

}