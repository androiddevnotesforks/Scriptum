package sgtmelon.scriptum.infrastructure.screen.note.parent

import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragmentImpl<N : NoteItem, T : ViewDataBinding> : BindingFragment<T>() {

    abstract val viewModel: ParentNoteViewModel<N>

    protected val connector get() = activity as? NoteConnector

    override fun setupObservers() {
        super.setupObservers()

        val bundleProvider = connector?.bundleProvider
        viewModel.isEdit.observe(this) { bundleProvider?.updateEdit(it) }
        viewModel.noteState.observe(this) { bundleProvider?.updateState(it) }
        viewModel.id.observe(this) { bundleProvider?.updateId(it) }
        viewModel.color.observe(this) {
            bundleProvider?.updateColor(it)
            connector?.updateHolder(it)
        }
    }
}