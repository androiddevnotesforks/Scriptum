package sgtmelon.scriptum.infrastructure.screen.note.text

import android.widget.EditText
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
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

    // TODO PLAN:
    // TODO 1. Change isEdit/noteState via new livedata value (if first time - skip animation - no views visible)
    //         - Move all binding related with it into UI classes
    // TODO 2. Make common use case for undo/redo (use flow?)
    // TODO 3. Move common functions into use cases? (don't use parent vm class?)

    //region Cleanup

    private val nameEnter: EditText?
        get() = binding?.appBar?.content?.nameEnter

    //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //        super.onViewCreated(view, savedInstanceState)
    //        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    //    }

    // TODO check how it will work with rotation end other staff
    override fun inject(component: ScriptumComponent) {
        component.getTextNoteBuilder()
            .set(fragment = this)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    override fun setupEnter(history: NoteHistory) {
        //        binding?.appBar?.content?.scrollView?.setOnTouchSelectionListener(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                history.add(HistoryAction.Name(value, cursor))
            })

            it.addOnNextAction {
                binding?.textEnter?.apply {
                    requestFocus()
                    setSelection(text.toString().length)
                }
            }
        }

        binding?.contentScroll?.setOnTouchSelectionListener(binding?.textEnter)

        binding?.textEnter?.let {
            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                history.add(HistoryAction.Text.Enter(value, cursor))
            })
        }
    }


    override fun onBindingLoad() {
        binding?.apply { this.isDataLoad = true }?.executePendingBindings()
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

    //    override fun onBindingInput(
    //        item: NoteItem.Text,
    //        historyMove: HistoryMoveAvailable
    //    ) {
    //        binding?.apply {
    //            this.item = item
    //            this.historyMove = historyMove
    //        }?.executePendingBindings()
    //    }


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