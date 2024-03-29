package sgtmelon.scriptum.infrastructure.screen.note.text

import android.os.Build
import android.widget.ScrollView
import androidx.core.widget.doOnTextChanged
import sgtmelon.extensions.emptyString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSave
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.requestFocusWithCursor
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnTouchSelectionListener
import sgtmelon.scriptum.infrastructure.utils.extensions.setOverscrollColor
import sgtmelon.scriptum.infrastructure.utils.extensions.setTextIfDifferent
import sgtmelon.scriptum.infrastructure.utils.extensions.setTextSelectionSafe
import javax.inject.Inject

/**
 * Fragment for display text note.
 */
class TextNoteFragment : ParentNoteFragment<NoteItem.Text, FragmentTextNoteBinding>() {

    override val layoutId: Int = R.layout.fragment_text_note
    override val type: NoteType = NoteType.TEXT

    @Inject override lateinit var viewModel: TextNoteViewModel
    @Inject override lateinit var permissionViewModel: PermissionViewModel
    @Inject override lateinit var noteSave: NoteSave

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar
    override val panelBar: IncNotePanelBinding? get() = binding?.panel

    override fun inject(component: ScriptumComponent) {
        component.getTextNoteBuilder()
            .set(owner = this)
            .set(lifecycle)
            .set(noteSaveCallback)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    override fun focusOnEnter() {
        binding?.textEnter?.requestFocusWithCursor(binding)
    }

    override fun setupContent() {
        super.setupContent()

        binding?.contentScroll?.setOnTouchSelectionListener(binding?.textEnter)
        binding?.textEnter?.let {
            /** Save changes of content to noteItem model (available only in edit mode). */
            it.doOnTextChanged { text, _, _, _ ->
                val item = viewModel.noteItem.value ?: return@doOnTextChanged
                item.text = text?.toString() ?: return@doOnTextChanged

                /** Invalidate save button separately because [observeNoteItem] will not called. */
                invalidateSaveButton(item)
            }

            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                HistoryAction.Text.Enter(value, cursor)
            })
        }
    }

    override fun collectUndoRedo(result: HistoryResult) {
        when (result) {
            is HistoryResult.Name -> onHistoryName(result)
            is HistoryResult.Text.Enter -> onHistoryEnter(result)
            else -> return /** Other cases would not be updated in UI. */
        }
    }

    private fun onHistoryEnter(result: HistoryResult.Text.Enter) = viewModel.disableHistoryChanges {
        binding?.textEnter?.setTextSelectionSafe(result.value, result.cursor)
    }

    //region Observable staff

    override fun observeNoteItem(item: NoteItem.Text) {
        super.observeNoteItem(item)
        invalidateContent()
    }

    override fun observeEdit(previousEdit: Boolean, isEdit: Boolean) {
        super.observeEdit(previousEdit, isEdit)

        binding?.textEnter?.makeVisibleIf(isEdit) { makeInvisible() }
        binding?.textRead?.makeVisibleIf(!isEdit) { makeInvisible() }
        invalidateContent()
    }

    private fun invalidateContent() {
        val isEdit = viewModel.isEdit.value ?: return
        val item = viewModel.noteItem.value ?: return

        binding?.textEnter?.setTextIfDifferent(item.text)
        /**
         * Set empty text needed for nameEnter has ability to change size
         * inside scrollView.
         */
        binding?.textRead?.text = if (isEdit) emptyString() else item.text
    }

    override fun observeColor(color: Color) {
        super.observeColor(color)

        /** If we can -> make overscroll color the same as note. Otherwise -> hide overscroll. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding?.contentScroll?.setOverscrollColor(color)
        } else {
            binding?.contentScroll?.overScrollMode = ScrollView.OVER_SCROLL_NEVER
        }
    }

    //endregion

    override fun isContentEmpty(): Boolean = binding?.textEnter?.text?.toString().isNullOrEmpty()

}