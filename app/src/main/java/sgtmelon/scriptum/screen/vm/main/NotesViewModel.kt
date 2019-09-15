package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.copyToClipboard
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.model.annotation.Options.Notes as Options

/**
 * ViewModel for [NotesFragment]
 */
class NotesViewModel(application: Application) : ParentViewModel<INotesFragment>(application),
        INotesViewModel {

    private val iInteractor: INotesInteractor by lazy { NotesInteractor(context, callback) }

    private val itemList: MutableList<NoteModel> = ArrayList()

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler(iInteractor.theme)
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


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
        val noteEntity = itemList[p].noteEntity

        val itemArray: Array<String> = context.resources.getStringArray(when (noteEntity.type) {
            NoteType.TEXT -> R.array.dialog_menu_text
            NoteType.ROLL -> R.array.dialog_menu_roll
        })

        itemArray[0] = if (noteEntity.isStatus) {
            context.getString(R.string.dialog_menu_status_unbind)
        } else {
            context.getString(R.string.dialog_menu_status_bind)
        }

        callback?.showOptionsDialog(itemArray, p)
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
        get(p).let { viewModelScope.launch { iInteractor.deleteNote(it) } }
        removeAt(p)
    }

    override fun onCancelNoteBind(id: Long) = itemList.forEachIndexed { i, it ->
        if (it.noteEntity.id == id) {
            it.noteEntity.isStatus = false
            callback?.notifyItemChanged(i, itemList)
            return@forEachIndexed
        }
    }

    companion object {
        /**
         * For one-time update statusBar binds
         */
        var updateStatus = true
    }

}