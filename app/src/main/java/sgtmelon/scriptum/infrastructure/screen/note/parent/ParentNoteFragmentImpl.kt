package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import java.util.Calendar
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
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
    ParentNoteFragment<N>,
    IconBlockCallback {

    private val connector get() = activity as? NoteConnector

    abstract val type: NoteType

    abstract val viewModel: ParentNoteViewModel<N>

    abstract val appBar: IncToolbarNoteBinding?

    private var tintToolbar: TintNoteToolbar? = null
    private var navigationIcon: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogs.getRank() }
    private val colorDialog by lazy { dialogs.getColor() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }
    private val convertDialog by lazy { dialogs.getConvert(type) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(viewModel)
        setupToolbar(view.context, appBar?.content?.toolbar, appBar?.indicator?.colorView)
    }

    override fun setupDialogs() {
        super.setupDialogs()

        rankDialog.apply {
            val rankDialogItems = viewModel.rankDialogItems.value
            if (rankDialogItems != null) {
                itemArray = rankDialogItems
            }

            onPositiveClick { viewModel.onResultRankDialog(check = rankDialog.check - 1) }
            onDismiss { open.clear() }
        }

        colorDialog.apply {
            onPositiveClick { viewModel.onResultColorDialog(colorDialog.check) }
            onDismiss { open.clear() }
        }

        dateDialog.apply {
            onPositiveClick {
                open.skipClear = true
                viewModel.onResultDateDialog(dateDialog.calendar)
            }
            onNeutralClick { viewModel.onResultDateDialogClear() }
            onDismiss { open.clear() }
        }

        timeDialog.apply {
            onPositiveClick { viewModel.onResultTimeDialog(timeDialog.calendar) }
            onDismiss { open.clear() }
        }

        convertDialog.apply {
            onPositiveClick { viewModel.onResultConvertDialog() }
            onDismiss { open.clear() }
        }
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

    //region Cleanup


    override fun tintToolbar(from: Color, to: Color) {
        tintToolbar?.setColorFrom(from)?.startTint(to)
    }

    override fun tintToolbar(color: Color) {
        tintToolbar?.startTint(color)
    }

    override fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean) {
        navigationIcon?.setDrawable(isCancel, needAnim)
    }

    override fun showRankDialog(check: Int) = open.attempt {
        hideKeyboard()
        rankDialog.setArguments(check).safeShow(DialogFactory.Note.RANK, owner = this)
    }

    override fun showColorDialog(color: Color) = open.attempt {
        tintToolbar?.setColorFrom(color)

        hideKeyboard()
        colorDialog.setArguments(color).safeShow(DialogFactory.Note.COLOR, owner = this)
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = open.attempt {
        open.tag = OpenState.Tag.DIALOG

        hideKeyboard()
        dateDialog.setArguments(calendar, resetVisible)
            .safeShow(DialogFactory.Note.DATE, owner = this)
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        open.attempt(OpenState.Tag.DIALOG) {
            hideKeyboard()
            timeDialog.setArguments(calendar, dateList)
                .safeShow(DialogFactory.Note.TIME, owner = this)
        }
    }

    override fun showConvertDialog() = open.attempt {
        hideKeyboard()
        convertDialog.safeShow(DialogFactory.Note.CONVERT, owner = this)
    }

    //endregion

}