package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.note.ITextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel

/**
 * ViewModel for [ITextNoteFragment].
 */
class TextNoteViewModel(application: Application) :
    ParentNoteViewModel<NoteItem.Text, ITextNoteFragment, ITextNoteInteractor>(application),
    ITextNoteViewModel {

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun setupBeforeInitialize() {
        val theme = interactor.theme
        callback?.apply {
            setupBinding(theme)
            setupToolbar(theme, color)
            setupEnter(inputControl)
        }
    }

    override suspend fun tryInitializeNote(): Boolean {
        /**
         * If first open
         */
        if (!isNoteInitialized()) {
            val name = parentCallback?.getString(R.string.dialog_item_rank) ?: return false
            rankDialogItemArray = runBack { interactor.getRankDialogItemArray(name) }

            if (id == Default.ID) {
                noteItem = NoteItem.Text.getCreate(interactor.defaultColor)
                cacheData()

                noteState = NoteState(isCreate = true)
            } else {
                runBack { interactor.getItem(id) }?.let {
                    noteItem = it
                    restoreItem = it.deepCopy()
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
        iconState.notAnimate { setupEditMode(noteState.isEdit) }
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
             * Change toolbar icon from arrow to cancel.
             */
            callback?.setToolbarBackIcon(isCancel = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch {
            runBack { interactor.saveNote(noteItem, noteState.isCreate) }
            cacheData()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }
        }

        return true
    }


    override fun setupEditMode(isEdit: Boolean) {
        inputControl.isEnabled = false

        noteState.isEdit = isEdit
        callback?.apply {
            setToolbarBackIcon(
                isCancel = isEdit && !noteState.isCreate,
                needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(noteItem, isEdit)
            onBindingInput(noteItem, inputControl.access)

            if (isEdit) focusOnEdit(noteState.isCreate)
        }

        saveControl.needSave = true
        saveControl.setSaveEvent(isEdit)

        inputControl.isEnabled = true
    }

    //endregion

}