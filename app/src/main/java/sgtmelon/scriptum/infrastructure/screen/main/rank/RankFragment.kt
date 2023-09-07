package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.content.IntentFilter
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.extensions.collect
import sgtmelon.extensions.emptyString
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.infrastructure.adapter.RankAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.scriptum.infrastructure.adapter.touch.DragAndSwipeTouchHelper
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.dialogs.RenameDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.utils.extensions.clearText
import sgtmelon.scriptum.infrastructure.utils.extensions.disableChangeAnimations
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorDoneAction
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import sgtmelon.test.idling.getIdling
import javax.inject.Inject

/**
 * Screen with list of categories and with ability to create them.
 */
class RankFragment : BindingFragment<FragmentRankBinding>(),
    ListScreen<RankItem>,
    SnackbarDelegator.Callback,
    DragAndSwipeTouchHelper.Callback,
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_rank

    @Inject override lateinit var viewModel: RankViewModel

    private val listAnimation = ShowListAnimation()

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Main(resources) }
    private val renameDialog = DialogStorage(
        DialogFactory.Main.RENAME, owner = this,
        create = { dialogs.getRename() },
        setup = { setupRenameDialog(it) },
    )

    private val touchHelper = DragAndSwipeTouchHelper(callback = this)
    override val adapter by lazy {
        RankAdapter(touchHelper, object : IconBlockCallback {
            override fun setIconEnabled(isEnabled: Boolean) {
                getIdling().change(!isEnabled, IdlingTag.Common.ICON_ANIM)
                parentOpen?.isBlocked = !isEnabled
            }
        }, object : RankClickListener {
            override fun onRankVisibleClick(p: Int, onAction: () -> Unit) {
                changeVisibility(p, onAction)
            }

            override fun onRankClick(p: Int) = showRenameDialog(p)
            override fun onRankCancelClick(p: Int) = removeRank(p)
        })
    }
    override val layoutManager by lazy { LinearLayoutManager(context) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    val snackbar = SnackbarDelegator(
        lifecycle, R.string.snackbar_message_rank, R.string.snackbar_action_cancel, callback = this
    )

    val enterCard: View? get() = binding?.appBar?.enterCard

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getRankBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        super.setupView()

        /**
         * Use [OpenState.attempt] inside add category feature, because calculations happens
         * inside coroutine, not main thread.
         */
        binding?.appBar?.apply {
            toolbar.title = getString(R.string.title_rank)
            clearButton.setOnClickListener { clearEnter() }
            addButton.setOnClickListener {
                parentOpen?.attempt { addFromEnter(toBottom = true) }
            }
            addButton.setOnLongClickListener {
                parentOpen?.attempt { addFromEnter(toBottom = false) }
                return@setOnLongClickListener true
            }
            rankEnter.doOnTextChanged { _, _, _, _ -> notifyToolbar() }
            rankEnter.setEditorDoneAction { if (addButton.isEnabled) addButton.callOnClick() }
        }

        notifyToolbar()

        binding?.recyclerView?.let {
            it.disableChangeAnimations()
            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true) /** The height of all items absolutely the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchHelper).attachToRecyclerView(binding?.recyclerView)
    }

    override fun setupDialogs() {
        super.setupDialogs()
        renameDialog.restore()
    }

    private fun setupRenameDialog(dialog: RenameDialog): Unit = with(dialog) {
        onPositiveClick {
            viewModel.renameItem(position, name).collect(owner = this@RankFragment) {
                notifyToolbar()
            }
        }
        onDismiss {
            renameDialog.release()
            parentOpen?.clear()
        }
    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.list.show.observe(this) {
            val binding = binding ?: return@observe
            listAnimation.startFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.root
            )

            /**
             * Need notify because enter field may contain text like in item name from list
             * (need update add button).
             */
            notifyToolbar()
        }
        viewModel.list.data.observe(this) { onListUpdate(it) }
        viewModel.showSnackbar.observe(this) { if (it) showSnackbar() }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        context?.registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.RANK))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        context?.unregisterReceiver(unbindNoteReceiver)
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateData()

        /** Restore our snack bar if it must be shown. */
        if (viewModel.showSnackbar.value.isTrue() && !snackbar.isDisplayed) {
            showSnackbar()
        }
    }

    //endregion

    private fun notifyToolbar() {
        val (isClearEnable, isAddEnable) = viewModel.getToolbarEnable(getEnterText())

        binding?.appBar?.apply {
            clearButton.isEnabled = isClearEnable
            clearButton.bindBoolTint(isClearEnable, R.attr.clContent, R.attr.clContrast)

            addButton.isEnabled = isAddEnable
            addButton.bindBoolTint(isAddEnable, R.attr.clAccent, R.attr.clContrast)
        }
    }

    private fun getEnterText(): String {
        return binding?.appBar?.rankEnter?.text?.toString() ?: emptyString()
    }

    private fun clearEnter() {
        binding?.appBar?.rankEnter?.clearText()
    }

    private fun addFromEnter(toBottom: Boolean) {
        /**
         * Cancel snackbar because added item may be with same name as canceled one
         * (exist in snackbar stack).
         */
        snackbar.cancel()

        viewModel.addItem(getEnterText(), toBottom).collect(owner = this) {
            when (it) {
                AddState.Deny -> parentOpen?.clear()
                AddState.Prepare -> {
                    hideKeyboard()
                    clearEnter()
                }
                AddState.Complete -> parentOpen?.clear()
            }
        }
    }

    private fun showSnackbar() {
        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = false)
    }

    private inline fun changeVisibility(p: Int, onAction: () -> Unit) {
        parentOpen?.attempt(withSwitch = false) {
            onAction()
            viewModel.changeVisibility(p).collect(owner = this) { updateNotesBind() }
        }
    }

    private fun showRenameDialog(p: Int) {
        parentOpen?.attempt {
            viewModel.getRenameData(p).collect(owner = this) { pair ->
                val (name, nameList) = pair

                /**
                 * Cancel snackbar because item may be renamed to canceled item (exist in
                 * snackbar stack).
                 */
                snackbar.cancel()

                renameDialog.show { setArguments(p, name, nameList) }
            }
        }
    }

    private fun removeRank(p: Int) {
        parentOpen?.attempt(withSwitch = false) {
            viewModel.removeItem(p).collect(owner = this) { updateNotesBind() }
        }
    }

    override fun onSnackbarAction() {
        viewModel.undoRemove().collect(owner = this) { updateNotesBind() }
    }

    override fun onSnackbarDismiss() = viewModel.clearUndoStack()

    private fun updateNotesBind() = run { system?.broadcast?.sendNotifyNotesBind() }

    //region Touch callback

    override fun onTouchAction(inAction: Boolean) {
        parentOpen?.isBlocked = inAction

        if (inAction) {
            snackbar.cancel()
        }
    }

    override fun onTouchGetDrag(): Boolean = parentOpen?.isBlocked != true

    override fun onTouchGetSwipe(): Boolean = false

    override fun onTouchSwiped(position: Int) = Unit

    override fun onTouchMoveStarts() = hideKeyboard()

    override fun onTouchMove(from: Int, to: Int): Boolean {
        /** I know it was closed inside [onTouchMoveStarts], but it's for sure. */
        hideKeyboard()

        viewModel.moveItem(from, to)

        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        parentOpen?.clear()
        viewModel.moveItemResult()
    }

    //endregion

    override fun scrollTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }
}