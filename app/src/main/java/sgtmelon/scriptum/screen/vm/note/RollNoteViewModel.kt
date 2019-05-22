package sgtmelon.scriptum.screen.vm.note

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.InputAction
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.CheckState
import sgtmelon.scriptum.model.state.IconState
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.office.utils.showToast
import sgtmelon.scriptum.office.utils.swap
import sgtmelon.scriptum.room.converter.StringConverter
import sgtmelon.scriptum.screen.callback.note.NoteChildCallback
import sgtmelon.scriptum.screen.callback.note.roll.RollNoteCallback
import sgtmelon.scriptum.screen.callback.note.roll.RollNoteMenuCallback
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollNoteViewModel(application: Application) : ParentViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        RollWriteHolder.RollChange,
        RollTouchControl.Result,
        RollNoteMenuCallback {

    lateinit var callback: RollNoteCallback
    lateinit var parentCallback: NoteChildCallback

    private val inputControl = InputControl()
    private val saveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteModel: NoteModel

    private lateinit var noteState: NoteState
    private lateinit var rankIdVisibleList: List<Long>
    private var isRankEmpty: Boolean = true

    private val iconState = IconState()
    private val checkState = CheckState()

    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        if (!::noteModel.isInitialized) {
            rankIdVisibleList = iRoomRepo.getRankIdVisibleList()
            isRankEmpty = iRoomRepo.getRankCount()

            if (id == NoteData.Default.ID) {
                noteModel = NoteModel(NoteItem(
                        create = context.getTime(),
                        color = preference.defaultColor,
                        type = NoteType.ROLL
                ), ArrayList())

                noteState = NoteState(isCreate = true)
            } else {
                noteModel = iRoomRepo.getNoteModel(id)

                BindControl(context, noteModel).updateBind(rankIdVisibleList)

                noteState = NoteState(isCreate = false, isBin = noteModel.noteItem.isBin)
            }
        }

        callback.apply {
            setupBinding(isRankEmpty)
            setupToolbar(noteModel.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankNameList())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        iconState.notAnimate { onMenuEdit(noteState.isEdit) }
    }

    fun onSaveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(
            if (onMenuSave(changeMode = false)) R.string.toast_note_save_done else R.string.toast_note_save_error
    )

    override fun onResultInputTextChange() =
            callback.bindInput(inputControl.access, noteModel.isSaveEnabled())


    override fun onResultInputRollChange(p: Int, text: String) {
        callback.notifyListItem(p, noteModel.rollList[p].apply { this.text = text })
        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
    }

    override fun onResultTouchFlags(drag: Boolean) = ItemTouchHelper.Callback.makeMovementFlags(
            if (noteState.isEdit && drag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            if (noteState.isEdit) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
    )

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)
        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
    }

    override fun onResultTouchSwipe(p: Int) {
        val rollItem = noteModel.rollList[p]
        noteModel.rollList.removeAt(p)

        inputControl.onRollRemove(p, rollItem.toString())

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
        callback.notifyItemRemoved(p, noteModel.rollList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        callback.notifyItemMoved(from, to, noteModel.rollList.apply { swap(from, to) })
        return true
    }

    override fun onMenuRestore() {
        noteModel.noteItem.let { viewModelScope.launch { iRoomRepo.restoreNote(it) } }
        parentCallback.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteModel.noteItem.apply {
            change = context.getTime()
            isBin = false
        }

        iconState.notAnimate { onMenuEdit(editMode = false) }

        iRoomRepo.updateNote(noteModel.noteItem)
    }

    override fun onMenuClear() {
        noteModel.noteItem.let { viewModelScope.launch { iRoomRepo.clearNote(it) } }
        parentCallback.finish()
    }

    override fun onMenuUndo() = onMenuUndoRedo(isUndo = true)

    override fun onMenuRedo() = onMenuUndoRedo(isUndo = false)

    private fun onMenuUndoRedo(isUndo: Boolean) {
        val item = if (isUndo) inputControl.undo() else inputControl.redo()

        if (item != null) inputControl.makeNotEnabled {
            val noteItem = noteModel.noteItem
            val rollList = noteModel.rollList

            when (item.tag) {
                InputAction.rank -> noteItem.rankId = StringConverter().toList(item[isUndo])
                InputAction.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = item[isUndo].toInt()

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputAction.name -> callback.changeName(item[isUndo], item.cursor!![isUndo])
                InputAction.roll -> {
                    rollList[item.p].text = item[isUndo]
                    callback.notifyItemChanged(item.p, item.cursor!![isUndo], rollList)
                }
                InputAction.rollAdd, InputAction.rollRemove -> {
                    val isAddUndo = isUndo && item.tag == InputAction.rollAdd
                    val isRemoveRedo = !isUndo && item.tag == InputAction.rollRemove

                    if (isAddUndo || isRemoveRedo) {
                        rollList.removeAt(item.p)
                        callback.notifyItemRemoved(item.p, rollList)
                    } else {
                        val rollItem = RollItem[item[isUndo]]

                        rollList.add(item.p, rollItem)
                        callback.notifyItemInserted(item.p, rollItem.text.length, rollList)
                    }
                }
                InputAction.rollMove -> {
                    val from = item[!isUndo].toInt()
                    val to = item[isUndo].toInt()

                    rollList.swap(from, to)
                    callback.notifyItemMoved(from, to, rollList)
                }
            }
        }

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheckArray(noteModel.noteItem))

    override fun onMenuColor() = callback.showColorDialog(noteModel.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val rollList = noteModel.rollList

        if (!noteModel.isSaveEnabled()) return false

        noteModel.noteItem.apply {
            change = context.getTime()
            setCompleteText(rollList.getCheck(), rollList.size)
        }

        /**
         * Переход в режим просмотра
         */
        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(false)
            inputControl.reset()
        }

        noteModel = iRoomRepo.saveRollNote(noteModel, noteState.isCreate)

        BindControl(context, noteModel).updateBind(rankIdVisibleList)

        noteState.ifCreate {
            id = noteModel.noteItem.id
            parentCallback.onUpdateNoteId(id)

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        callback.notifyList(rollList)

        return true
    }

    override fun onMenuCheck() {
        val size: Int = noteModel.rollList.size
        val isAll = checkState.isAll

        noteModel.updateCheck(!isAll)

        val noteItem = noteModel.noteItem.apply {
            change = context.getTime()
            setCompleteText(if (isAll) 0 else size, size)
        }

        iRoomRepo.updateRollCheck(noteItem, !isAll)
        BindControl(context, noteModel).updateBind(rankIdVisibleList)

        callback.bindNoteItem(noteItem)
        callback.changeCheckToggle(state = true)

        onUpdateData()
    }

    override fun onMenuBind() = with(noteModel) {
        noteItem.isStatus = !noteItem.isStatus

        BindControl(context, noteModel).updateBind()

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNote(noteItem)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteModel.noteItem.let { viewModelScope.launch { iRoomRepo.deleteNote(it) } }
        BindControl(context, noteModel).cancelBind()

        parentCallback.finish()
    }

    override fun onMenuEdit(editMode: Boolean) = inputControl.makeNotEnabled {
        noteState.isEdit = editMode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = editMode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && iconState.animate
            )

            bindEdit(editMode, noteModel.noteItem)
            bindInput(inputControl.access, noteModel.isSaveEnabled())
            updateNoteState(noteState)
        }

        saveControl.setSaveHandlerEvent(editMode)
    }

    fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    fun onDestroy() = saveControl.setSaveHandlerEvent(isStart = false)

    fun onUpdateData() {
        checkState.setAll(noteModel.rollList)

        callback.apply {
            notifyDataSetChanged(noteModel.rollList)
            changeCheckToggle(state = false)
        }
    }

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            parentCallback.finish()
        }
    }

    fun onPressBack(): Boolean {
        if (!noteState.isEdit) return false

        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate) onRestoreData() else false
        } else {
            true
        }
    }

    private fun onRestoreData(): Boolean {
        if (id == NoteData.Default.ID) return false

        val colorFrom = noteModel.noteItem.color
        noteModel = iRoomRepo.getNoteModel(id)

        callback.notifyDataSetChanged(noteModel.rollList)
        onMenuEdit(editMode = false)
        callback.tintToolbar(colorFrom, noteModel.noteItem.color)

        inputControl.reset()

        return true
    }

    fun onClickAdd(simpleClick: Boolean) {
        val textEnter = callback.clearEnter()

        if (textEnter.isEmpty()) return

        val p = if (simpleClick) noteModel.rollList.size else 0
        val rollItem = RollItem().apply {
            noteId = noteModel.noteItem.id
            text = textEnter
        }

        inputControl.onRollAdd(p, rollItem.toString())

        noteModel.rollList.add(p, rollItem)

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
        callback.scrollToItem(simpleClick, p, noteModel.rollList)
    }

    fun onClickItemCheck(p: Int) {
        val rollList = noteModel.rollList
        val rollItem = rollList[p].apply { isCheck = !isCheck }

        callback.notifyListItem(p, rollItem)

        val check = rollList.getCheck()

        val noteItem = noteModel.noteItem.apply {
            change = context.getTime()
            setCompleteText(check, rollList.size)
        }

        if (checkState.setAll(check, rollList.size)) callback.bindNoteItem(noteItem)

        iRoomRepo.updateRollCheck(noteItem, rollItem)

        BindControl(context, noteModel).updateBind(rankIdVisibleList)
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteModel.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.apply {
            bindInput(inputControl.access, noteModel.isSaveEnabled())
            tintToolbar(check)
        }
    }

    fun onResultRankDialog(check: BooleanArray) {
        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        iRoomRepo.getRankIdList().forEachIndexed { i, id ->
            if (check[i]) {
                rankId.add(id)
                rankPs.add(i.toLong())
            }
        }

        val noteItem = noteModel.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.apply {
            this.rankId = rankId
            this.rankPs = rankPs
        }

        callback.bindInput(inputControl.access, noteModel.isSaveEnabled())
        callback.bindItem(noteItem)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteModel)

        parentCallback.onConvertNote()
    }

    fun onCancelNoteBind() = with(noteModel) {
        noteItem.isStatus = false
        callback.bindItem(noteItem)
    }

}