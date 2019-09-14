package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.copyToClipboard
import sgtmelon.scriptum.interactor.main.notes.INotesInteractor
import sgtmelon.scriptum.interactor.main.notes.NotesInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel

/**
 * ViewModel for [NotesFragment]
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
        INotesViewModel {

    private val iInteractor: INotesInteractor = NotesInteractor(context)

    private val itemList: MutableList<NoteModel> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
        }
    }

    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            setupBinding(iInteractor.isListHide())
            bind()
        }

        if (updateStatus) updateStatus = false
    }

    override fun onClickNote(p: Int) {
        callback?.startActivity(NoteActivity[context, itemList[p].noteEntity])
    }

    override fun onShowOptionsDialog(p: Int) {
        with(itemList[p].noteEntity) {
            val itemArray: Array<String> = context.resources.getStringArray(when (type) {
                NoteType.TEXT -> R.array.dialog_menu_text
                NoteType.ROLL -> R.array.dialog_menu_roll
            })

            itemArray[0] = if (isStatus) context.getString(R.string.dialog_menu_status_unbind) else context.getString(R.string.dialog_menu_status_bind)

            callback?.showOptionsDialog(itemArray, p)
        }
    }

    override fun onResultOptionsDialog(p: Int, which: Int) {
        when (which) {
            Options.BIND -> callback?.notifyItemChanged(p, onMenuBind(p))
            Options.CONVERT -> callback?.notifyItemChanged(p, onMenuConvert(p))
            Options.COPY -> context.copyToClipboard(itemList[p].noteEntity)
            Options.DELETE -> callback?.notifyItemRemoved(p, onMenuDelete(p))
        }
    }

    private fun onMenuBind(p: Int) = itemList.apply {
        val noteEntity = get(p).noteEntity.apply { isStatus = !isStatus }

        viewModelScope.launch { iInteractor.updateNote(noteEntity) }
    }

    private fun onMenuConvert(p: Int) = itemList.apply {
        set(p, iInteractor.convert(get(p)))
    }

    private fun onMenuDelete(p: Int) = itemList.apply {
        get(p).let { viewModelScope.launch { iInteractor.deleteNote(it, callback) } }
        removeAt(p)
    }

    override fun onCancelNoteBind(id: Long) = itemList.forEachIndexed { i, it ->
        if (it.noteEntity.id == id) {
            it.noteEntity.isStatus = false
            callback?.notifyItemChanged(i, itemList)
            return@forEachIndexed
        }
    }


    @IntDef(Options.BIND, Options.CONVERT, Options.COPY, Options.DELETE)
    private annotation class Options {
        companion object {
            const val BIND = 0
            const val CONVERT = 1
            const val COPY = 2
            const val DELETE = 3
        }
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

}