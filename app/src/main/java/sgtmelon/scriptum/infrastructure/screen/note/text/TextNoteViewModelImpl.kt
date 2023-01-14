package sgtmelon.scriptum.infrastructure.screen.note.text

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave

class TextNoteViewModelImpl(
    init: NoteInit,
    history: NoteHistory,
    createNote: CreateTextNoteUseCase,
    getNote: GetTextNoteUseCase,
    cacheNote: CacheTextNoteUseCase,

    // TODO cleanup
    callback: TextNoteFragment,
    parentCallback: NoteConnector?,
    colorConverter: ColorConverter,
    preferencesRepo: PreferencesRepo,
    private val saveNote: SaveNoteUseCase,
    convertNote: ConvertNoteUseCase,
    updateNote: UpdateNoteUseCase,
    deleteNote: DeleteNoteUseCase,
    restoreNote: RestoreNoteUseCase,
    clearNote: ClearNoteUseCase,
    setNotification: SetNotificationUseCase,
    deleteNotification: DeleteNotificationUseCase,
    getNotificationDateList: GetNotificationsDateListUseCase,
    getRankId: GetRankIdUseCase,
    getRankDialogNames: GetRankDialogNamesUseCase,
    saveControl: SaveControl
) : ParentNoteViewModelImpl<NoteItem.Text, TextNoteFragment>(
    init, history, createNote, getNote, cacheNote,

    // TODO cleanup
    callback, parentCallback, colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId, getRankDialogNames,
    saveControl
), TextNoteViewModel {

    override suspend fun setupAfterInitialize() {
//        callback.setupDialog(rankDialogItemArray)

        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEdit.value.isTrue())
        mayAnimateIcon = true

        //        callback.onBindingLoad()
    }

    //region Cleanup

    override fun onRestoreData(): Boolean {
        if (id.value == Default.ID || deprecatedNoteItem.id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = deprecatedNoteItem.color
        val restoreItem = cacheNote.item
        if (restoreItem != null) {
            deprecatedNoteItem = restoreItem.copy()
        }
        //        deprecatedNoteItem = deprecatedRestoreItem.copy()
        val colorTo = deprecatedNoteItem.color

        setupEditMode(isEdit = false)

        callback.tintToolbar(colorFrom, colorTo)
        color.postValue(colorTo)
        history.reset()

        return true
    }

    //region Menu click

    // TODO move undo/redo staff inside use case or something like this
    override fun onMenuUndoRedoSelect(action: HistoryAction, isUndo: Boolean) {
        history.saveChanges = false

        when (action) {
            is HistoryAction.Name -> onMenuUndoRedoName(action, isUndo)
            is HistoryAction.Rank -> onMenuUndoRedoRank(action, isUndo)
            is HistoryAction.Color -> onMenuUndoRedoColor(action, isUndo)
            is HistoryAction.Text.Enter -> onMenuUndoRedoText(action, isUndo)
            else -> Unit
        }

        history.saveChanges = true
    }

    private fun onMenuUndoRedoText(action: HistoryAction.Text.Enter, isUndo: Boolean) {
        callback.changeText(action.value[isUndo], action.cursor[isUndo])
    }

    /**
     * Don't need update [color] because it's happen in [changeColor] function.
     */
    override fun save(changeMode: Boolean): Boolean {
        if (changeMode && callback.isDialogOpen) return false

        if (isEdit.value.isFalse() || !deprecatedNoteItem.isSaveEnabled) return false

        deprecatedNoteItem.onSave()

        if (changeMode) {
//            callback.hideKeyboardDepr()
            setupEditMode(isEdit = false)
            history.reset()
        } else if (noteState.value == NoteState.CREATE) {
            // TODO noteState will be changed later
            //            /** Change toolbar icon from arrow to cancel for auto save case. */
            //            callback.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        viewModelScope.launch {
            val isCreate = noteState.value == NoteState.CREATE
            runBack { saveNote(deprecatedNoteItem, isCreate) }
            cacheNote(deprecatedNoteItem)
            //        cacheData()

            if (isCreate) {
                noteState.postValue(NoteState.EXIST)
                id.postValue(deprecatedNoteItem.id)
            }

            callback.sendNotifyNotesBroadcast()
        }

        return true
    }

    override fun setupEditMode(isEdit: Boolean) {
        history.saveChanges = false

        this.isEdit.postValue(isEdit)

        callback.apply {
            // TODO isEdit value already posted
            val noteState = noteState.value
            //            val notCreate = noteState != NoteState.CREATE
            //            setToolbarBackIcon(
            //                isCancel = notCreate && isEdit,
            //                needAnim = notCreate && mayAnimateIcon
            //            )

            onBindingEdit(deprecatedNoteItem, isEdit)
            historyAvailable.postValue(history.available)

//            if (isEdit) {
//                focusOnEdit(isCreate = noteState == NoteState.CREATE)
//            }
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        history.saveChanges = true
    }

    //endregion

    //endregion

}