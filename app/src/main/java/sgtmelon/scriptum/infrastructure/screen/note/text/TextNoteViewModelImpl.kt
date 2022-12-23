package sgtmelon.scriptum.infrastructure.screen.note.text

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
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

/**
 * ViewModel for [TextNoteFragment].
 */
class TextNoteViewModelImpl(
    init: NoteInit,
    createNote: CreateTextNoteUseCase,
    getNote: GetTextNoteUseCase,

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
    getNotificationDateList: GetNotificationDateListUseCase,
    getRankId: GetRankIdUseCase,
    getRankDialogNames: GetRankDialogNamesUseCase
) : ParentNoteViewModelImpl<NoteItem.Text, TextNoteFragment>(
    init, createNote, getNote,

    // TODO cleanup
    callback, parentCallback, colorConverter, preferencesRepo, convertNote,
    updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
    getNotificationDateList, getRankId, getRankDialogNames
), TextNoteViewModel {

    override fun setupBeforeInitialize() {
        //        callback?.setupBinding()
        //        color.value?.let { callback?.setupToolbar(it) }
        callback?.setupEnter(inputControl)
    }

    override suspend fun tryInitializeNote(): Boolean {
        TODO("Remove it")
        //        /** If first open. */
        //        if (!isNoteInitialized()) {
        //            rankDialogItemArray = runBack { getRankDialogNames() }
        //
        //            val id = id.value
        //            if (id == null || id == Default.ID) {
        //                val defaultColor = preferencesRepo.defaultColor
        //                deprecatedNoteItem =
        //                    NoteItem.Text.getCreate(defaultColor) // TODO по идее в color уже ставится дефолтный, если не было что-то передано
        //                cacheData()
        //            } else {
        //                runBack { getNote(id) }?.let {
        //                    deprecatedNoteItem = it
        //                    cacheData()
        //
        //                    callback?.sendNotifyNotesBroadcast()
        //                } ?: run {
        //                    callback?.finish()
        //                    return false
        //                }
        //            }
        //        }
        //
        //        return true
    }

    override suspend fun setupAfterInitialize() {
//        callback?.setupDialog(rankDialogItemArray)

        mayAnimateIcon = false
        // TODO may this is not needed?
        setupEditMode(isEdit.value.isTrue())
        mayAnimateIcon = true

        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    }

    //region Cleanup

    override fun cacheData() {
        deprecatedRestoreItem = deprecatedNoteItem.deepCopy()
    }

    // TODO remove
    //    override fun setupBeforeInitialize() {
    //        callback?.setupBinding()
    //        color.value?.let { callback?.setupToolbar(it) }
    //        callback?.setupEnter(inputControl)
    //    }
    //
    //    override suspend fun tryInitializeNote(): Boolean {
    //        TODO("Remove it")
    //        //        /** If first open. */
    //        //        if (!isNoteInitialized()) {
    //        //            rankDialogItemArray = runBack { getRankDialogNames() }
    //        //
    //        //            val id = id.value
    //        //            if (id == null || id == Default.ID) {
    //        //                val defaultColor = preferencesRepo.defaultColor
    //        //                deprecatedNoteItem =
    //        //                    NoteItem.Text.getCreate(defaultColor) // TODO по идее в color уже ставится дефолтный, если не было что-то передано
    //        //                cacheData()
    //        //            } else {
    //        //                runBack { getNote(id) }?.let {
    //        //                    deprecatedNoteItem = it
    //        //                    cacheData()
    //        //
    //        //                    callback?.sendNotifyNotesBroadcast()
    //        //                } ?: run {
    //        //                    callback?.finish()
    //        //                    return false
    //        //                }
    //        //            }
    //        //        }
    //        //
    //        //        return true
    //    }
    //
    //    override suspend fun setupAfterInitialize() {
    //        callback?.setupDialog(rankDialogItemArray)
    //
    //        mayAnimateIcon = false
    //        // TODO may this is not needed?
    //        setupEditMode(isEdit.value.isTrue())
    //        mayAnimateIcon = true
    //
    //        callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
    //    }

    override fun onRestoreData(): Boolean {
        if (id.value == Default.ID || deprecatedNoteItem.id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = deprecatedNoteItem.color
        deprecatedNoteItem = deprecatedRestoreItem.deepCopy()
        val colorTo = deprecatedNoteItem.color

        setupEditMode(isEdit = false)

        callback?.tintToolbar(colorFrom, colorTo)
        color.postValue(colorTo)
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
            else -> Unit
        }

        inputControl.isEnabled = true
    }

    private fun onMenuUndoRedoText(item: InputItem, isUndo: Boolean) {
        val text = item[isUndo]
        val cursor = item.cursor[isUndo]

        callback?.changeText(text, cursor)
    }

    /**
     * Don't need update [color] because it's happen in [onResultColorDialog] function.
     */
    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (isEdit.value.isFalse() || !deprecatedNoteItem.isSaveEnabled()) return false

        deprecatedNoteItem.onSave()

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.value == NoteState.CREATE) {
            /** Change toolbar icon from arrow to cancel for auto save case. */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        viewModelScope.launch { saveBackgroundWork() }

        return true
    }

    override suspend fun saveBackgroundWork() {
        val isCreate = noteState.value == NoteState.CREATE
        runBack { saveNote(deprecatedNoteItem, isCreate) }
        cacheData()

        if (isCreate) {
            noteState.postValue(NoteState.EXIST)
            id.postValue(deprecatedNoteItem.id)
        }

        callback?.sendNotifyNotesBroadcast()
    }

    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        this.isEdit.postValue(isEdit)

        callback?.apply {
            val noteState = noteState.value
            val notCreate = noteState != NoteState.CREATE
            setToolbarBackIcon(
                isCancel = notCreate && isEdit,
                needAnim = notCreate && mayAnimateIcon
            )

            onBindingEdit(deprecatedNoteItem, isEdit)
            onBindingInput(deprecatedNoteItem, inputControl.access)

            if (isEdit) focusOnEdit(isCreate = noteState == NoteState.CREATE)
        }

        saveControl.isNeedSave = true
        saveControl.changeAutoSaveWork(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

    //endregion

}