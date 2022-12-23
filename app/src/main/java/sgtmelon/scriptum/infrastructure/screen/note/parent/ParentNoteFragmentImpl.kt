package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.databinding.ViewDataBinding
import java.util.Calendar
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.icons.BackToCancelIcon
import sgtmelon.scriptum.infrastructure.utils.tint.TintNoteToolbar
import sgtmelon.test.idling.getIdling

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragmentImpl<N : NoteItem, T : ViewDataBinding> : BindingFragment<T>(),
    ParentNoteFragment<N>,
    IconBlockCallback {

    protected val connector get() = activity as NoteConnector

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
        setupToolbar(view.context, appBar?.content?.toolbar)
    }

    override fun setupDialogs() {
        super.setupDialogs()

        rankDialog.apply {
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

        viewModel.isDataReady.observe(this) {
            // TODO("change enable of button, fields and etc")
            invalidateToolbar()
        }
        viewModel.isEdit.observe(this) { connector.init.isEdit = it }
        viewModel.noteState.observe(this) { connector.init.noteState = it }
        viewModel.id.observe(this) { connector.init.id = it }
        viewModel.color.observe(this) {
            connector.init.color = it
            connector.updateHolder(it)
        }
        viewModel.rankDialogItems.observe(this) { rankDialog.itemArray = it }
        viewModel.noteItem.observe(this) { observeNoteItem(it) }
    }

    abstract fun observeNoteItem(item: N)

    abstract fun setupBinding(callback: NoteMenu)

    @CallSuper
    open fun setupToolbar(context: Context, toolbar: Toolbar?) {
        val color = viewModel.color.value ?: return
        val colorIndicator = appBar?.indicator?.colorView
        tintToolbar = TintNoteToolbar(context, activity?.window, toolbar, colorIndicator, color)
        navigationIcon = BackToCancelIcon(context, toolbar, callback = this)

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }

        /** Save changes of name to noteItem model (it's only will be available in edit mode). */
        appBar?.content?.nameEnter?.doOnTextChanged { it, _, _, _ ->
            viewModel.noteItem.value?.name = it?.toString() ?: return@doOnTextChanged
        }

        // TODO setup back button (rely on edit mode, and disable click while data not loaded)
    }

    override fun setIconEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Anim.ICON)
        open.isBlocked = !isEnabled
    }

    @CallSuper
    open fun invalidateToolbar() {
        val isDataReady = viewModel.isDataReady.value ?: return
        val isEdit = viewModel.isEdit.value ?: return

        appBar?.content?.run {
            nameEnter.makeVisibleIf(condition = !isDataReady && isEdit) { makeInvisible() }
            /** XOR: 01, 10 - true; 00, 11 - false */
            nameRead.makeVisibleIf(condition = isDataReady xor !isEdit) { makeInvisible() }

            if (isDataReady) {
                val item = viewModel.noteItem.value ?: return

                nameEnter.setText(item.name)
                /** Set empty text needed for nameEnter has ability to change size. */
                nameRead.text = if (isEdit) IntentData.Note.Default.NAME else item.name
            } else {
                /**
                 * Name in init only may exists if note is already in [NoteState.EXIST] state,
                 * whats why don't need set text for nameEnter.
                 */
                nameRead.text = connector.init.name
            }
        }
    }

    //region Cleanup

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override val isDialogOpen: Boolean get() = open.isBlocked

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun onPressBack() = viewModel.onPressBack()

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


    override fun showSaveToast(isSuccess: Boolean) {
        val text = if (isSuccess) R.string.toast_note_save_done else R.string.toast_note_save_error
        system.toast.show(context, text)
    }

    override fun finish() {
        activity?.finish()
    }

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        system.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = system.broadcast.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = system.broadcast.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = system.broadcast.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) {
        system.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

}