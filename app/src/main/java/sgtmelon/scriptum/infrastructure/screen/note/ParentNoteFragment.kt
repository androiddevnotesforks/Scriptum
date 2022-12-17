package sgtmelon.scriptum.infrastructure.screen.note

import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IParentNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragment<T : ViewDataBinding> : BindingFragment<T>() {

    abstract val viewModel: IParentNoteViewModel

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