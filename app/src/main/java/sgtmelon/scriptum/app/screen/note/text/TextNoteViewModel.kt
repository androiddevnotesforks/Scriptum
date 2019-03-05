package sgtmelon.scriptum.app.screen.note.text

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputDef
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.PrefUtils
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import java.util.*

/**
 * ViewModel для [TextNoteFragment]
 */
class TextNoteViewModel(application: Application) : AndroidViewModel(application),
        InputTextWatcher.Result,
        MenuCallback {

    private val context: Context = application.applicationContext

    private val prefUtils = PrefUtils(context)
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: TextNoteCallback
    lateinit var noteCallback: NoteCallback

    private val inputControl: InputControl = InputControl()
    private val saveControl: SaveControl = SaveControl(context)

    private var id: Long = NoteData.Default.ID

    private lateinit var noteRepo: NoteRepo

    private lateinit var noteState: NoteState
    private lateinit var rankVisibleList: List<Long>

    fun setupData(bundle: Bundle?) {
        id = bundle?.getLong(NoteData.Intent.ID, NoteData.Default.ID) ?: NoteData.Default.ID

        if (::noteRepo.isInitialized) return

        rankVisibleList = iRoomRepo.getRankVisibleList()

        if (id == NoteData.Default.ID) {
            val noteItem = NoteItem(context.getTime(), prefUtils.defaultColor, NoteType.TEXT)
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
        }

        onMenuEdit(noteState.isEdit)
        noteState.isFirst = false
    }

    fun saveData(bundle: Bundle) = bundle.putLong(NoteData.Intent.ID, id)

    override fun onInputTextChangeResult() =
            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

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

    override fun onMenuUndo() = onMenuUndoRedo(undo = true)

    override fun onMenuRedo() = onMenuUndoRedo(undo = false)

    private fun onMenuUndoRedo(undo: Boolean) {
        val inputItem = if (undo) inputControl.undo() else inputControl.redo()

        if (inputItem != null) {
            inputControl.setEnabled(false)

            val noteItem = noteRepo.noteItem

            when (inputItem.tag) {
                InputDef.rank ->
                    noteItem.rankId = StringConverter().fromString(inputItem.getValue(undo))
                InputDef.color -> {
                    val colorFrom = noteItem.color
                    val colorTo = Integer.parseInt(inputItem.getValue(undo))

                    noteItem.color = colorTo

                    callback.tintToolbar(colorFrom, colorTo)
                }
                InputDef.name -> callback.changeName(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )
                InputDef.text -> callback.changeText(
                        inputItem.getValue(undo), inputItem.cursorItem!!.getValue(undo)
                )

            }

            inputControl.setEnabled(true)
        }

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    override fun onMenuRank() =
            callback.showRankDialog(iRoomRepo.getRankCheck(noteRepo.noteItem.rankId))

    override fun onMenuColor() = callback.showColorDialog(noteRepo.noteItem.color)

    override fun onMenuSave(changeMode: Boolean): Boolean {
        val noteItem = noteRepo.noteItem

        if (TextUtils.isEmpty(noteRepo.noteItem.text)) return false

        noteItem.change = context.getTime()

        if (changeMode) {
            callback.hideKeyboard()
            onMenuEdit(mode = false)
        }

        noteRepo = iRoomRepo.saveTextNote(noteRepo, noteState.isCreate)

        if (noteState.isCreate) {
            noteState.isCreate = false

            if (!changeMode) {
                callback.changeToolbarIcon(drawableOn = true, needAnim = true)
            }
        }

        inputControl.clear()
        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)

        return true
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
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        }

        saveControl.setSaveHandlerEvent(mode)

        inputControl.apply {
            setEnabled(true)
            isChangeEnabled = true
        }
    }

    fun onClickBackArrow() {
        if (!noteState.isCreate && noteState.isEdit && id != NoteData.Default.ID) {
            callback.hideKeyboard()

            val colorFrom = noteRepo.noteItem.color

            noteRepo = iRoomRepo.getNoteRepo(id)

            onMenuEdit(mode = false)
            callback.tintToolbar(colorFrom, noteRepo.noteItem.color)

            inputControl.clear()
            // TODo проверить
//            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
        } else {
            saveControl.needSave = false
            noteCallback.finish()
        }
    }

    fun onPressBack(): Boolean {
        saveControl.needSave = false

        if (!onMenuSave(changeMode = true)) {
            if (!noteState.isCreate &&noteState.isEdit && id != NoteData.Default.ID) {
                val colorFrom = noteRepo.noteItem.color

                noteRepo = iRoomRepo.getNoteRepo(id)

                onMenuEdit(mode = false)
                callback.tintToolbar(colorFrom, noteRepo.noteItem.color)

                inputControl.clear()
                // TODo проверить
//            callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
                return true
            } else {
                return false
            }
        } else {
            return false
        }

//        if (!textNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
//                    if (noteSt.isEdit && !noteSt.isCreate) {
//                        val colorFrom = noteItem.color
//                        val colorTo = vm.resetFragmentData(noteItem.id, textNoteFragment!!.viewModel)
//
//                        textNoteFragment!!.startTintToolbar(colorFrom, colorTo)
//                        textNoteFragment!!.onMenuEditClick(false)
//                    } else if (noteSt.isCreate) {
//                        super.onBackPressed()
//                    }
//                } else {
//                    super.onBackPressed()
//                }
    }

    fun onResultColorDialog(check: Int) {
        val noteItem = noteRepo.noteItem
        inputControl.onColorChange(noteItem.color, check)
        noteItem.color = check

        callback.apply {
            bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
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

        callback.bindInput(inputControl.isUndoAccess, inputControl.isRedoAccess)
    }

    fun onResultConvertDialog() {
        iRoomRepo.convertToRoll(noteRepo.noteItem)

        noteCallback.showRollFragment(noteRepo.noteItem.id, isSave = false)
    }

}