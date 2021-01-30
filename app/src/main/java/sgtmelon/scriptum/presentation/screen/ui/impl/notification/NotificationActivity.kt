package sgtmelon.scriptum.presentation.screen.ui.impl.notification

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.presentation.control.snackbar.SnackbarCallback
import sgtmelon.scriptum.presentation.control.snackbar.SnackbarControl
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel
import java.util.*
import javax.inject.Inject

/**
 * Screen with list of notifications.
 */
class NotificationActivity : AppActivity(), INotificationActivity, SnackbarCallback {

    private var binding: ActivityNotificationBinding? = null

    @Inject internal lateinit var viewModel: INotificationViewModel

    private val alarmControl by lazy { AlarmControl[this] }
    private val bindControl by lazy { BindControl[this] }

    private val openState = OpenState()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object: ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = openState.tryInvoke {
                when (view.id) {
                    R.id.notification_click_container -> viewModel.onClickNote(p)
                    R.id.notification_cancel_button -> viewModel.onClickCancel(p)
                }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val snackbarControl = SnackbarControl(
            R.string.snackbar_message_notification, R.string.snackbar_action_cancel,
            callback = this
    )

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.notification_parent_container) }
    private val recyclerContainer by lazy { findViewById<ViewGroup?>(R.id.notification_recycler_container) }
    private val emptyInfoView by lazy { findViewById<View?>(R.id.notification_info_include) }
    private val progressBar by lazy { findViewById<View?>(R.id.notification_progress)}
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.notification_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getNotificationBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_notification)

        alarmControl.initLazy()
        bindControl.initLazy()

        openState.get(savedInstanceState)

        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        /**
         * Clear [openState] after click on item container.
         */
        openState.clear()
        viewModel.onUpdateData()
    }

    override fun onPause() {
        super.onPause()

        snackbarControl.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onSnackbarDismiss()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    override fun setNavigationColor(@Theme theme: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
        } else {
            super.setNavigationColor(theme)
        }
    }

    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
        } else {
            super.setNavigationDividerColor(theme)
        }
    }


    override fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar_container).apply {
            title = getString(R.string.title_notification)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    override fun setupRecycler(@Theme theme: Int) {
        adapter.theme = theme

        recyclerView?.let {
            it.setDefaultAnimator {
                onBindingList()

                /**
                 * Clear [openState] after click on item cancel OR [onSnackbarAction].
                 */
                openState.clear()
            }
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            return@doOnApplyWindowInsets insets
        }

        recyclerView?.doOnApplyWindowInsets { view, insets, _, padding, _ ->
            view.updatePadding(InsetsDir.BOTTOM, insets, padding)
            return@doOnApplyWindowInsets insets
        }
    }


    override fun beforeLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        if (adapter.itemCount == 0) {
            emptyInfoView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            recyclerView?.visibility = View.VISIBLE

            emptyInfoView?.animateAlpha(isVisible = true) {
                emptyInfoView?.visibility = View.GONE
            }
        }
    }

    override fun openNoteScreen(item: NotificationItem) = startActivity(NoteActivity[this, item])

    override fun showSnackbar(@Theme theme: Int) {
        recyclerContainer?.let { snackbarControl.show(it, theme, withInsets = true) }
    }

    override fun onSnackbarAction() = openState.tryInvoke { viewModel.onSnackbarAction() }

    override fun onSnackbarDismiss() = viewModel.onSnackbarDismiss()


    override fun setList(list: List<NotificationItem>) {
        adapter.setList(list)
    }

    override fun notifyList(list: List<NotificationItem>) = adapter.notifyList(list)

    override fun notifyItemRemoved(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemInsertedScroll(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemInserted(p)

        val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

        /**
         *  FirstVisiblePosition can be equal [p] if:
         *  - Click on first item remove;
         *  - Click on snackbar undo.
         *
         *  Then [p] = 0 and firstVisiblePosition = 0.
         */
        if (p <= firstVisiblePosition || p > lastVisiblePosition) {
            recyclerView?.smoothScrollToPosition(p)
        }
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        alarmControl.set(calendar, id, showToast = false)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)

    companion object {
        operator fun get(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}