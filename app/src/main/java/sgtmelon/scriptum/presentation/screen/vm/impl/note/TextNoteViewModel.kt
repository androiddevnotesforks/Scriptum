package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.data.NoteData.Default
import sgtmelon.scriptum.domain.model.data.NoteData.Intent
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel

/**
 * ViewModel for [TextNoteFragment].
 */
class TextNoteViewModel(application: Application) :
        ParentNoteViewModel<NoteItem.Text, ITextNoteFragment, ITextNoteInteractor>(application),
        ITextNoteViewModel {

    override fun cacheData() {
        restoreItem = noteItem.deepCopy()
    }

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = interactor.defaultColor
        }

        val theme = interactor.theme
        callback?.apply {
            setupBinding(theme)
            setupToolbar(theme, color)
            setupEnter(inputControl)
        }

        viewModelScope.launch {
            /**
             * If first open
             */
            if (!isNoteInitialized()) {
                val name = parentCallback?.getString(R.string.dialog_item_rank) ?: return@launch
                rankDialogItemArray = interactor.getRankDialogItemArray(name)

                if (id == Default.ID) {
                    noteItem = NoteItem.Text.getCreate(interactor.defaultColor)
                    cacheData()

                    noteState = NoteState(isCreate = true)
                } else {
                    interactor.getItem(id)?.let {
                        noteItem = it
                        restoreItem = it.deepCopy()
                    } ?: run {
                        parentCallback?.finish()
                        return@launch
                    }

                    noteState = NoteState(isBin = noteItem.isBin)
                }
            }

            callback?.setupDialog(rankDialogItemArray)

            iconState.notAnimate { setupEditMode(noteState.isEdit) }

            callback?.onBindingLoad(isRankEmpty = rankDialogItemArray.size == 1)
        }
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

    override fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            when (item.tag) {
                InputAction.RANK -> onMenuUndoRedoRank(item, isUndo)
                InputAction.COLOR -> onMenuUndoRedoColor(item, isUndo)
                InputAction.NAME -> onMenuUndoRedoName(item, isUndo)
                InputAction.TEXT -> onMenuUndoRedoText(item, isUndo)
            }
        }

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    private fun onMenuUndoRedoText(item: InputItem, isUndo: Boolean) {
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
            interactor.saveNote(noteItem, noteState.isCreate)
            cacheData()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }
        }

        return true
    }


    override fun setupEditMode(isEdit: Boolean) = inputControl.makeNotEnabled {
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
    }

    //endregion

}