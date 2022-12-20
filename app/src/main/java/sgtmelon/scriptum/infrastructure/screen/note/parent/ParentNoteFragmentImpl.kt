package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.icons.BackToCancelIcon
import sgtmelon.scriptum.infrastructure.utils.tint.TintNoteToolbar
import sgtmelon.test.idling.getIdling

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragmentImpl<N : NoteItem, T : ViewDataBinding> : BindingFragment<T>(),
    IconBlockCallback {

    abstract val viewModel: ParentNoteViewModel<N>

    protected val connector get() = activity as? NoteConnector

    abstract val appBar: IncToolbarNoteBinding?

    private var tintToolbar: TintNoteToolbar? = null
    private var navigationIcon: IconChangeCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(viewModel)
        setupToolbar(view.context, appBar?.content?.toolbar, appBar?.indicator?.colorView)
    }

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

    abstract fun setupBinding(callback: NoteMenu)

    @CallSuper
    open fun setupToolbar(
        context: Context,
        toolbar: Toolbar?,
        colorIndicator: View?
    ) {
        val color = viewModel.color.value ?: return
        tintToolbar = TintNoteToolbar(context, activity?.window, toolbar, colorIndicator, color)
        navigationIcon = BackToCancelIcon(context, toolbar, callback = this)

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    }

    override fun setEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Anim.ICON)
        open.isBlocked = !isEnabled
    }
}