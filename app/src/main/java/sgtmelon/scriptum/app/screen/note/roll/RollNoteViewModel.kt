package sgtmelon.scriptum.app.screen.note.roll

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.CheckState
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.showToast
import sgtmelon.scriptum.office.utils.AppUtils.swap
import sgtmelon.scriptum.office.utils.HelpUtils.Note.getCheck
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime

/**
 * ViewModel для [RollNoteFragment]
 */
class RollNoteViewModel(application: Application) : AndroidViewModel(application),
        SaveControl.Result,
        InputTextWatcher.TextChange,
        RollWriteHolder.TextChange,
        RollTouchControl.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RollNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context, result = this)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteRepo: NoteRepo

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

    private val checkState = CheckState()

    private val isSaveEnable by lazy { noteRepo.listRoll.size != 0 }

    fun setupData(bundle: Bundle?) {
        id = bundle?.getLong(NoteData.Intent.ID, NoteData.Default.ID) ?: NoteData.Default.ID

        if (::noteRepo.isInitialized) return

        rankVisibleList = iRoomRepo.getRankVisibleList()

        if (id == NoteData.Default.ID) {
            val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.ROLL)
            val statusItem = StatusItem(context, noteItem, false)

            noteRepo = NoteRepo(noteItem, ArrayList(), statusItem)

            noteState = NoteState(isCreate = true)
        } else {
            noteRepo = iRoomRepo.getNoteRepo(id)

            noteState = NoteState(isCreate = false, isBin = noteRepo.noteItem.isBin)
        }

        callback.apply {
            setupBinding(rankVisibleList.isEmpty())
            setupToolbar(noteRepo.noteItem.color, noteState)
            setupDialog(iRoomRepo.getRankDialogName())
            setupEnter(inputControl)
            setupRecycler(inputControl)
        }

        onMenuEdit(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onResultSaveControl() = context.showToast(when (onMenuSave(changeMode = false)) {
        true -> R.string.toast_note_save_done
        false -> R.string.toast_note_save_error
    })

    override fun onResultInputTextChange() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

    override fun onResultInputRollChange() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

    override fun onResultInputRollAfter(p: Int, text: String) { // TODO handler (чтобы была возможность написать что-то обратно
        with(noteRepo.listRoll) {
            if (TextUtils.isEmpty(text)) {
                callback.notifyItemRemoved(p, apply { removeAt(p) })
            } else {
                callback.notifyListItem(p, get(p).apply { this.text = text })
            }
        }
    }

    override fun onResultTouchFlags(drag: Boolean): Int {
        val flagsDrag = when (noteState.isEdit && drag) {
            true -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            false -> 0
        }

        val flagsSwipe = when (noteState.isEdit) {
            true -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            false -> 0
        }

        return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        inputControl.onRollMove(dragFrom, dragTo)

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    override fun onResultTouchSwipe(p: Int) {
        val listRoll = noteRepo.listRoll

        inputControl.onRollRemove(p, listRoll[p].toString())

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        listRoll.removeAt(p)

        callback.notifyItemRemoved(p, listRoll)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        callback.notifyItemMoved(from, to, noteRepo.listRoll.apply { swap(from, to) })
        return true
    }

    override fun onMenuRestore() {
        iRoomRepo.restoreNoteItem(noteRepo.noteItem.id)
        noteCallback.finish()
    }

    override fun onMenuRestoreOpen() {
        noteState.isBin = false

        noteRepo.noteItem.apply {
            change = context.getTime()
            isBin = false
        }

        onMenuEdit(mode = false) // TODO исправить работу иконки назад (происходит анимация)

        iRoomRepo.updateNoteItem(noteRepo.noteItem)
    }

    override fun onMenuClear() {
        iRoomRepo.clearNoteItem(noteRepo.noteItem.id)

        noteRepo.updateStatus(status = false)

        noteCallback.finish()
    }

    override fun onMenuUndo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        //Log.i(TAG, "onUndoClick");
        //
        //        final InputItem inputItem = inputControl.undo();
        //
        //        if (inputItem != null) {
        //            inputControl.setEnabled(false);
        //
        //            final CursorItem cursorItem = inputItem.getCursorItem();
        //
        //            final NoteRepo noteRepo = vm.getNoteRepo();
        //            final NoteItem noteItem = noteRepo.getNoteItem();
        //            final List<RollItem> listRoll = noteRepo.getListRoll();
        //
        //            RollItem rollItem;
        //
        //            switch (inputItem.getTag()) {
        //                case InputDef.rank:
        //                    final StringConv stringConv = new StringConv();
        //                    final List<Long> rankId = stringConv.fromString(inputItem.getValueFrom());
        //
        //                    noteItem.setRankId(rankId);
        //                    break;
        //                case InputDef.color:
        //                    menuControl.setColorFrom(noteItem.getColor());
        //
        //                    final int color = Integer.parseInt(inputItem.getValueFrom());
        //                    noteItem.setColor(color);
        //
        //                    menuControl.startTint(color);
        //                    break;
        //                case InputDef.name:
        //                    assert cursorItem != null : "Cursor @NonNull for this tag";
        //
        //                    nameEnter.requestFocus();
        //                    nameEnter.setText(inputItem.getValueFrom());
        //                    nameEnter.setSelection(cursorItem.getValueFrom());
        //                    break;
        //                case InputDef.roll:
        //                    assert cursorItem != null : "Cursor @NonNull for this tag";
        //
        //                    final int position = inputItem.getPosition();
        //                    listRoll.get(position).setText(inputItem.getValueFrom());
        //
        //                    adapter.setList(listRoll);
        //                    adapter.setCursorPosition(cursorItem.getValueFrom());
        //                    adapter.notifyItemChanged(position);
        //                    break;
        //                case InputDef.rollAdd:
        //                    listRoll.remove(inputItem.getPosition());
        //
        //                    adapter.setList(listRoll);
        //                    adapter.notifyItemRemoved(inputItem.getPosition());
        //                    break;
        //                case InputDef.rollRemove:
        //                    rollItem = new RollItem(inputItem.getValueFrom());
        //                    listRoll.add(inputItem.getPosition(), rollItem);
        //
        //                    adapter.setList(listRoll);
        //                    adapter.setCursorPosition(rollItem.getText().length());
        //                    adapter.notifyItemInserted(inputItem.getPosition());
        //                    break;
        //                case InputDef.rollMove:
        //                    final int positionStart = Integer.parseInt(inputItem.getValueTo());
        //                    final int positionEnd = Integer.parseInt(inputItem.getValueFrom());
        //
        //                    rollItem = listRoll.get(positionStart);
        //                    listRoll.remove(positionStart);
        //                    listRoll.add(positionEnd, rollItem);
        //
        //                    adapter.setList(listRoll);
        //                    adapter.notifyItemMoved(positionStart, positionEnd);
        //                    break;
        //            }
        //
        //            if (inputItem.getTag() != InputDef.roll) {
        //                inputControl.setEnabled(true);
        //            }
        //        }
        //
        //        bindInput();
    }

    override fun onMenuRedo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        //final InputItem inputItem = inputControl.redo();
        //
        //        if (inputItem != null) {
        //            inputControl.setEnabled(false);
        //
        //            final CursorItem cursorItem = inputItem.getCursorItem();
        //
        //            final NoteRepo noteRepo = vm.getNoteRepo();
        //            final NoteItem noteItem = noteRepo.getNoteItem();
        //            final List<RollItem> listRoll = noteRepo.getListRoll();
        //
        //            RollItem rollItem;
        //
        //            switch (inputItem.getTag()) {
        //                case InputDef.rank:
        //                    final StringConv stringConv = new StringConv();
        //                    final List<Long> rankId = stringConv.fromString(inputItem.getValueTo());
        //
        //                    noteItem.setRankId(rankId);
        //                    break;
        //                case InputDef.color:
        //                    menuControl.setColorFrom(noteItem.getColor());
        //
        //                    final int colorTo = Integer.parseInt(inputItem.getValueTo());
        //                    noteItem.setColor(colorTo);
        //
        //                    menuControl.startTint(colorTo);
        //                    break;
        //                case InputDef.name:
        //                    assert cursorItem != null : "Cursor @NonNull for this tag";
        //
        //                    nameEnter.requestFocus();
        //                    nameEnter.setText(inputItem.getValueTo());
        //                    nameEnter.setSelection(cursorItem.getValueTo());
        //                    break;
        //                case InputDef.roll:
        //                    assert cursorItem != null : "Cursor @NonNull for this tag";
        //
        //                    final int position = inputItem.getPosition();
        //                    listRoll.get(position).setText(inputItem.getValueTo());
        //
        //                    adapter.setList(listRoll);
        //                    adapter.setCursorPosition(cursorItem.getValueTo());
        //                    adapter.notifyItemChanged(position);
        //                    break;
        //                case InputDef.rollAdd:
        //                    rollItem = new RollItem(inputItem.getValueTo());
        //                    listRoll.add(inputItem.getPosition(), rollItem);
        //
        //                    adapter.setList(listRoll);
        //                    adapter.setCursorPosition(rollItem.getText().length());
        //                    adapter.notifyItemInserted(inputItem.getPosition());
        //                    break;
        //                case InputDef.rollRemove:
        //                    listRoll.remove(inputItem.getPosition());
        //
        //                    adapter.setList(listRoll);
        //                    adapter.notifyItemRemoved(inputItem.getPosition());
        //                    break;
        //                case InputDef.rollMove:
        //                    final int positionStart = Integer.parseInt(inputItem.getValueFrom());
        //                    final int positionEnd = Integer.parseInt(inputItem.getValueTo());
        //
        //                    rollItem = listRoll.get(positionStart);
        //                    listRoll.remove(positionStart);
        //                    listRoll.add(positionEnd, rollItem);
        //
        //                    adapter.setList(listRoll);
        //                    adapter.notifyItemMoved(positionStart, positionEnd);
        //                    break;
        //            }
        //
        //            if (inputItem.getTag() != InputDef.roll) {
        //                inputControl.setEnabled(true);
        //            }
        //        }
        //
        //        bindInput();
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteRepo.noteItem.rankId))

    override fun onMenuColor() = callback.showColorDialog(noteRepo.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val listRoll = noteRepo.listRoll

        if (noteRepo.listRoll.size == 0) return false

        noteRepo.noteItem.apply {
            change = context.getTime()
            setText(listRoll.getCheck(), listRoll.size)
        }

        /**
         * Переход в режим просмотра
         */
        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(false)
            inputControl.clear()
        }

        noteRepo = iRoomRepo.saveRollNote(noteRepo, noteState.isCreate)

        // TODO проверить id
        noteRepo.listRoll.forEach {
            Log.i("HERE", "id = ${it.id} | ps = ${it.position} | text = ${it.text}")
        }

        if (noteState.isCreate) {
            noteState.isCreate = false

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        callback.notifyList(listRoll)

        return true
    }

    override fun onMenuCheck() {
        val size: Int = noteRepo.listRoll.size
        val isAll = checkState.isAll

        noteRepo.updateCheck(!isAll)

        val noteItem = noteRepo.noteItem
        noteItem.change = context.getTime()
        noteItem.setText(if (isAll) 0 else size, size)

        iRoomRepo.updateRollCheck(noteItem, !isAll)

        callback.bindNoteItem(noteItem)
        callback.changeCheckToggle(state = true)

        onUpdateData()
    }

    override fun onMenuBind() {
        val noteItem = noteRepo.noteItem
        noteItem.isStatus = !noteItem.isStatus

        noteRepo.updateStatus(noteItem.isStatus)

        callback.bindEdit(noteState.isEdit, noteItem)

        iRoomRepo.updateNoteItemBind(noteItem.id, noteItem.isStatus)
    }

    override fun onMenuConvert() = callback.showConvertDialog()

    override fun onMenuDelete() {
        noteRepo.updateStatus(status = false)

        iRoomRepo.deleteNoteItem(noteRepo.noteItem.id)
        noteCallback.finish()
    }

    override fun onMenuEdit(mode: Boolean) {
        inputControl.apply {
            setEnabled(false)
            isChangeEnabled = false
        }

        noteState.isEdit = mode

        callback.apply {
            changeToolbarIcon(
                    drawableOn = mode && !noteState.isCreate,
                    needAnim = !noteState.isCreate && !noteState.isFirst
            )

            bindEdit(mode, noteRepo.noteItem)
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            updateNoteState(noteState)
        }

        saveControl.setSaveHandlerEvent(mode)

        inputControl.apply {
            setEnabled(true)
            isChangeEnabled = true
        }
    }

    fun onPause() = saveControl.onPauseSave(noteState.isEdit)

    fun onUpdateData() {
        checkState.setAll(noteRepo.listRoll)

        callback.apply {
            notifyDataSetChanged(noteRepo.listRoll)
            changeCheckToggle(state = false)
        }
    }

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback.hideKeyboard()
            onRestoreData()
        } else {
            saveControl.needSave = false
            noteCallback.finish()
        }
    }

    fun onPressBack(): Boolean {
        saveControl.needSave = false

        return if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
                onRestoreData()
            }

            true
        } else {
            false
        }
    }

    private fun onRestoreData() {
        val colorFrom = noteRepo.noteItem.color
        noteRepo = iRoomRepo.getNoteRepo(id)

        callback.notifyDataSetChanged(noteRepo.listRoll)
        onMenuEdit(mode = false)
        callback.tintToolbar(colorFrom, noteRepo.noteItem.color)

        inputControl.clear()
    }

    fun onClickAdd(simpleClick: Boolean) {
        val textEnter = callback.clearEnter()

        if (textEnter.isEmpty()) return

        val p = if (simpleClick) noteRepo.listRoll.size else 0
        val rollItem = RollItem().apply {
            noteId = noteRepo.noteItem.id
            text = textEnter
        }

        inputControl.onRollAdd(p, rollItem.toString())

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)

        noteRepo.listRoll.add(p, rollItem)

        callback.scrollToItem(simpleClick, p, noteRepo.listRoll)
    }

    fun onClickItemCheck(p: Int) {
        val listRoll = noteRepo.listRoll

        val rollItem = listRoll[p]
        rollItem.isCheck = !rollItem.isCheck

        callback.notifyListItem(p, rollItem)

        val noteItem = noteRepo.noteItem
        val check = listRoll.getCheck()

        noteItem.change = context.getTime()
        noteItem.setText(check, listRoll.size)

        if (checkState.setAll(check, listRoll.size)) callback.bindNoteItem(noteItem)

        iRoomRepo.updateRollCheck(rollItem, noteItem)
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
            tintToolbar(check)
        }
    }

    fun onResultRankDialog(check: BooleanArray) {
        val rankId = ArrayList<Long>()
        val rankPs = ArrayList<Long>()

        iRoomRepo.getRankId().forEachIndexed { index, l ->
            if (check[index]) {
                rankId.add(l)
                rankPs.add(index.toLong())
            }
        }

        val noteItem = noteRepo.noteItem

        inputControl.onRankChange(noteItem.rankId, rankId)

        noteItem.rankId = rankId
        noteItem.rankPs = rankPs

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess, isSaveEnable)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToText(noteRepo.noteItem)

        noteCallback.showTextFragment(noteRepo.noteItem.id, isSave = false)
    }

}