package sgtmelon.scriptum.infrastructure.screen.note.text

import androidx.core.widget.doOnTextChanged
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnTouchSelectionListener

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
    override val panelBar: IncNotePanelContentBinding? get() = binding?.panel?.content

    override fun focusOnEnter() {
        binding?.textEnter?.requestSelectionFocus()
    }

    override fun setupContent() {
        super.setupContent()

        binding?.contentScroll?.setOnTouchSelectionListener(binding?.textEnter)
        binding?.textEnter?.let {
            /** Save changes of content to noteItem model (available only in edit mode). */
            it.doOnTextChanged { text, _, _, _ ->
                viewModel.noteItem.value?.text = text?.toString() ?: return@doOnTextChanged
            }

            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                HistoryAction.Text.Enter(value, cursor)
            })
        }
    }

    //region Observable staff

    override fun observeDataReady(it: Boolean) {
        super.observeDataReady(it)

        binding?.contentScroll?.makeVisibleIf(it) { makeInvisible() }
    }

    override fun observeEdit(previousEdit: Boolean, isEdit: Boolean) {
        super.observeEdit(previousEdit, isEdit)

        binding?.textEnter?.makeVisibleIf(isEdit) { makeInvisible() }
        binding?.textRead?.makeVisibleIf(!isEdit) { makeInvisible() }
        invalidateContent()
    }

    override fun observeNoteItem(item: NoteItem.Text) {
        super.observeNoteItem(item)
        invalidateContent()
    }

    private fun invalidateContent() {
        val isEdit = viewModel.isEdit.value ?: return
        val item = viewModel.noteItem.value ?: return

        binding?.textEnter?.setText(item.text)
        /**
         * Set empty text needed for nameEnter has ability to change size
         * inside scrollView.
         */
        binding?.textRead?.text = if (isEdit) "" else item.text
    }

    //endregion

    //region Cleanup

    // TODO PLAN:
    // TODO 1. Change isEdit/noteState via new livedata value (if first time - skip animation - no views visible)
    //         - Move all binding related with it into UI classes
    // TODO 2. Make common use case for undo/redo (use flow?)
    // TODO 3. Move common functions into use cases? (don't use parent vm class?)

    // TODO check how it will work with rotation end other staff
    override fun inject(component: ScriptumComponent) {
        component.getTextNoteBuilder()
            .set(fragment = this)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    override fun changeName(text: String, cursor: Int) {
        appBar?.content?.nameEnter?.apply {
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