package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.state.NoteState
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.ITextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [ITextNoteFragment].
 */
class TextNoteViewModel(
    callback: ITextNoteFragment,
    parentCallback: INoteConnector?,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
    interactor: ITextNoteInteractor,
    updateNote: UpdateNoteUseCase,
    deleteNote: DeleteNoteUseCase,
    restoreNote: RestoreNoteUseCase,
    clearNote: ClearNoteUseCase,
    setNotification: SetNotificationUseCase,
    deleteNotification: DeleteNotificationUseCase,
    getNotificationDateList: GetNotificationDateListUseCase
) : ParentNoteViewModel<NoteItem.Text, ITextNoteFragment, ITextNoteInteractor>(
    callback, parentCallback, colorConverter, preferencesRepo, interactor,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList
), ITextNoteViewModel {

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun setupBeforeInitialize() {
        callback?.setupBinding()
        callback?.setupToolbar(color)
        callback?.setupEnter(inputControl)
    }

    override suspend fun tryInitializeNote(): Boolean {
        /**
         * If first open
         */
        if (!isNoteInitialized()) {
            val name = parentCallback?.getString(R.string.dialog_item_rank) ?: return false
            rankDialogItemArray = runBack { interactor.getRankDialogItemArray(name) }

            if (id == Default.ID) {
                noteItem = NoteItem.Text.getCreate(preferencesRepo.defaultColor)
                cacheData()

                noteState = NoteState(isCreate = true)
            } else {
                runBack { interactor.getItem(id) }?.let {
                    noteItem = it
                    restoreItem = it.deepCopy()

                    callback?.sendNotifyNotesBroadcast()
                } ?: run {
                    parentCallback?.finish()
                    return false
                }

                noteState = NoteState(isBin = noteItem.isBin)
            }
        }

        return true
    }

    override suspend fun setupAfterInitialize() {
        callback?.setupDialog(rankDialogItemArray)

        mayAnimateIcon = false
        setupEditMode(noteState.isEdit)
        mayAnimateIcon = true

        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    }

    override fun onRestoreData(): Boolean {
        if (id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color
        noteItem = restoreItem.deepCopy()
        val colorTo = noteItem.color

        setupEditMode(isEdit = false)

        callback?.tintToolbar(colorFrom, colorTo)
        parentCallback?.onUpdateNoteColor(colorTo)
        inputControl.reset()

        return true
    }

    //region Menu click

    // TODO move undo/redo staff inside use case or something like this
    override fun onMenuUndoRedoSelect(item: InputItem, isUndo: Boolean) {
        inputControl.isEnabled = false

        when (item.tag) {
            InputAction.RANK -> onMenuUndoRedoRank(item, isUndo)
            InputAction.COLOR -> onMenuUndoRedoColor(item, isUndo)
            InputAction.NAME -> onMenuUndoRedoName(item, isUndo)
            InputAction.TEXT -> onMenuUndoRedoText(item, isUndo)
        }

        inputControl.isEnabled = true
    }

    @RunPrivate fun onMenuUndoRedoText(item: InputItem, isUndo: Boolean) {
        val text = item[isUndo]
        val cursor = item.cursor[isUndo]

        callback?.changeText(text, cursor)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (!noteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel for auto save case.
             */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch { saveBackgroundWork() }

        return true
    }

    override suspend fun saveBackgroundWork() {
        runBack { interactor.saveNote(noteItem, noteState.isCreate) }
        cacheData()

        if (noteState.isCreate) {
            noteState.isCreate = NoteState.ND_CREATE

            id = noteItem.id
            parentCallback?.onUpdateNoteId(id)
        }

        callback?.sendNotifyNotesBroadcast()
    }

    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        noteState.isEdit = isEdit
        callback?.apply {
            setToolbarBackIcon(
                isCancel = isEdit && !noteState.isCreate,
                needAnim = !noteState.isCreate && mayAnimateIcon
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)

            if (isEdit) focusOnEdit(noteState.isCreate)
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

}