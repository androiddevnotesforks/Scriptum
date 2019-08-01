package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.copyToClipboard
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel

/**
 * ViewModel for [NotesFragment]
 *
 * @author SerjantArbuz
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
        INotesViewModel {

    private val noteModelList: MutableList<NoteModel> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iPreferenceRepo.theme)
        }
    }

    override fun onUpdateData() {
        noteModelList.clearAndAdd(iRoomRepo.getNoteModelList(bin = false))

        callback?.apply {
            notifyDataSetChanged(noteModelList)
            setupBinding(iRoomRepo.isListHide(bin = false))
            bind()
        }

        if (updateStatus) updateStatus = false
    }

    override fun onClickNote(p: Int) {
        with(noteModelList[p].noteEntity) {
            callback?.startActivity(context.getNoteIntent(type, id))
        }
    }

    override fun onShowOptionsDialog(p: Int) {
        with(noteModelList[p].noteEntity) {
            val itemArray: Array<String> = when (type) {
                NoteType.TEXT -> context.resources.getStringArray(R.array.dialog_menu_text)
                NoteType.ROLL -> context.resources.getStringArray(R.array.dialog_menu_roll)
            }

            itemArray[0] = if (isStatus) context.getString(R.string.dialog_menu_status_unbind) else context.getString(R.string.dialog_menu_status_bind)

            callback?.showOptionsDialog(itemArray, p)
        }
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            OptionsNote.bind -> callback?.notifyItemChanged(p, onMenuBind(p))
            OptionsNote.convert -> callback?.notifyItemChanged(p, onMenuConvert(p))
            OptionsNote.copy -> context.copyToClipboard(noteModelList[p].noteEntity)
            OptionsNote.delete -> callback?.notifyItemRemoved(p, onMenuDelete(p))
        }
    }

    private fun onMenuBind(p: Int) = noteModelList.apply {
        get(p).noteEntity.let {
            it.isStatus = !it.isStatus

            viewModelScope.launch { iRoomRepo.updateNote(it) }
            BindControl(context, it).updateBind()
        }
    }

    private fun onMenuConvert(p: Int) = noteModelList.apply {
        set(p, get(p).let {
            return@let when (it.noteEntity.type) {
                NoteType.TEXT -> iRoomRepo.convertToRoll(it)
                NoteType.ROLL -> iRoomRepo.convertToText(it)
            }
        })

        BindControl(context, get(p).noteEntity).updateBind()
    }

    private fun onMenuDelete(p: Int) = noteModelList.apply {
        get(p).noteEntity.let {
            viewModelScope.launch { iRoomRepo.deleteNote(it) }
            BindControl(context, it).cancelBind()
        }

        removeAt(p)
    }

    override fun onCancelNoteBind(id: Long) = noteModelList.forEachIndexed { i, it ->
        if (it.noteEntity.id == id) {
            it.noteEntity.isStatus = false
            callback?.notifyItemChanged(i, noteModelList)
            return@forEachIndexed
        }
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true

        private object OptionsNote {
            const val bind = 0
            const val convert = 1
            const val copy = 2
            const val delete = 3
        }
    }

}