package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.data.NoteData.Default
import sgtmelon.scriptum.model.data.NoteData.Intent
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.converter.model.StringConverter
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import java.util.*

/**
 * ViewModel for [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : ParentViewModel<IRollNoteFragment>(application),
        IRollNoteViewModel {

    var parentCallback: INoteChild? = null

    private val iInteractor: IRollNoteInteractor by lazy { RollNoteInteractor(context, callback) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val saveControl by lazy { SaveControl(context, iInteractor.getSaveModel(), callback = this) }
    private val inputControl = InputControl()

    private var id: Long = Default.ID
    private var color: Int = Default.COLOR

    private lateinit var noteItem: NoteItem
    private lateinit var restoreItem: NoteItem

    private var noteState = NoteState()

    /**
     * App doesn't have ranks if size == 1.
     */
    private var rankDialogItemArray: Array<String> = arrayOf()

    private val iconState = IconState()

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID
        color = bundle?.getInt(Intent.COLOR, Default.COLOR) ?: Default.COLOR

        if (color == Default.COLOR) {
            color = iInteractor.defaultColor
        }

        callback?.apply {
            setupBinding(iInteractor.theme)
            setupToolbar(iInteractor.theme, color)
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        viewModelScope.launch {
            /**
             * If first open.
             */
            if (!::noteItem.isInitialized) {
                rankDialogItemArray = iInteractor.getRankDialogItemArray()

                if (id == Default.ID) {
                    noteItem = NoteItem.getCreate(iInteractor.defaultColor, NoteType.ROLL)
                    restoreItem = noteItem.deepCopy()

                    noteState = NoteState(isCreate = true)
                } else {
                    iInteractor.getItem(id)?.let {
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

            callback?.notifyDataSetChanged(noteItem.rollList)
            callback?.onBindingLoad(rankEmpty = rankDialogItemArray.size == 1)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        iInteractor.onDestroy()
        parentCallback = null
        saveControl.setSaveHandlerEvent(isStart = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(Intent.ID, id)
            putInt(Intent.COLOR, color)
        }
    }

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveHandlerEvent(isStart = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave(noteState.isEdit)
            saveControl.setSaveHandlerEvent(isStart = false)
        }
    }


    override fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != Default.ID) {
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

        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    private fun onRestoreData(): Boolean {
        if (id == Default.ID) return false

        /**
         * Get color before restore data.
         */
        val colorFrom = noteItem.color
        noteItem = restoreItem.deepCopy()

        callback?.notifyDataSetChanged(noteItem.rollList)
        setupEditMode(isEdit = false)
        callback?.tintToolbar(colorFrom, noteItem.color)

        parentCallback?.onUpdateNoteColor(noteItem.color)

        inputControl.reset()

        return true
    }


    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

        if (enterText.isEmpty()) {
            onMenuSave(changeMode = true)
        } else {
            onClickAdd(simpleClick = true)
        }

        return true
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onClickAdd(simpleClick: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val enterText = callback?.getEnterText()?.clearSpace() ?: ""

        if (enterText.isEmpty()) return

        callback?.clearEnterText()

        val p = if (simpleClick) noteItem.rollList.size else 0
        val rollItem = RollItem(position = p, text = enterText)

        inputControl.onRollAdd(p, rollItem.toJson())
        noteItem.rollList.add(p, rollItem)

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            scrollToItem(simpleClick, p, noteItem.rollList)
        }
    }

    override fun onClickItemCheck(p: Int) {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        /**
         * Change item check and update [restoreItem].
         */
        noteItem.onItemCheck(p)
        restoreItem = noteItem.deepCopy()

        callback?.notifyItemChanged(noteItem.rollList, p, cursor = null)

        viewModelScope.launch { iInteractor.updateRollCheck(noteItem, p) }
    }

    override fun onLongClickItemCheck() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        /**
         * Change items check and update [restoreItem].
         */
        val check = noteItem.onItemLongCheck()
        restoreItem = noteItem.deepCopy()

        callback?.apply {
            changeCheckToggle(state = true)
            notifyDataSetChanged(noteItem.rollList)
            changeCheckToggle(state = false)
        }

        viewModelScope.launch { iInteractor.updateRollCheck(noteItem, check) }
    }

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
            val rankId = iInteractor.getRankId(check)

            inputControl.onRankChange(noteItem.rankId, noteItem.rankPs, rankId, check)

            noteItem.apply {
                this.rankId = rankId
                this.rankPs = check
            }

            callback?.apply {
                onBindingInput(noteItem, inputControl.access)
                onBingingNote(noteItem)
            }
        }
    }

    override fun onResultDateDialog(calendar: Calendar) {
        viewModelScope.launch { callback?.showTimeDialog(calendar, iInteractor.getDateList()) }
    }

    override fun onResultDateDialogClear() {
        viewModelScope.launch {
            iInteractor.clearDate(noteItem)
            iBindInteractor.notifyInfoBind(callback)
        }

        noteItem.clearAlarm()

        callback?.onBingingNote(noteItem)
    }

    override fun onResultTimeDialog(calendar: Calendar) {
        if (calendar.beforeNow()) return

        viewModelScope.launch {
            iInteractor.setDate(noteItem, calendar)
            callback?.onBingingNote(noteItem)

            iBindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onResultConvertDialog() {
        viewModelScope.launch {
            iInteractor.convert(noteItem)
            parentCallback?.onConvertNote()
        }
    }

    //endregion

    /**
     * Calls on cancel note bind from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        callback?.onBingingNote(noteItem.apply { isStatus = false })
    }

    //region Menu click

    override fun onMenuRestore() {
        viewModelScope.launch {
            iInteractor.restoreNote(noteItem)
            parentCallback?.finish()
        }
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteItem.restore()

        iconState.notAnimate { setupEditMode(isEdit = false) }

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = false) }
    }

    override fun onMenuClear() {
        viewModelScope.launch {
            iInteractor.clearNote(noteItem)
            parentCallback?.finish()
        }
    }


    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        if (callback?.isDialogOpen == true || !noteState.isEdit) return

        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val rollList = noteItem.rollList

            when (item.tag) {
                InputAction.RANK -> {
                    val list = StringConverter().toList(item[isUndo])
                    noteItem.rankId = list[0]
                    noteItem.rankPs = list[1].toInt()
                }
                InputAction.COLOR -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback?.tintToolbar(colorFrom, colorTo)
                }
                InputAction.NAME -> callback?.changeName(item[isUndo], cursor = item.cursor[isUndo])
                InputAction.ROLL -> {
                    rollList[item.p].text = item[isUndo]
                    callback?.notifyItemChanged(rollList, item.p, item.cursor[isUndo])
                }
                InputAction.ROLL_ADD, InputAction.ROLL_REMOVE -> {
                    val isAddUndo = isUndo && item.tag == InputAction.ROLL_ADD
                    val isRemoveRedo = !isUndo && item.tag == InputAction.ROLL_REMOVE

                    if (isAddUndo || isRemoveRedo) {
                        rollList.removeAt(item.p)
                        callback?.notifyItemRemoved(rollList, item.p)
                    } else {
                        val rollItem = RollItem[item[isUndo]]
                        if (rollItem != null) {
                            rollList.add(item.p, rollItem)
                            callback?.notifyItemInserted(rollList, item.p, rollItem.text.length)
                        }
                    }
                }
                InputAction.ROLL_MOVE -> {
                    val from = item[!isUndo].toInt()
                    val to = item[isUndo].toInt()

                    rollList.move(from, to)
                    callback?.notifyItemMoved(rollList, from, to)
                }
            }
        }

        callback?.onBindingInput(noteItem, inputControl.access)
    }

    override fun onMenuRank() {
        if (!noteState.isEdit) return

        callback?.showRankDialog(check = noteItem.rankPs + 1)
    }

    override fun onMenuColor() {
        if (!noteState.isEdit) return

        callback?.showColorDialog(noteItem.color, iInteractor.theme)
    }

    override fun onMenuSave(changeMode: Boolean): Boolean {
        if (changeMode && callback?.isDialogOpen == true) return false

        if (!noteState.isEdit || !noteItem.isSaveEnabled()) return false

        noteItem.onSave()

        /**
         * Need update adapter after remove rows with empty text.
         */
        callback?.setList(noteItem.rollList)

        if (changeMode) {
            callback?.hideKeyboard()
            setupEditMode(isEdit = false)
            inputControl.reset()
        } else if (noteState.isCreate) {
            /**
             * Change toolbar icon from arrow to cancel.
             */
            callback?.changeToolbarIcon(drawableOn = true, needAnim = true)
        }

        parentCallback?.onUpdateNoteColor(noteItem.color)

        viewModelScope.launch {
            iInteractor.saveNote(noteItem, noteState.isCreate)
            restoreItem = noteItem.deepCopy()

            if (noteState.isCreate) {
                noteState.isCreate = NoteState.ND_CREATE

                id = noteItem.id
                parentCallback?.onUpdateNoteId(id)
            }

            callback?.setList(noteItem.rollList)
        }

        return true
    }


    override fun onMenuNotification() {
        if (noteState.isEdit) return

        callback?.showDateDialog(noteItem.alarmDate.getCalendar(), noteItem.haveAlarm())
    }

    override fun onMenuBind() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        /**
         * Change bind and update [restoreItem].
         */
        noteItem.apply { isStatus = !isStatus }
        restoreItem = noteItem.deepCopy()

        callback?.onBindingEdit(noteState.isEdit, noteItem)

        viewModelScope.launch { iInteractor.updateNote(noteItem, updateBind = true) }
    }

    override fun onMenuConvert() {
        if (noteState.isEdit) return

        callback?.showConvertDialog()
    }

    override fun onMenuDelete() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        viewModelScope.launch {
            iInteractor.deleteNote(noteItem)
            parentCallback?.finish()

            iBindInteractor.notifyInfoBind(callback)
        }
    }

    override fun onMenuEdit() {
        if (callback?.isDialogOpen == true || noteState.isEdit) return

        setupEditMode(isEdit = true)
    }

    private fun setupEditMode(isEdit: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = isEdit

        callback?.apply {
            changeToolbarIcon(
                    drawableOn = isEdit && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            onBindingEdit(isEdit, noteItem)
            onBindingInput(noteItem, inputControl.access)
            updateNoteState(noteState)

            if (isEdit) focusOnEdit(noteState.isCreate)
        }

        saveControl.setSaveHandlerEvent(isEdit)
    }

    //endregion

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onInputTextChange() {
        callback?.onBindingInput(noteItem, inputControl.access)
    }

    override fun onInputRollChange(p: Int, text: String) {
        callback?.apply {
            val list = noteItem.rollList.apply {
                get(p).text = text
            }

            setList(list)
            onBindingInput(noteItem, inputControl.access)
        }
    }

    override fun onRollActionNext() {
        callback?.onFocusEnter()
    }

    //region Touch callbacks

    override fun onTouchGetFlags(drag: Boolean) = ItemTouchHelper.Callback.makeMovementFlags(
            if (noteState.isEdit && drag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            if (noteState.isEdit) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
    )

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwipe(p: Int) {
        val rollItem = noteItem.rollList.removeAt(p)

        inputControl.onRollRemove(p, rollItem.toJson())

        callback?.apply {
            onBindingInput(noteItem, inputControl.access)
            notifyItemRemoved(noteItem.rollList, p)
        }
    }

    /**
     * All item positions updates after call [onMenuSave], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        callback?.notifyItemMoved(noteItem.rollList.apply { move(from, to) }, from, to)
        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        inputControl.onRollMove(from, to)
        callback?.onBindingInput(noteItem, inputControl.access)
    }

    //endregion

    companion object {
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onSave() {
            rollList.apply {
                removeAll { it.text.clearSpace().isEmpty() }
                forEachIndexed { i, item ->
                    item.position = i
                    item.text = item.text.clearSpace()
                }
            }

            name = name.clearSpace()
            updateTime().updateComplete()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun RollItem.isNeedRemove(): Boolean = text.clearSpace().isEmpty()

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onItemCheck(p: Int): RollItem {
            val rollItem = rollList[p].apply { isCheck = !isCheck }

            updateTime().updateComplete()

            return rollItem
        }

        /**
         * If have some unchecked items - need turn them to true. Otherwise uncheck all items.
         */
        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun NoteItem.onItemLongCheck(): Boolean {
            val check = rollList.any { !it.isCheck }

            updateTime().updateCheck(check)

            return check
        }
    }

}