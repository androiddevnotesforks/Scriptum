package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.office.utils.TimeUtils.getTime
import sgtmelon.scriptum.office.utils.clearAndAdd
import sgtmelon.scriptum.screen.callback.main.NotesCallback
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [NotesFragment]
 *
 * @author SerjantArbuz
 */
class NotesViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotesCallback

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    fun onUpdateData() {
        noteModelList.clearAndAdd(iRoomRepo.getNoteModelList(bin = false))

        callback.apply {
            notifyDataSetChanged(noteModelList)
            bind()
        }

        if (updateStatus) updateStatus = false
    }

    fun onClickNote(p: Int) = with(noteModelList[p].noteItem) {
        callback.startNote(context.getNoteIntent(type, id))
    }

    fun onShowOptionsDialog(p: Int) = with(noteModelList[p].noteItem) {
        val itemArray: Array<String>

        when (type) {
            NoteType.TEXT -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_text)
                itemArray[0] = if (isStatus) context.getString(R.string.dialog_menu_status_unbind) else context.getString(R.string.dialog_menu_status_bind)
            }
            NoteType.ROLL -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_roll)

                itemArray[0] = if (isAllCheck) context.getString(R.string.dialog_menu_check_zero) else context.getString(R.string.dialog_menu_check_all)
                itemArray[1] = if (isStatus) context.getString(R.string.dialog_menu_status_unbind) else context.getString(R.string.dialog_menu_status_bind)

            }
        }

        callback.showOptionsDialog(itemArray, p)
    }

    fun onResultOptionsDialog(p: Int, which: Int) {
        val noteItem = noteModelList[p].noteItem

        when (noteItem.type) {
            NoteType.TEXT -> when (which) {
                OptionsDef.Text.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Text.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Text.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Text.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
            NoteType.ROLL -> when (which) {
                OptionsDef.Roll.check -> callback.notifyItemChanged(p, onMenuCheck(p))
                OptionsDef.Roll.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Roll.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Roll.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Roll.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
        }
    }

    private fun onMenuCheck(p: Int): MutableList<NoteModel> {
        with(noteModelList[p]) {
            val checkText = noteItem.check
            val isAll = checkText[0] == checkText[1]

            noteItem.change = context.getTime()
            noteItem.setCompleteText(if (isAll) 0 else checkText[1], checkText[1])

            iRoomRepo.updateRollCheck(noteItem, !isAll)

            updateCheck(!isAll)

            BindControl(context, noteItem).updateBind()
        }

        return noteModelList
    }

    private fun onMenuBind(p: Int) = noteModelList.apply {
        get(p).noteItem.let {
            it.isStatus = !it.isStatus

            BindControl(context, it).updateBind()

            iRoomRepo.updateNote(it)
        }
    }

    private fun onMenuConvert(p: Int): MutableList<NoteModel> {
        noteModelList[p] = with(noteModelList[p]) {
            return@with when (noteItem.type) {
                NoteType.TEXT -> iRoomRepo.convertToRoll(noteModel = this)
                NoteType.ROLL -> iRoomRepo.convertToText(noteModel = this)
            }
        }

        BindControl(context, noteModelList[p].noteItem).updateBind()

        return noteModelList
    }

    private fun onMenuDelete(p: Int): MutableList<NoteModel> {
        val noteItem = noteModelList[p].noteItem

        viewModelScope.launch { iRoomRepo.deleteNote(noteItem) }

        BindControl(context, noteItem).cancelBind()

        return noteModelList.apply { removeAt(p) }
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

}