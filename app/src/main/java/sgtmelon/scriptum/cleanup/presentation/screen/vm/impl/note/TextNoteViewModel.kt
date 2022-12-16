package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.ITextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.INoteConnector
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [ITextNoteFragment].
 */
class TextNoteViewModel(
    isEdit: Boolean,
    noteState: NoteState,
    id: Long,
    color: Color,

    // TODO cleanup
    callback: ITextNoteFragment,
    parentCallback: INoteConnector?,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
    private val getNote: GetTextNoteUseCase,
    private val saveNote: SaveNoteUseCase,
    convertNote: ConvertNoteUseCase,
    updateNote: UpdateNoteUseCase,
    deleteNote: DeleteNoteUseCase,
    restoreNote: RestoreNoteUseCase,
    clearNote: ClearNoteUseCase,
    setNotification: SetNotificationUseCase,
    deleteNotification: DeleteNotificationUseCase,
    getNotificationDateList: GetNotificationDateListUseCase,
    getRankId: GetRankIdUseCase,
    private val getRankDialogNames: GetRankDialogNamesUseCase
) : ParentNoteViewModel<NoteItem.Text, ITextNoteFragment>(
    isEdit, noteState, id, color,

    // TODO cleanup
    callback, parentCallback, colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId
), ITextNoteViewModel {

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun setupBeforeInitialize() {
        callback?.setupBinding()
        callback?.setupToolbar(deprecatedColor)
        callback?.setupEnter(inputControl)
    }

    override suspend fun tryInitializeNote(): Boolean {
        /**
         * If first open
         */
        if (!isNoteInitialized()) {
            rankDialogItemArray = runBack { getRankDialogNames() }

            if (deprecatedId == Default.ID) {
                noteItem = NoteItem.Text.getCreate(preferencesRepo.defaultColor)
                cacheData()

                // TODO remove
                //                deprecatedNoteState = DeprecatedNoteState(isCreate = true)
            } else {
                runBack { getNote(deprecatedId) }?.let {
                    noteItem = it
                    restoreItem = it.deepCopy()

                    callback?.sendNotifyNotesBroadcast()
                } ?: run {
                    callback?.finish()
                    return false
                }

                // TODO remove
                //                deprecatedNoteState = DeprecatedNoteState(isBin = noteItem.isBin)
            }
        }

        return true
    }

    override suspend fun setupAfterInitialize() {
        callback?.setupDialog(rankDialogItemArray)

        mayAnimateIcon = false
        setupEditMode(isEdit.value.isTrue())
        // TODO may this is not needed?
        //        setupEditMode(deprecatedNoteState.isEdit)
        mayAnimateIcon = true

        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    }

    override fun onRestoreData(): Boolean {
        if (deprecatedId == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color
        noteItem = restoreItem.deepCopy()
        val colorTo = noteItem.color

        setupEditMode(isEdit = false)

        callback?.tintToolbar(colorFrom, colorTo)
        parentCallback?.updateNoteColor(colorTo)
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

        if (isEdit.value.isFalse() || !noteItem.isSaveEnabled()) return false
        // TODO remove
        //        if (!deprecatedNoteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.value == NoteState.CREATE) {
            // TODO remove
            //        } else if (deprecatedNoteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel for auto save case.
             */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        parentCallback?.updateNoteColor(noteItem.color)

        viewModelScope.launch { saveBackgroundWork() }

        return true
    }

    override suspend fun saveBackgroundWork() {
        val isCreate = noteState.value == NoteState.CREATE
        runBack { saveNote(noteItem, isCreate) }
        // TODO remove
        //        runBack { saveNote(noteItem, deprecatedNoteState.isCreate) }
        cacheData()

        if (isCreate) {
            noteState.postValue(NoteState.EXIST)
            // TODO remove
            //        if (deprecatedNoteState.isCreate) {
            //            deprecatedNoteState.isCreate = DeprecatedNoteState.ND_CREATE

            deprecatedId = noteItem.id
            parentCallback?.updateNoteId(deprecatedId)
        }

        callback?.sendNotifyNotesBroadcast()
    }

    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        this.isEdit.postValue(isEdit)
        // TODO remove
        //        deprecatedNoteState.isEdit = isEdit

        callback?.apply {
            val noteState = noteState.value
            val notCreate = noteState != NoteState.CREATE
            setToolbarBackIcon(
                isCancel = notCreate && isEdit,
                needAnim = notCreate && mayAnimateIcon
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)

            if (isEdit) focusOnEdit(isCreate = noteState == NoteState.CREATE)
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

}