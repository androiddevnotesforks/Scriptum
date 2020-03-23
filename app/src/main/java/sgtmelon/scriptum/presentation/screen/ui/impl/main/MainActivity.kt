package sgtmelon.scriptum.presentation.screen.ui.impl.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.presentation.control.ShowHolderControl
import sgtmelon.scriptum.presentation.control.alarm.AlarmControl
import sgtmelon.scriptum.presentation.control.bind.BindControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel
import java.util.*
import javax.inject.Inject

/**
 * Screen which displays main menu and fragments: [RankFragment], [NotesFragment], [BinFragment]
 */
class MainActivity : AppActivity(), IMainActivity {

    @Inject internal lateinit var viewModel: IMainViewModel

    private val alarmControl by lazy { AlarmControl[this] }
    private val bindControl by lazy { BindControl[this] }
    private val holderControl by lazy { ShowHolderControl(arrayOf(toolbarHolder)) }

    private val mainReceiver by lazy { MainReceiver(viewModel) }

    private val fragmentFactory = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragmentFactory.getRankFragment() }
    private val notesFragment by lazy { fragmentFactory.getNotesFragment() }
    private val binFragment by lazy { fragmentFactory.getBinFragment() }

    override val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Main(context = this, fm = fm) }
    private val addDialog by lazy { dialogFactory.getAddDialog() }

    private val toolbarHolder by lazy { findViewById<View?>(R.id.main_toolbar_holder) }
    private val fab by lazy { findViewById<FloatingActionButton?>(R.id.main_add_fab) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getMainBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmControl.initLazy()
        bindControl.initLazy()

        openState.get(savedInstanceState)

        viewModel.onSetup(savedInstanceState)

        registerReceiver(mainReceiver, IntentFilter(ReceiverData.Filter.MAIN))
    }

    override fun onResume() {
        super.onResume()

        openState.changeEnabled = true
        openState.clear()

        /**
         * Show FAB on return to screen if it possible.
         */
        onFabStateChange(state = true)
    }

    override fun onDestroy() {
        super.onDestroy()

        openState.clearBlockCallback()

        holderControl.onDestroy()

        viewModel.onDestroy()
        unregisterReceiver(mainReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            openState.save(bundle = this)
            viewModel.onSaveData(bundle = this)
        })
    }

    /**
     * If touch was outside of [RankFragment.enterCard], when need hide keyboard
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        rankFragment.enterCard?.let { if (!ev.onView(it)) hideKeyboard() }

        return super.dispatchTouchEvent(ev)
    }

    override fun setupNavigation(@IdRes itemId: Int) {
        fab?.setOnClickListener {
            openState.tryInvoke { addDialog.show(fm, DialogFactory.Main.ADD) }
        }

        findViewById<BottomNavigationView>(R.id.main_menu_navigation).apply {
            val animTime = resources.getInteger(R.integer.fade_anim_time).toLong()

            setOnNavigationItemSelectedListener {
                return@setOnNavigationItemSelectedListener openState.tryReturnInvoke {
                    openState.block(animTime)
                    viewModel.onSelectItem(it.itemId)

                    return@tryReturnInvoke true
                } ?: false
            }

            selectedItemId = itemId
        }

        addDialog.apply {
            itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
                dismiss()
                viewModel.onResultAddDialog(it.itemId)
                return@OnNavigationItemSelectedListener true
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }


    override fun onFabStateChange(state: Boolean) = viewModel.onFabStateChange(state)

    override fun setFabState(state: Boolean) {
        fab?.setState(state)
    }

    override fun scrollTop(mainPage: MainPage) {
        when (mainPage) {
            MainPage.RANK -> rankFragment.scrollTop()
            MainPage.NOTES -> notesFragment.scrollTop()
            MainPage.BIN -> binFragment.scrollTop()
        }
    }

    override fun showPage(pageFrom: MainPage, pageTo: MainPage) {
        holderControl.show()

        with(fm) {
            beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)

                if (findFragmentByTag(pageFrom.getFragmentTag()) != null) {
                    hide(pageFrom.getFragmentByName())
                }

                val fragmentTo = pageTo.getFragmentByName()
                val fragmentToTag = pageTo.getFragmentTag()

                if (findFragmentByTag(fragmentToTag) != null) {
                    show(fragmentTo)
                    fragmentTo.onResume()
                } else {
                    add(R.id.main_fragment_container, fragmentTo, fragmentToTag)
                }

                commit()
            }
        }
    }

    override fun startNoteActivity(noteType: NoteType) = openState.tryInvoke {
        startActivity(NoteActivity[this, noteType.ordinal])
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        alarmControl.set(calendar, id, showToast = false)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>) {
        bindControl.notifyNote(item, rankIdVisibleList)
    }

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)


    override fun onReceiveUnbindNote(id: Long) = notesFragment.onReceiveUnbindNote(id)

    override fun onReceiveUpdateAlarm(id: Long) = notesFragment.onReceiveUpdateAlarm(id)


    private fun MainPage.getFragmentByName(): Fragment = let {
        return@let when (it) {
            MainPage.RANK -> rankFragment
            MainPage.NOTES -> notesFragment
            MainPage.BIN -> binFragment
        }
    }

    private fun MainPage.getFragmentTag(): String = let {
        return@let when (it) {
            MainPage.RANK -> FragmentFactory.Main.RANK
            MainPage.NOTES -> FragmentFactory.Main.NOTES
            MainPage.BIN -> FragmentFactory.Main.BIN
        }
    }

    private fun FloatingActionButton.setState(isVisible: Boolean) {
        if (isVisible) show() else hide()
        isEnabled = isVisible
    }

    /**
     * Func return was [MotionEvent] happen on [view]
     */
    private fun MotionEvent?.onView(view: View?): Boolean {
        if (view == null || this == null) return false

        return Rect().apply {
            view.getGlobalVisibleRect(this)
        }.contains(rawX.toInt(), rawY.toInt())
    }

    companion object {
        operator fun get(context: Context) = Intent(context, MainActivity::class.java)
    }

}