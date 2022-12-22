package sgtmelon.scriptum.infrastructure.screen.note.text

import android.os.Bundle
import android.view.View
import android.widget.EditText
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl

/**
 * Fragment for display text note.
 */
class TextNoteFragmentImpl : ParentNoteFragmentImpl<NoteItem.Text, FragmentTextNoteBinding>(),
    TextNoteFragment,
    IconBlockCallback {

    override val layoutId: Int = R.layout.fragment_text_note
    override val type: NoteType = NoteType.TEXT

    @Inject override lateinit var viewModel: TextNoteViewModel

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar

    // TODO PLAN:
    // TODO 1. Change isEdit/noteState via new livedata value (if first time - skip animation - no views visible)
    //         - Move all binding related with it into UI classes
    // TODO 2. Make common use case for undo/redo (use flow?)
    // TODO 3. Move common functions into use cases? (don't use parent vm class?)

    override fun setupBinding(callback: NoteMenu) {
        binding?.menuCallback = callback
    }

    //region Cleanup

    private val nameEnter: EditText?
        get() = binding?.appBar?.content?.nameEnter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    }

    // TODO not save way to finish activity (view model is lateinit value)
    override fun inject(component: ScriptumComponent) {
        val bundleProvider = (activity as? NoteActivity)?.bundleProvider
        val (isEdit, noteState) = bundleProvider?.state ?: return run { activity?.finish() }
        val (id, _, color) = bundleProvider.data

        if (id == null || color == null) {
            activity?.finish()
            return
        }

        component.getTextNoteBuilder()
            .set(fragment = this)
            .set(isEdit)
            .set(noteState)
            .set(id)
            .set(color)
            .build()
            .inject(fragment = this)
    }

    override fun setupEnter(inputControl: IInputControl) {
        binding?.appBar?.content?.scrollView?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(it, InputAction.NAME, viewModel, inputControl)
            )
            it.addOnNextAction {
                binding?.textEnter?.apply {
                    requestFocus()
                    setSelection(text.toString().length)
                }
            }
        }

        binding?.contentScroll?.requestFocusOnVisible(binding?.textEnter)

        val inputWatcher = InputTextWatcher(
            binding?.textEnter,
            InputAction.TEXT,
            viewModel,
            inputControl
        )
        binding?.textEnter?.addTextChangedListener(inputWatcher)
    }


    override fun onBindingLoad(isRankEmpty: Boolean) {
        binding?.apply {
            this.isDataLoad = true
            this.isRankEmpty = isRankEmpty
        }?.executePendingBindings()
    }

    override fun onBindingNote(item: NoteItem.Text) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEdit(item: NoteItem.Text, isEditMode: Boolean) {
        binding?.apply {
            this.item = item
            this.isEditMode = isEditMode
        }?.executePendingBindings()
    }

    override fun onBindingInput(item: NoteItem.Text, inputAccess: InputControl.Access) {
        binding?.apply {
            this.item = item
            this.inputAccess = inputAccess
        }?.executePendingBindings()
    }


    override fun focusOnEdit(isCreate: Boolean) {
        view?.post {
            if (isCreate) {
                nameEnter?.requestSelectionFocus()
            } else {
                binding?.textEnter?.requestSelectionFocus()
            }
        }
    }

    override fun changeName(text: String, cursor: Int) {
        nameEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }

    override fun changeText(text: String, cursor: Int) {
        binding?.textEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }

    //endregion

}